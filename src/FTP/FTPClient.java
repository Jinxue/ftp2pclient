package FTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * FTPClient is a simple package that implements a Java FTP client. With
 * SimpleFTP, you can connect to an FTP server and upload multiple files.
 * <p>
 * The initial version is by Paul Mutton, 
 * <a href="http://www.jibble.org/">http://www.jibble.org/ </a>
 * 
 * Zhang Jinxue has extended this client
 * Notice that there is a FtpClient library in Java standard library
 */

public class FTPClient {

	private Socket socket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	private static boolean DEBUG = true;
	private String serverIP = null;

	// This is socket for data transmission
	private Socket dataSocket = null;
	/**
	 * Create an instance of SimpleFTP.
	 */
	public FTPClient() {
	}

	/*
	 * Connect, cwd, cdup, quit is belong to Access Control commands in FTP protocol
	 */
	/**
	 * Connects to the default port of an FTP server and logs in as
	 * anonymous/anonymous.
	 */
	public void connect(String host) throws IOException {
		connect(host, 21);
	}

	/**
	 * Connects to an FTP server and logs in as anonymous/anonymous.
	 */
	public void connect(String host, int port) throws IOException {
		connect(host, port, "anonymous", "anonymous");
	}

	public void connect(String host, String user,
			String pass) throws IOException {
		connect(host, 21, user, pass);
	}
	/**
	 * Connects to an FTP server and logs in with the supplied username and*
	 * password.
	 */
	public void connect(String host, int port, String user,
			String pass) throws IOException {
		serverIP = host;
		if (socket != null) {
			throw new IOException(
					"FTPClient is already connected. Disconnect first.");
		}
		socket = new Socket(host, port);
		reader = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(socket
				.getOutputStream()));
		String response = readLine();
		if (!response.startsWith("220 ")) {
			throw new IOException(
					"FTPClient received an unknown response when connecting to the FTP server: "
							+ response);
		}
		sendLine("USER " + user);
		response = readLine();
		if (!response.startsWith("331 ")) {
			throw new IOException(
					"FTPClient received an unknown response after sending the user: "
							+ response);
		}
		sendLine("PASS " + pass);
		response = readLine();
		if (!response.startsWith("230 ")) {
			throw new IOException(
					"FTPClient was unable to log in with the supplied password: "
							+ response);
		}
		// Now logged in.
	}

	/**
	 * Disconnects from the FTP server.
	 */
	public void disconnect() throws IOException {
		try {
			sendLine("QUIT");
		} finally {
			socket = null;
		}
	}

	/**
	 * Changes the working directory (like cd). Returns true if successful.
	 */
	public boolean cwd(String dir) throws IOException {
		sendLine("CWD " + dir);
		String response = readLine();
		return (response.startsWith("250 "));
	}

	/*
	 * Change the working directory to parent 
	 */
	public boolean cdup() throws IOException{
		sendLine("CDUP ");
		String response = readLine();
		return (response.startsWith("250 "));
	}
	
	/*
	 *  PASV and PORT are both Transfer Parameter Commands in FTP protocol
	 *  Here we only implement the PASV mode
	 */
	
	/*
	 * Set the data transport to as passive mode to avoid NAT or
	 * firewall problems at the client end
	 */
	public boolean setPasv() throws IOException{
//		if((dataSocket != null) && dataSocket.isConnected()){
//			System.out.println("The data socket is established!");
//			return true;
//		}else{
			sendLine("PASV");
			String response = readLine();
			if (!response.startsWith("227 ")) {
				throw new IOException(
						"FTPClient could not request passive mode: " + response);
			}
			String ip = null;
			int port = -1;
			int opening = response.indexOf('(');
			int closing = response.indexOf(')', opening + 1);
			if (closing > 0) {
				String dataLink = response.substring(opening + 1, closing);
				StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
				try {
					ip = tokenizer.nextToken() + "." + tokenizer.nextToken()
							+ "." + tokenizer.nextToken() + "."
							+ tokenizer.nextToken();
					port = Integer.parseInt(tokenizer.nextToken()) * 256
							+ Integer.parseInt(tokenizer.nextToken());
				} catch (Exception e) {
					throw new IOException(
							"FTPClient received bad data link information: "
									+ response);
				}
			}
			dataSocket = new Socket(ip, port);
			// boolean co = dataSocket.isConnected();
			// co = dataSocket.isClosed();
			return true;
//		}
	}
	
	/**
	 * Set the data mode for IPv6
	 */
	public boolean setEPasv() throws IOException{
		sendLine("EPSV 2");
		String response = readLine();
		if(response.startsWith("522 ")){
			System.out.println("Sorry, the IPv6 protocol is not supported");
			return false;
		}
		if (!response.startsWith("229 ")) {
			throw new IOException(
					"FTPClient could not request passive mode: " + response);
		}
		//String ip = null;
		int port = -1;
		int closing = response.lastIndexOf('|') - 1;
		int opening = response.lastIndexOf('|', closing);
		
		if (closing > 0) {
			String sPort = response.substring(opening + 1, closing + 1);
			port = Integer.parseInt(sPort);
		}
		dataSocket = new Socket(serverIP, port);

		return true;
	} 
	/**
	 * PWD, STOR, RETR, LIST, DELE, RMD, MKD, RNFR/RMTO, and ABOR are the FTP
	 * Service Commands
	 */
	
	/**
	 * Returns the working directory of the FTP server it is connected to.
	 */
	public String pwd() throws IOException {
		sendLine("PWD");
		String dir = null;
		String response = readLine();
		if (response.startsWith("257 ")) {
			int firstQuote = response.indexOf('\"');
			int secondQuote = response.indexOf('\"', firstQuote + 1);
			if (secondQuote > 0) {
				dir = response.substring(firstQuote + 1, secondQuote);
			}
		}
		return dir;
	}

	/**
	 * List the directory or file information  
	 * @throws IOException 
	 * @throws IOException 
	 */
	public boolean list() throws IOException{
		return list("./");
	}
	
	public boolean list(String filename) throws IOException {
		setPasv();
//		setEPasv();
		if(filename.equals("./"))
			sendLine("LIST ");
		else
			sendLine("LIST " + filename);
		
		String response = readLine();
		if (!response.startsWith("150 ")) {
			throw new IOException(
					"FTPClient was not allowed to get the directory list: " + response);
		}
		//BufferedInputStream input = new BufferedInputStream(dataSocket.getInputStream());
		BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket
				.getInputStream()));
		String totalInfo = new String();
		String info = null;
		while((info = dataReader.readLine()) != null){
			totalInfo += info + "\r\n";
		}
		if(DEBUG)
			System.out.println(totalInfo);
		response = readLine();
		return (response.startsWith("226 "));
	}
	
	/**
	 * Sends a file to be stored on the FTP server. Returns true if the file
	 * transfer was successful.
	 */
	public boolean stor(File file) throws IOException {
		if (file.isDirectory()) {
			throw new IOException("FTPClient cannot upload a directory.");
		}
		String filename = file.getName();
		return stor(new FileInputStream(file), filename);
	}

	/**
	 * Sends a file to be stored on the FTP server. Returns true if the file
	 * transfer was successful. The file is sent in passive mode to avoid NAT or
	 * firewall problems at the client end.
	 */
	public boolean stor(InputStream inputStream, String filename)
			throws IOException {
		BufferedInputStream input = new BufferedInputStream(inputStream);
		
		// Set the passive mode for data transmission
		setPasv();
		
		sendLine("STOR " + filename);
		String response = readLine();
		if (!response.startsWith("150 ")) {
			throw new IOException(
					"FTPClient was not allowed to send the file: " + response);
		}
		BufferedOutputStream output = new BufferedOutputStream(dataSocket
				.getOutputStream());
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		output.flush();
		output.close();
		input.close();
		response = readLine();
		return response.startsWith("226 ");
	}

	/**
	 * Send the file to the server
	 * @throws IOException 
	 */
	public boolean download(String filename) throws IOException{
		// Set the passive mode for data transmission
		setPasv();
		
		sendLine("RETR " + filename);
		String response = readLine();
		if(response.startsWith("550 ")){
			return false;
		}
		if (!response.startsWith("150 ")) {
			throw new IOException(
					"FTPClient was not allowed to download the file: " + response);
		}
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File(filename)));

		BufferedInputStream input = new BufferedInputStream(dataSocket
				.getInputStream());
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		output.flush();
		output.close();
		input.close();

		response = readLine();
		return (response.startsWith("226 "));
	}
	
	/**
	 * Delete a file in server
	 * @throws IOException 
	 */
	public boolean delete(String filename) throws IOException{
		sendLine("DELE " + filename);
		String response = readLine();
		return (response.startsWith("250 "));
	}
	
	/**
	 * make the directory in server
	 */
	public boolean mkDirectory(String dir) throws IOException{
		sendLine("MKD " + dir);
		String response = readLine();
		return (response.startsWith("250 "));
	}
	
	/**
	 * Remove the directory in server
	 */
	public boolean rmDirectory(String dir) throws IOException{
		sendLine("RMD " + dir);
		String response = readLine();
		return (response.startsWith("250 "));
	}

	/**
	 * Tell the server to abort the previous command and data transmission related
	 */
	public boolean abort(String dir) throws IOException{
		sendLine("ABOR");
		String response = readLine();
		return (response.startsWith("250 "));
	}
	
	/**
	 * Rename the file in server
	 */
	public boolean rename(String oldName, String newName) throws IOException{
		sendLine("RNFR " + oldName);
		String response = readLine();
		if(!response.startsWith("350 "))
			return false;
		
		sendLine("RNTO " + newName);
		response = readLine();
		return (response.startsWith("250 "));
	}
	
	/**
	 * Enter binary mode for sending binary files.
	 */
	public boolean bin() throws IOException {
		sendLine("TYPE I");
		String response = readLine();
		return (response.startsWith("200 "));
	}

	/**
	 * Enter ASCII mode for sending text files. This is usually the default
	 * mode. Make sure you use binary mode if you are sending images or other
	 * binary data, as ASCII mode is likely to corrupt them.
	 */
	public boolean ascii() throws IOException {
		sendLine("TYPE A");
		String response = readLine();
		return (response.startsWith("200 "));
	}

	/**
	 * Sends a raw command to the FTP server.
	 */
	private void sendLine(String line) throws IOException {
		if (socket == null) {
			throw new IOException("FTPClient is not connected.");
		}
		try {
			writer.write(line + "\r\n");
			writer.flush();
			if (DEBUG) {
				System.out.println("> " + line);
			}
		} catch (IOException e) {
			socket = null;
			throw e;
		}
	}

	private String readLine() throws IOException {
		String line = reader.readLine();
		if (DEBUG) {
			System.out.println("< " + line);
		}
		return line;
	}

}
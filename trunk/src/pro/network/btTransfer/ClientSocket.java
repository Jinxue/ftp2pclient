package pro.network.btTransfer;

import java.net.*;
import java.io.*;

public class ClientSocket {

	private String ip;
	private int port;
	private Socket socket = null;
	DataOutputStream out = null;
	DataInputStream getMessageStream = null;
	
	public ClientSocket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public void createConnection() throws Exception {
		try{
			socket = new Socket(ip, port);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void sendMessage(String message) throws Exception {
		try{
			out = new DataOutputStream(socket.getOutputStream());
			if(message.equals("Windows")) {
				out.writeByte(0x1);
				out.flush();
				return;
			} 
			out.writeUTF(message);
			out.flush();
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public DataInputStream getMessageStream() throws Exception {
		try{
			getMessageStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch(Exception e) {
			e.printStackTrace();
			getMessageStream = null;
			throw e;
		}
		return getMessageStream;
	}
	
	public void closeConnection() {
		try{
			if(out != null) {
				out.close();
			}
			if(getMessageStream != null) {
				getMessageStream.close();
			}
			if(socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package FTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SingleDownloadThread extends Thread {

	private File localFile;
	private Socket dataSocket;
	private FTPClient ftpClient;

	public SingleDownloadThread(File file, Socket socket, FTPClient ftp) {
		this.localFile = file;
		this.dataSocket = socket;
        this.ftpClient = ftp;
	}

	public void run() {
		System.out.println("线程" + this.getId() + "开始下载");

//		FTPClient.SIGNAL++;
		ftpClient.incDownloadThread();

		try {
			BufferedOutputStream output = new BufferedOutputStream(
					new FileOutputStream(localFile));

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
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
//		FTPClient.SIGNAL --;
		ftpClient.downloadDone();
		System.out.println("线程" + this.getId() + "已经完成");

	}
}
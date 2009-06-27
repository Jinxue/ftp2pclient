package FTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DownloadThread extends Thread {

	private File localFile;
	private Socket dataSocket;

	public DownloadThread(File file, Socket socket) {
		this.localFile = file;
		this.dataSocket = socket;
	}

	public void run() {
		System.out.println("线程" + this.getId() + "开始下载");

//		FTPClient.SIGNAL++;

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
		System.out.println("线程" + this.getId() + "已经完成");

	}
}
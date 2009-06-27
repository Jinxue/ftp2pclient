package FTP;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class SplitDownloadThread extends Thread {
	
	FTPServerInfo ftpServer;
	int length;
	int start;
	File localFile;
	String filename;	// Remote file name
	String currentPath;
    private FTPClient ftpClient;

	public SplitDownloadThread(FTPServerInfo server, int len, int str, FTPClient ftp) {
		// TODO Auto-generated constructor stub
		ftpServer = server;
		length = len;
		start = str;
        this.ftpClient = ftp;
	}
	
	public void setLocalFile(File file){
		localFile = file;
	}
	
	public void setRemoteFile(String path, String name){
		filename = name;
		currentPath = path;
	}

	public void run (){

		System.out.println("线程" + this.getId() + "开始下载");

//		FTPClient.SIGNAL++;
//		ftpClient.incDownloadThread();

		FTPClient client = new FTPClient();
//		int singal = FTPClient.SIGNAL;
		try {
			client.connect(ftpServer);
			client.cwd(currentPath);
			client.restart(start);
			client.singleDownload(filename, localFile, length);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ftpClient.downloadDone();
		//int singal = FTPClient.SIGNAL;
		System.out.println("线程" + this.getId() + "已经完成");
	}
}

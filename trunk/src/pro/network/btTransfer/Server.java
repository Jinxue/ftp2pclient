package pro.network.btTransfer;
/*
 * 服务器类，实现端口侦听、数据传送等功能
 * 作者：李腾飞
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class Server extends Thread {
	
	int port;                      //端口号
	
	public Server(int port) {
		this.port = port;
	}
	
	/**
	 * 每个线程执行的操作
	 */
	public void run() {
		Socket s = null;
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {

				/* 侦听端口 */
				s = ss.accept();
				System.out.println("建立socket连接");
				
				/* 得到文件的路径和文件名 */
				DataInputStream dataIn = new DataInputStream(s.getInputStream());
				String filePath = dataIn.readUTF();
				String fileName = dataIn.readUTF();
				
				/* 向客户端传送文件的大小 */
				File file = new File(filePath + fileName);
				DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
				dataOut.writeLong(file.length());
				dataOut.flush();
				
				/* 接收客户端传来的偏移量和长度 */
				long off = dataIn.readLong();
				long segLen = dataIn.readLong();
				
				DataInputStream fileIn = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
				fileIn.skipBytes((int) off);
				
				int bufferSize = 1024;
				byte[] buf = new byte[bufferSize];
				
				int hasSentLen = 0;                       //已经传送的数据的大小
				
				System.out.println("开始传送数据");
				while (true) {
					int read = 0;
					if (fileIn != null) {
						if((segLen - hasSentLen) >= bufferSize) {
							read = fileIn.read(buf, 0, bufferSize);
						} else {
							read = fileIn.read(buf, 0, (int) segLen - hasSentLen);
						}						
					}
					hasSentLen += read;
					dataOut.write(buf, 0, read);
					if(hasSentLen == segLen) {
						break;
					}
				}
				dataOut.flush();
				
				
				dataIn.close();
				dataOut.close();
				fileIn.close();
				s.close();
				System.out.println("文件传输完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

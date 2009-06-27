package pro.network.btTransfer;
/*
 * �������࣬ʵ�ֶ˿����������ݴ��͵ȹ���
 * ���ߣ����ڷ�
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class Server extends Thread {
	
	int port;                      //�˿ں�
	
	public Server(int port) {
		this.port = port;
	}
	
	/**
	 * ÿ���߳�ִ�еĲ���
	 */
	public void run() {
		Socket s = null;
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {

				/* �����˿� */
				s = ss.accept();
				System.out.println("����socket����");
				
				/* �õ��ļ���·�����ļ��� */
				DataInputStream dataIn = new DataInputStream(s.getInputStream());
				String filePath = dataIn.readUTF();
				String fileName = dataIn.readUTF();
				
				/* ��ͻ��˴����ļ��Ĵ�С */
				File file = new File(filePath + fileName);
				DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
				dataOut.writeLong(file.length());
				dataOut.flush();
				
				/* ���տͻ��˴�����ƫ�����ͳ��� */
				long off = dataIn.readLong();
				long segLen = dataIn.readLong();
				
				DataInputStream fileIn = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
				fileIn.skipBytes((int) off);
				
				int bufferSize = 1024;
				byte[] buf = new byte[bufferSize];
				
				int hasSentLen = 0;                       //�Ѿ����͵����ݵĴ�С
				
				System.out.println("��ʼ��������");
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
				System.out.println("�ļ��������");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

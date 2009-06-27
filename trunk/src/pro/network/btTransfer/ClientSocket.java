package pro.network.btTransfer;
/*
 * ����ͻ��˵�Socket
 * ���ߣ����ǽ�
 */
import java.net.*;
import java.io.*;

public class ClientSocket {

	private String ip;                             //IP��ַ
	private int port;                              //�˿ں�
	private Socket socket = null;                  //Socket����
	DataOutputStream out = null;                   //����������Ķ���
	DataInputStream getMessageStream = null;       //����������
	
	/**
	 * ���캯��
	 * @param ip   IP��ַ
	 * @param port �˿ں�
	 */
	public ClientSocket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	/**
	 * ��������
	 * @throws Exception
	 */
	public void createConnection() throws Exception {
		try{
			socket = new Socket(ip, port);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * ��������˴���message
	 * @param message
	 * @throws Exception
	 */
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
	
	/**
	 * �õ�����������
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * �ر�socket����
	 */
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

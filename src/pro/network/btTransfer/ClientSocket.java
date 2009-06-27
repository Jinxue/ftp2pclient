package pro.network.btTransfer;
/*
 * 管理客户端的Socket
 * 作者：苗亚杰
 */
import java.net.*;
import java.io.*;

public class ClientSocket {

	private String ip;                             //IP地址
	private int port;                              //端口号
	private Socket socket = null;                  //Socket对象
	DataOutputStream out = null;                   //数据输出流的对象
	DataInputStream getMessageStream = null;       //数据输入流
	
	/**
	 * 构造函数
	 * @param ip   IP地址
	 * @param port 端口号
	 */
	public ClientSocket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	/**
	 * 建立连接
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
	 * 向服务器端传送message
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
	 * 得到数据输入流
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
	 * 关闭socket连接
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

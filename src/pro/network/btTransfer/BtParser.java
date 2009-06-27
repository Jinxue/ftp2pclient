package pro.network.btTransfer;
/*
 * 解析Bt文件，得到服务器URL，端口号，要下载的文件路径和文件名
 * 作者：苗亚杰
 * 日期：2009-05-30
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class BtParser {

	private String btFilePath = null;               //bt文件的路径
	
	private ArrayList<String> btFileContent;        //bt文件的内容
	
	/**
	 * 构造函数
	 * @param btFilePath
	 */
	public BtParser(String btFilePath) {
		this.btFilePath = btFilePath;
		btFileContent = new ArrayList<String>();
		getBtFileContent();
	}
	
	/**
	 * 得到下载文件的信息
	 * @return 包含下载文件信息的FileInfo类对象的集合
	 */
	public ArrayList<FileInfo> getFileInfo() {
		ArrayList<FileInfo> btParseResults = new ArrayList<FileInfo>();
		
		for (int i = 0; i < btFileContent.size(); i++) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setId(i + 1);
			fileInfo.setFilename(getFileName(btFileContent.get(i)));
			fileInfo.setPath(getPath(btFileContent.get(i)));
			fileInfo.setServerURL(getServerURL(btFileContent.get(i)));
			fileInfo.setPort(getPort(btFileContent.get(i)));
			
			btParseResults.add(fileInfo);
		}

		return btParseResults;
	}
	
    /**
     * 从btFilePath中读取Bt文件的内容
     */
	private void getBtFileContent() {
		File file = new File(btFilePath);
		try{
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String oneLine = null;
			
			while((oneLine = bf.readLine()) != null) {
				btFileContent.add(oneLine);
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析出文件路径信息
	 * @param btOneItem
	 * @return 文件的路径
	 */
	private String getPath(String btOneItem) {
		
		/* 得到Server部分的长度 */
		Pattern p = Pattern.compile("path([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String pathLen = null;
		while(m.find()) {
			pathLen = m.group(1);
		}
		
		/* 得到从http开始到server描述结束 */
		String path = null;
		p = Pattern.compile("path" + pathLen + ":" +"(.{" + pathLen + "})");
		m = p.matcher(btOneItem);
		while(m.find()) {
			path = m.group(1);
		}
		return path;
	}
	
	/**
	 * 解析出文件名信息
	 * @param btOneItem
	 * @return 文件名
	 */
	private String getFileName(String btOneItem) {
		
		/* 得到Server部分的长度 */
		Pattern p = Pattern.compile("filename([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String filenameLen = null;
		while(m.find()) {
			filenameLen = m.group(1);
		}
		
		/* 得到从http开始到server描述结束 */
		String filename = null;
		p = Pattern.compile("filename" + filenameLen + ":" +"(.{" + filenameLen + "})");
		m = p.matcher(btOneItem);
		while(m.find()) {
			filename = m.group(1);
		}
		return filename;
	}
	
	/**
	 * 解析出文件服务器端URL信息
	 * @param btOneItem
	 * @return 服务器的URL（IP）
	 */
	private String getServerURL(String btOneItem) {
		
		/* 得到Server部分的长度 */
		Pattern p = Pattern.compile("server([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String serverLen = null;
		while(m.find()) {
			serverLen = m.group(1);
		}
		
		/* 得到从http开始到server描述结束 */
		String serverDes = null;
		p = Pattern.compile("server" + serverLen + ":" +"(.{" + serverLen + "})");
		m = p.matcher(btOneItem);
		while(m.find()) {
			serverDes = m.group(1);
		}
		
		serverDes = serverDes.replace("http://", "");
		String[] strs = serverDes.split(":");
		String serverURL = strs[0];
		return serverURL;
	}
	
	/**
	 * 解析得到端口号
	 * @param btOneItem
	 * @return 服务器端得端口号
	 */
	private int getPort(String btOneItem) {
		
		/* 得到Server部分的长度 */
		Pattern p = Pattern.compile("server([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String serverLen = null;
		while(m.find()) {
			serverLen = m.group(1);
		}
		
		/* 得到从http开始到server描述结束 */
		String serverDes = null;
		p = Pattern.compile("server" + serverLen + ":" +"(.{" + serverLen + "})");
		m = p.matcher(btOneItem);
		while(m.find()) {
			serverDes = m.group(1);
		}
		
		serverDes = serverDes.replace("http://", "");
		String[] strs = serverDes.split(":");
		int port = Integer.parseInt(strs[1]);
		return port;
	}
	
}

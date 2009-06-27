package pro.network.btTransfer;
/*
 * ����Bt�ļ����õ�������URL���˿ںţ�Ҫ���ص��ļ�·�����ļ���
 * ���ߣ����ǽ�
 * ���ڣ�2009-05-30
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

	private String btFilePath = null;               //bt�ļ���·��
	
	private ArrayList<String> btFileContent;        //bt�ļ�������
	
	/**
	 * ���캯��
	 * @param btFilePath
	 */
	public BtParser(String btFilePath) {
		this.btFilePath = btFilePath;
		btFileContent = new ArrayList<String>();
		getBtFileContent();
	}
	
	/**
	 * �õ������ļ�����Ϣ
	 * @return ���������ļ���Ϣ��FileInfo�����ļ���
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
     * ��btFilePath�ж�ȡBt�ļ�������
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
	 * �������ļ�·����Ϣ
	 * @param btOneItem
	 * @return �ļ���·��
	 */
	private String getPath(String btOneItem) {
		
		/* �õ�Server���ֵĳ��� */
		Pattern p = Pattern.compile("path([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String pathLen = null;
		while(m.find()) {
			pathLen = m.group(1);
		}
		
		/* �õ���http��ʼ��server�������� */
		String path = null;
		p = Pattern.compile("path" + pathLen + ":" +"(.{" + pathLen + "})");
		m = p.matcher(btOneItem);
		while(m.find()) {
			path = m.group(1);
		}
		return path;
	}
	
	/**
	 * �������ļ�����Ϣ
	 * @param btOneItem
	 * @return �ļ���
	 */
	private String getFileName(String btOneItem) {
		
		/* �õ�Server���ֵĳ��� */
		Pattern p = Pattern.compile("filename([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String filenameLen = null;
		while(m.find()) {
			filenameLen = m.group(1);
		}
		
		/* �õ���http��ʼ��server�������� */
		String filename = null;
		p = Pattern.compile("filename" + filenameLen + ":" +"(.{" + filenameLen + "})");
		m = p.matcher(btOneItem);
		while(m.find()) {
			filename = m.group(1);
		}
		return filename;
	}
	
	/**
	 * �������ļ���������URL��Ϣ
	 * @param btOneItem
	 * @return ��������URL��IP��
	 */
	private String getServerURL(String btOneItem) {
		
		/* �õ�Server���ֵĳ��� */
		Pattern p = Pattern.compile("server([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String serverLen = null;
		while(m.find()) {
			serverLen = m.group(1);
		}
		
		/* �õ���http��ʼ��server�������� */
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
	 * �����õ��˿ں�
	 * @param btOneItem
	 * @return �������˵ö˿ں�
	 */
	private int getPort(String btOneItem) {
		
		/* �õ�Server���ֵĳ��� */
		Pattern p = Pattern.compile("server([0-9]+):");
		Matcher m = p.matcher(btOneItem);
		String serverLen = null;
		while(m.find()) {
			serverLen = m.group(1);
		}
		
		/* �õ���http��ʼ��server�������� */
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

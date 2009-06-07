package pro.network.btTransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
/**
 * ʵ���ļ���ƴ�ӣ��Ѽ����ļ�ƴ�ӳ�һ��
 * @author Yajie Miao
 *
 */
public class Combine {

	private ArrayList<String> list;           //Դ�ļ�����List
	private String des;                       //ƴ�Ӻ��Ŀ���ļ���
	
	
	public Combine(ArrayList<String> list, String des) {
		this.list = list;
		this.des = des;
	}
	
	public void combineFile() {
		try{
			DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(des))));
			for (int i = 0; i < list.size(); i++) {
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				
				DataInputStream dataIn = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(list.get(i)))));
				
				while(true) {
					
					int read = dataIn.read(buffer);
					if(read == -1) break;
					dataOut.write(buffer, 0, read);
				}
				dataOut.flush();
				dataIn.close();
			}
			dataOut.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


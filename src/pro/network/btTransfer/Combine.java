package pro.network.btTransfer;
/*
 * 实现文件的拼接：把几个文件拼接成一个
 * @author 李腾飞
 *
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class Combine {

	private ArrayList<String> list;           //源文件名的List
	private String des;                       //拼接后的目标文件名
	
	
    /**
     * 构造函数
     * @param list
     * @param des
     */
	public Combine(ArrayList<String> list, String des) {
		this.list = list;
		this.des = des;
	}
	
	/**
	 * 合并文件
	 */
	public void combineFile() {
		try{
			DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(des))));
			
			/* 对于每个文件片段，读取其数据，写入到完整的文件中 */
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


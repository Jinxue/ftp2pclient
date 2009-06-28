package FTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MergeFile extends Thread {

    private int threadNumber;
    private String fileName;
    private String downLoadPath;
    private FTPClient ftpClient;
    
    public MergeFile(int threadNumber, String downLoadPath, String fileName, FTPClient ftp) {
        super();
        this.threadNumber = threadNumber;
        this.downLoadPath = downLoadPath;
        this.fileName = fileName;
        this.ftpClient = ftp;
    }

    public void run() {
    	synchronized (ftpClient){
    		while(!(ftpClient.SIGNAL == 0 && ftpClient.START_DOWNLOAD)) {
    			try {
    				ftpClient.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
		long begin = System.nanoTime();
    	
        try {
            System.out.println("文件开始合并");
            OutputStream bos = new FileOutputStream(new File(downLoadPath, fileName), false);
            for (int i = 0; i<threadNumber; i++)
            {
                File file = new File(downLoadPath, fileName + ".part" + i);
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                is.close();
                file.delete();
            }
            bos.close();
            System.out.println("文件合并完成");
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
   
		double temp = (System.nanoTime() - begin) / 1000000000.0 ;
		System.out.println("********** File name: " + fileName + " Used time: " + temp + " *************");

        synchronized (ftpClient){
        	ftpClient.START_DOWNLOAD = false;	
        }
        
        ftpClient.mergeDone();
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownLoadPath() {
        return downLoadPath;
    }

    public void setDownLoadPath(String downLoadPath) {
        this.downLoadPath = downLoadPath;
    }
}

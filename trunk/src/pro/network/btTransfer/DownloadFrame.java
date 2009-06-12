package pro.network.btTransfer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.util.ArrayList;

public class DownloadFrame extends JFrame {

	
	private ArrayList<FileInfo> btItems;
	
	/* 界面上的组件 */
	private JPanel panel;
	private JPanel panelState;
	private JPanel panelLog;
	private JButton openFile;
	private JButton downloadFile;
	private JButton buttonCombine;
	private JFileChooser fileChooser;
	private JScrollPane scrollPaneTable;
	private JScrollPane scrollPaneLog;
	private JTextArea textLog;
	private JTable table;
	
	private ArrayList<String> list = new ArrayList<String>();
	private String des;
	
	DefaultTableModel dfModel = new DefaultTableModel();
	
	
	/* 初始化 */
	public DownloadFrame() {
		btItems = new ArrayList<FileInfo>();
		
		this.setSize(796, 1000);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panel = new JPanel();
		panelState = new JPanel();
		panelLog = new JPanel();
		openFile = new JButton("打开文件");
		downloadFile = new JButton("下载文件");
		buttonCombine = new JButton("合并文件");
		fileChooser = new JFileChooser();
		table = new JTable();
		textLog = new JTextArea();
		scrollPaneTable = new JScrollPane(table);
		scrollPaneLog = new JScrollPane(textLog);
		
		/* 初始化表格 */
		dfModel.setColumnCount(4);
		dfModel.setRowCount(15);
		String[] titles = {"服务器", "状态", "数据大小", "已下载"};
		dfModel.setColumnIdentifiers(titles);
		table.setModel(dfModel);
				
		openFile.addActionListener(new FileOpenListener());
		
		downloadFile.addActionListener(new FileDownLoadListener());
		
		buttonCombine.addActionListener(new CombineListener());
		
		layOut();
	}
	
    /* 布局界面的函数 */
	private void layOut() {
		org.jdesktop.layout.GroupLayout panelLayout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLayout.createSequentialGroup()
                .add(75, 75, 75)
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(buttonCombine)
                    .add(downloadFile)
                    .add(openFile))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(openFile)
                .add(48, 48, 48)
                .add(downloadFile)
                .add(46, 46, 46)
                .add(buttonCombine)
                .addContainerGap(96, Short.MAX_VALUE))
        );
        org.jdesktop.layout.GroupLayout panelStateLayout = new org.jdesktop.layout.GroupLayout(panelState);
        panelState.setLayout(panelStateLayout);
        panelStateLayout.setHorizontalGroup(
            panelStateLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelStateLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneTable, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelStateLayout.setVerticalGroup(
            panelStateLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelStateLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneTable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 249, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        org.jdesktop.layout.GroupLayout panelLogLayout = new org.jdesktop.layout.GroupLayout(panelLog);
        panelLog.setLayout(panelLogLayout);
        panelLogLayout.setHorizontalGroup(
            panelLogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLogLayout.createSequentialGroup()
                .add(78, 78, 78)
                .add(scrollPaneLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelLogLayout.setVerticalGroup(
            panelLogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLogLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelState, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(52, 52, 52)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(panelState, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(panelLog, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
 
        pack();
	}

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {                          
//        System.exit(0);
    	dispose();
    }                         
	
	/**
	 * "打开文件"按钮上的操作：解析Bt文件
	 */
	private class FileOpenListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			fileChooser.setCurrentDirectory(new File("."));
			int result = fileChooser.showOpenDialog(DownloadFrame.this);
			String btFilePath = null;
			if (result == JFileChooser.APPROVE_OPTION) {
				btFilePath = fileChooser.getSelectedFile().getPath();
			}
			
			BtParser btParser = new BtParser(btFilePath);
			btItems = btParser.getFileInfo();
			
			/* 把解析得到的服务器名称显示在界面上 */
            for (int i = 0; i < btItems.size(); i++) {
            	dfModel.setValueAt(btItems.get(i).getServerURL(), i, 0);
            }
			System.out.println("Bt文件解析完毕");
            textLog.append("Bt文件解析成功\n");
		}
	}
	
	/**
     *"下载文件"按钮上的操作
	 */
	private class FileDownLoadListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setSelectedFile(new File(btItems.get(0).getFilename()));
			int result = fileChooser.showSaveDialog(DownloadFrame.this);
			String saveFilePath = null;
			if (result == JFileChooser.APPROVE_OPTION) {
				saveFilePath = fileChooser.getSelectedFile().getPath();
			}
			
			/* 拆分文件路径，以保存得到的文件片段 */
			String str1 = (saveFilePath.split("\\."))[0];
			String str2 = (saveFilePath.split("\\."))[1];
			 
			des = saveFilePath;
			
			for (int i = 0; i < btItems.size(); i++) {
				DownloadFile downloadFile = new DownloadFile(btItems.get(i), str1 + (i + 1)  + "." + str2, btItems.size());
				list.add(str1 + (i + 1) + "." + str2);
				downloadFile.start();
			}
			
		}
	}	
	
	/**
     *"合并文件"按钮上的操作
	 */
	private class CombineListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			textLog.append("正在合并文件...\n");
			Combine combine = new Combine(list, des);
			combine.combineFile();
			textLog.append("合并文件成功\n");
			
		}
	}	
	
	private class DownloadFile extends Thread {

		private FileInfo fileInfo;
		
		private String saveFilePath;
		
		Socket s;
		
		private int serverNum;                 //服务器的个数
		
		public DownloadFile(FileInfo fileInfo, String saveFilePath, int serverNum) {
			this.fileInfo = fileInfo;
			this.saveFilePath = saveFilePath;
			this.serverNum = serverNum;
		}
		
		public void run() {
			if(connect() == false) {           //连接失败
				dfModel.setValueAt("连接失败", fileInfo.getId()-1, 1);
				textLog.append("连接服务器" + fileInfo.getServerURL() + "失败\n");
				return;
			}
			dfModel.setValueAt("已连接", fileInfo.getId()-1, 1);
			textLog.append("连接服务器" + fileInfo.getServerURL() + "成功\n");
			
			try {
				
				dfModel.setValueAt("准备下载", fileInfo.getId()-1, 1);
				textLog.append("准备从服务器" + fileInfo.getServerURL() + "下载\n");
				
				DataInputStream dataIn = new DataInputStream(s.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
				
				/* 定义把文件数据保存到本地的输出流 */
				DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(saveFilePath))));
				
				
				/* 向服务器端发送文件路径和文件名 */
				dataOut.writeUTF(fileInfo.getPath());
//				dataOut.flush();
				dataOut.writeUTF(fileInfo.getFilename());
//				dataOut.flush();
										
				/* 接收文件大小*/
				long fileLen = 0;
				fileLen = dataIn.readLong();
				
				/* 计算偏移量和长度 */
				long everySeg = fileLen / serverNum;
				long left = fileLen % serverNum;
			    long off = (fileInfo.getId() - 1) * everySeg;
			    long segLen = 0;
			    if(fileInfo.getId() == serverNum) {
			    	segLen = everySeg + left;
			    } else {
			    	segLen = everySeg;
			    }
			    
			    /* 把下载的长度显示在界面上 */
			    Double segLenM = ((double) segLen) / (1024 * 1024);
			    dfModel.setValueAt(segLenM + "m", fileInfo.getId()-1, 2);
				textLog.append("从服务器" + fileInfo.getServerURL() + "下载" + segLenM + "m数据\n");
				
			    /* 向服务器发送文件数据的偏移量和长度 */
				dataOut.writeLong(off);
//				dataOut.flush();
				dataOut.writeLong(segLen);
//				dataOut.flush();
				
				int bufferSize = 1024;
				byte[] buffer =  new byte[bufferSize];
				
				int passedLen = 0;             //已经接受到文件的大小
				
				dfModel.setValueAt("开始下载", fileInfo.getId()-1, 1);
				textLog.append("开始从服务器" + fileInfo.getServerURL() + "下载数据\n");
				
				while (true) {
					int read = 0;
					if (dataIn != null) {
						read = dataIn.read(buffer);
					}
					
//					if(read == -1) {
//						break;
//					}
					passedLen += read;
					
					Double rcvPercentage = ((double) passedLen / segLen) * 100;
					
					dfModel.setValueAt(rcvPercentage + "%", fileInfo.getId()-1, 3);

//					System.out.println("文件接收了" + rcvPercentage + "%");
					
					fileOut.write(buffer, 0, read);
					if(passedLen == segLen) {
						break;
					}
				}
				
				dfModel.setValueAt("下载完成", fileInfo.getId()-1, 1);
				textLog.append("从服务器" + fileInfo.getServerURL() + "下载完成\n");
				
				System.out.println("文件片段" + fileInfo.getId() + "下载成功");
				
				dataIn.close();
				dataOut.close();
				fileOut.close();
				
			} catch(IOException e) {
				//e.printStackTrace();
				dfModel.setValueAt("下载失败", fileInfo.getId()-1, 1);
				textLog.append("从服务器" + fileInfo.getServerURL() + "下载失败\n");
				System.out.println("文件片段" + fileInfo.getId() + "下载出错");
				return;
			}
			
		}
		
		/**
		 * 建立到服务器端的连接
		 * @return 返回是否连接成功建立的布尔值
		 */
		private boolean connect() {
			
			dfModel.setValueAt("连接服务器", fileInfo.getId()-1, 1);
			textLog.append("正在连接服务器" + fileInfo.getServerURL() + "\n");
			
			try {
				s = new Socket(fileInfo.getServerURL(), fileInfo.getPort());
//				System.out.println("连接服务器" + fileInfo.getId() + "成功");
				return true;
			} catch (Exception e) {
//				System.out.println("连接服务器" + fileInfo.getId() + "失败");
				return false;
			}
		}		
	}

	
	public static void main(String args[]) {
		DownloadFrame downloadFrame = new DownloadFrame();
		downloadFrame.setVisible(true);
	}
	
}

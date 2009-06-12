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
	
	/* �����ϵ���� */
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
	
	
	/* ��ʼ�� */
	public DownloadFrame() {
		btItems = new ArrayList<FileInfo>();
		
		this.setSize(796, 1000);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panel = new JPanel();
		panelState = new JPanel();
		panelLog = new JPanel();
		openFile = new JButton("���ļ�");
		downloadFile = new JButton("�����ļ�");
		buttonCombine = new JButton("�ϲ��ļ�");
		fileChooser = new JFileChooser();
		table = new JTable();
		textLog = new JTextArea();
		scrollPaneTable = new JScrollPane(table);
		scrollPaneLog = new JScrollPane(textLog);
		
		/* ��ʼ����� */
		dfModel.setColumnCount(4);
		dfModel.setRowCount(15);
		String[] titles = {"������", "״̬", "���ݴ�С", "������"};
		dfModel.setColumnIdentifiers(titles);
		table.setModel(dfModel);
				
		openFile.addActionListener(new FileOpenListener());
		
		downloadFile.addActionListener(new FileDownLoadListener());
		
		buttonCombine.addActionListener(new CombineListener());
		
		layOut();
	}
	
    /* ���ֽ���ĺ��� */
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
	 * "���ļ�"��ť�ϵĲ���������Bt�ļ�
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
			
			/* �ѽ����õ��ķ�����������ʾ�ڽ����� */
            for (int i = 0; i < btItems.size(); i++) {
            	dfModel.setValueAt(btItems.get(i).getServerURL(), i, 0);
            }
			System.out.println("Bt�ļ��������");
            textLog.append("Bt�ļ������ɹ�\n");
		}
	}
	
	/**
     *"�����ļ�"��ť�ϵĲ���
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
			
			/* ����ļ�·�����Ա���õ����ļ�Ƭ�� */
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
     *"�ϲ��ļ�"��ť�ϵĲ���
	 */
	private class CombineListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			textLog.append("���ںϲ��ļ�...\n");
			Combine combine = new Combine(list, des);
			combine.combineFile();
			textLog.append("�ϲ��ļ��ɹ�\n");
			
		}
	}	
	
	private class DownloadFile extends Thread {

		private FileInfo fileInfo;
		
		private String saveFilePath;
		
		Socket s;
		
		private int serverNum;                 //�������ĸ���
		
		public DownloadFile(FileInfo fileInfo, String saveFilePath, int serverNum) {
			this.fileInfo = fileInfo;
			this.saveFilePath = saveFilePath;
			this.serverNum = serverNum;
		}
		
		public void run() {
			if(connect() == false) {           //����ʧ��
				dfModel.setValueAt("����ʧ��", fileInfo.getId()-1, 1);
				textLog.append("���ӷ�����" + fileInfo.getServerURL() + "ʧ��\n");
				return;
			}
			dfModel.setValueAt("������", fileInfo.getId()-1, 1);
			textLog.append("���ӷ�����" + fileInfo.getServerURL() + "�ɹ�\n");
			
			try {
				
				dfModel.setValueAt("׼������", fileInfo.getId()-1, 1);
				textLog.append("׼���ӷ�����" + fileInfo.getServerURL() + "����\n");
				
				DataInputStream dataIn = new DataInputStream(s.getInputStream());
				DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
				
				/* ������ļ����ݱ��浽���ص������ */
				DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(saveFilePath))));
				
				
				/* ��������˷����ļ�·�����ļ��� */
				dataOut.writeUTF(fileInfo.getPath());
//				dataOut.flush();
				dataOut.writeUTF(fileInfo.getFilename());
//				dataOut.flush();
										
				/* �����ļ���С*/
				long fileLen = 0;
				fileLen = dataIn.readLong();
				
				/* ����ƫ�����ͳ��� */
				long everySeg = fileLen / serverNum;
				long left = fileLen % serverNum;
			    long off = (fileInfo.getId() - 1) * everySeg;
			    long segLen = 0;
			    if(fileInfo.getId() == serverNum) {
			    	segLen = everySeg + left;
			    } else {
			    	segLen = everySeg;
			    }
			    
			    /* �����صĳ�����ʾ�ڽ����� */
			    Double segLenM = ((double) segLen) / (1024 * 1024);
			    dfModel.setValueAt(segLenM + "m", fileInfo.getId()-1, 2);
				textLog.append("�ӷ�����" + fileInfo.getServerURL() + "����" + segLenM + "m����\n");
				
			    /* ������������ļ����ݵ�ƫ�����ͳ��� */
				dataOut.writeLong(off);
//				dataOut.flush();
				dataOut.writeLong(segLen);
//				dataOut.flush();
				
				int bufferSize = 1024;
				byte[] buffer =  new byte[bufferSize];
				
				int passedLen = 0;             //�Ѿ����ܵ��ļ��Ĵ�С
				
				dfModel.setValueAt("��ʼ����", fileInfo.getId()-1, 1);
				textLog.append("��ʼ�ӷ�����" + fileInfo.getServerURL() + "��������\n");
				
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

//					System.out.println("�ļ�������" + rcvPercentage + "%");
					
					fileOut.write(buffer, 0, read);
					if(passedLen == segLen) {
						break;
					}
				}
				
				dfModel.setValueAt("�������", fileInfo.getId()-1, 1);
				textLog.append("�ӷ�����" + fileInfo.getServerURL() + "�������\n");
				
				System.out.println("�ļ�Ƭ��" + fileInfo.getId() + "���سɹ�");
				
				dataIn.close();
				dataOut.close();
				fileOut.close();
				
			} catch(IOException e) {
				//e.printStackTrace();
				dfModel.setValueAt("����ʧ��", fileInfo.getId()-1, 1);
				textLog.append("�ӷ�����" + fileInfo.getServerURL() + "����ʧ��\n");
				System.out.println("�ļ�Ƭ��" + fileInfo.getId() + "���س���");
				return;
			}
			
		}
		
		/**
		 * �������������˵�����
		 * @return �����Ƿ����ӳɹ������Ĳ���ֵ
		 */
		private boolean connect() {
			
			dfModel.setValueAt("���ӷ�����", fileInfo.getId()-1, 1);
			textLog.append("�������ӷ�����" + fileInfo.getServerURL() + "\n");
			
			try {
				s = new Socket(fileInfo.getServerURL(), fileInfo.getPort());
//				System.out.println("���ӷ�����" + fileInfo.getId() + "�ɹ�");
				return true;
			} catch (Exception e) {
//				System.out.println("���ӷ�����" + fileInfo.getId() + "ʧ��");
				return false;
			}
		}		
	}

	
	public static void main(String args[]) {
		DownloadFrame downloadFrame = new DownloadFrame();
		downloadFrame.setVisible(true);
	}
	
}

package FTP;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class RemoteFilePanel extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox jcbPath;
	// We use a Hash Set to store the visited path
	private HashSet<String> pathSet;

	private JTable jtFile;
	private DefaultTableModel dtmFile;
	private String currentPath = new String();
//	private int currentIndex;
	private boolean init = false;
	private FTPClient ftp;

	private JPopupMenu popupMenu;
	private JTextArea outArea;

	private int selectRowIndex = -1;
	
	private LocalFilePanel local;
	
	public RemoteFilePanel(FTPClient ftp) {
		super(new BorderLayout());

		this.ftp = ftp;

		JPanel jp = new JPanel(new BorderLayout());
		// Add the combined box for path selection
		jcbPath = new JComboBox();
//		jcbPath.addActionListener(this);
		jcbPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				if (e.getSource())
				if (init == false) {
					return;
				}
				String item = (String) jcbPath.getSelectedItem();
				try {
					listFiles(item);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			});
		jp.add(jcbPath, "North");
		pathSet = new HashSet<String>();

		// Add the file list table
		dtmFile = new LocalTableModel();
		dtmFile.addColumn("����");
		dtmFile.addColumn("��С");
		dtmFile.addColumn("�޸�����");
		dtmFile.addColumn("����");
		jtFile = new JTable(dtmFile);
		jtFile.setShowGrid(false);
		jtFile.addMouseListener(this);
		createPopMenuForTable();

		// Add the FTP command output
		outArea = new JTextArea();
		outArea.setColumns(20);
		outArea.setRows(5);

		// Finally, add the three panel to the whole frame
		add(jp, "North");
		add(new JScrollPane(jtFile), "Center");
		add(new JScrollPane(outArea), "South");
		
		// �ض���ͨ���ı�������������������С�
		System.setOut(new GUIPrintStream(System.out, outArea));
	}
//	private String newFilename;
	// Create the Pop up menu for FTP operation
	private void createPopMenuForTable() {
		// TODO Auto-generated method stub
		popupMenu = new JPopupMenu();

		JMenuItem downloadItem = new JMenuItem("����");
		downloadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = jtFile.getValueAt(selectRowIndex, 0).toString();
				if(jtFile.getValueAt(selectRowIndex, 3).toString().startsWith("-")){
					try {
						ftp.download(filename);
						listFiles(currentPath);
						local.ListFiles();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		JMenuItem deleteItem = new JMenuItem("ɾ��");
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = jtFile.getValueAt(selectRowIndex, 0)
						.toString();
				try {
					if (jtFile.getValueAt(selectRowIndex, 3).toString()
							.startsWith("-")) {
						ftp.delete(filename);

					} else if (jtFile.getValueAt(selectRowIndex, 3).toString()
							.startsWith("d")) {
						ftp.rmDirectory(filename);
					}
					listFiles(currentPath);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		class NewNameDialog extends JDialog{
			private static final long serialVersionUID = 1L;
			private String newName;
			public NewNameDialog(JFrame parent, String title, String label){
//				super();
				super(parent, title, true);
				
//				Container cp = getContentPane();
//				setPreferredSize(new Dimension(150, 100));
				setSize(200, 80);
				Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
				setLocation((int) (di.getWidth() - getWidth()) / 2, (int) (di
						.getHeight() - getHeight()) / 2);
				setLayout(new BorderLayout());
				add(new JLabel(label), "North");
				final JTextField text = new JTextField();
				add(text, "Center");
				JButton ok = new JButton("OK");
				ok.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						newName = text.getText();
						dispose();
					}
				});
				add(ok, "East");
//				setsize(150,50);
			}
			public String getNewName(){
				return newName;
			}
		}
		
		JMenuItem renameItem = new JMenuItem("������");
		renameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = jtFile.getValueAt(selectRowIndex, 0).toString();
//				if(jtFile.getValueAt(selectRowIndex, 3).toString().startsWith("-")){
				NewNameDialog dialog = new NewNameDialog(null,"�������ļ�","���ļ����ƣ�");
					dialog.setVisible(true);
				try {
					String newName = dialog.getNewName();
					if((newName != null) && !newName.isEmpty()){
						ftp.rename(filename, newName);
						listFiles(currentPath);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
//			}
		});

		JMenuItem mkdItem = new JMenuItem("����Ŀ¼");
		mkdItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String filename = jtFile.getValueAt(selectRowIndex, 0).toString();
//				if(jtFile.getValueAt(selectRowIndex, 3).toString().startsWith("-")){
				NewNameDialog dialog1 = new NewNameDialog(null,"����Ŀ¼", "Ŀ¼����");
				dialog1.setVisible(true);
				try {
					String newName = dialog1.getNewName();
					if((newName != null) && !newName.isEmpty()){
						ftp.mkDirectory(dialog1.getNewName());
						listFiles(currentPath);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
//		popupMenu.addSeparator();
		popupMenu.add(downloadItem);
		popupMenu.add(deleteItem);
		popupMenu.add(renameItem);
		popupMenu.add(mkdItem);
//		jtFile2.add(popupMenu);
	}

	// ����·����ѡ���¼�
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		// TODO Auto-generated method stub
//		if (init == false) {
//			return;
//		}
//		String item = (String) jcbPath.getSelectedItem();
//		try {
//			listFiles(item);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}

	// JTable���ļ���˫���¼�
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getClickCount() == 2) {
			int row = ((JTable) e.getSource()).getSelectedRow();
			String attr = ((JTable) e.getSource()).getValueAt(row, 3)
					.toString();
			if (attr.startsWith("d")) {
				try {
					listFiles(((JTable) e.getSource()).getValueAt(row, 0)
							.toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (((JTable) e.getSource()).getValueAt(row, 0).toString()
					.equals("..")
					&& ((JTable) e.getSource()).getValueAt(row, 2).toString()
							.equals("")) {
				try {
					ftp.cdup();
					// return to the parent
					int end = currentPath.lastIndexOf('/',
							currentPath.length() - 2) + 1;
					currentPath = currentPath.substring(0, end);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					listFiles("./");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger() && jtFile.isEnabled()) {
			Point p = new Point(e.getX(), e.getY());
//			int col = jtFile.columnAtPoint(p);
			int row = jtFile.rowAtPoint(p);
			
			selectRowIndex = row;

			// translate table index to model index
//			int mcol = jtFile.getColumn(jtFile.getColumnName(col))
//					.getModelIndex();

			if (row >= 0 && row < jtFile.getRowCount()) {
//				cancelCellEditing();

				// Create the Popup menu
//				createPopMenuForTable();
				
				// ... and show it
				if (popupMenu != null && popupMenu.getComponentCount() > 0) {
					popupMenu.show(jtFile, p.x, p.y);
				}
			}
		}
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		maybeShowPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		maybeShowPopup(e);
	}
	
	public boolean listFiles() throws IOException{
		return listFiles(currentPath);
	}

	// The first time to list the files after the FTP is connected
	public boolean initListFiles(String filename) throws IOException {
		listFiles(filename);
		init = true;
		return true;
	}

	private boolean listFiles(String path) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> fileList = new ArrayList<String>();
//		ftp.list(path);
		if (ftp.list(path, fileList)) {
			if (!path.equals("./")) {
				ftp.cwd(path);
				if(path.startsWith("/"))
					currentPath = path; // Now in root directory or the absolute directory
				else
					currentPath += path + "/";
			}
			if (!pathSet.contains(currentPath)) {
				jcbPath.addItem(currentPath);
				pathSet.add(currentPath);
			}
			jcbPath.setSelectedItem(currentPath);

			// �����������
			dtmFile.setRowCount(0);

			// ����ж�Ϊ�Ƿ�����Ŀ¼������� �����ϼ� һ��
			if (!currentPath.equals("/")) {
				dtmFile.addRow(new String[] { "..", "", "", "" });
			}

			// �г���ǰĿ¼����Ŀ¼���ļ�
			// The format return is as following:
			// -rw------- 1 14 50 185339904 May 28 01:20 Fedora-8-i386-DVD.iso

			for (int i = 0; i < fileList.size(); i++) {
				StringTokenizer tokenizer = new StringTokenizer(fileList.get(i));
				String attr = tokenizer.nextToken();
				// Note the "other" is necessary because of the token analyzer
				String other = tokenizer.nextToken() + tokenizer.nextToken()
						+ tokenizer.nextToken();
				String size = sizeFormat(Long.parseLong(tokenizer.nextToken()));
				String time = tokenizer.nextToken() + " "
						+ tokenizer.nextToken() + " " + tokenizer.nextToken();
				String name = tokenizer.nextToken();
				// We must to handle the file name contained space
				while(tokenizer.hasMoreTokens())
					name += " " + tokenizer.nextToken();

				dtmFile.addRow(new String[] { name, size, time, attr });
			}
			return true;
		}
		return false;
	}
	
	public void setLocalFileList(LocalFilePanel aLocal){
		local = aLocal;
	}

	// ���ļ���Сת������Ӧ�ַ�����ʽ
	private String sizeFormat(long length) {
		long kb;
		if (length < 1024) {
			return String.valueOf(length);
		} else if ((kb = length / 1024) < 1024) {
			return (String.valueOf(kb) + "KB");
		} else {
			return (String.valueOf(length / 1024 / 1024) + "MB");
		}
	}

	// ����
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame jf = new JFrame("����");
		jf.setSize(300, 400);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
		jf.setLocation((int) (di.getWidth() - jf.getWidth()) / 2, (int) (di
				.getHeight() - jf.getHeight()) / 2);
		FTPClient aFtp = new FTPClient();
		RemoteFilePanel remote = new RemoteFilePanel(aFtp);
		jf.add(remote);
		jf.setVisible(true);
//		Thread.sleep(1000);
		aFtp.setLocalWD("E:\\");
		aFtp.connect("166.111.80.101", "ftptest", "ftptest");
		remote.initListFiles("/");
	}

	// ʵ����Ӧ��tablemodel��
	class LocalTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
}

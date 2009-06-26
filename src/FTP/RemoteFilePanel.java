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
		dtmFile.addColumn("名称");
		dtmFile.addColumn("大小");
		dtmFile.addColumn("修改日期");
		dtmFile.addColumn("属性");
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
		
		// 重定向到通过文本组件构建的组件输出流中。
		System.setOut(new GUIPrintStream(System.out, outArea));
	}
//	private String newFilename;
	// Create the Pop up menu for FTP operation
	private void createPopMenuForTable() {
		// TODO Auto-generated method stub
		popupMenu = new JPopupMenu();

		JMenuItem downloadItem = new JMenuItem("下载");
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
		
		JMenuItem deleteItem = new JMenuItem("删除");
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
		
		JMenuItem renameItem = new JMenuItem("重命名");
		renameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = jtFile.getValueAt(selectRowIndex, 0).toString();
//				if(jtFile.getValueAt(selectRowIndex, 3).toString().startsWith("-")){
				NewNameDialog dialog = new NewNameDialog(null,"重命名文件","新文件名称：");
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

		JMenuItem mkdItem = new JMenuItem("创建目录");
		mkdItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String filename = jtFile.getValueAt(selectRowIndex, 0).toString();
//				if(jtFile.getValueAt(selectRowIndex, 3).toString().startsWith("-")){
				NewNameDialog dialog1 = new NewNameDialog(null,"创建目录", "目录名称");
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

	// 处理路径的选择事件
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

	// JTable里文件夹双击事件
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

			// 清空现有数据
			dtmFile.setRowCount(0);

			// 如果判断为非分区根目录，则添加 返回上级 一行
			if (!currentPath.equals("/")) {
				dtmFile.addRow(new String[] { "..", "", "", "" });
			}

			// 列出当前目录所有目录及文件
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

	// 将文件大小转换成相应字符串格式
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

	// 测试
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame jf = new JFrame("测试");
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

	// 实现相应的tablemodel类
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

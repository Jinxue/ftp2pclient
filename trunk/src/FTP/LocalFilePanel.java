package FTP;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class LocalFilePanel extends JPanel implements ActionListener,
		MouseListener {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private JComboBox jcbPath;
    private JTable jtFile;
    private DefaultTableModel dtmFile;
    private JLabel jlLocal;
    private File path;
    private String currentPath;
    private int currentIndex;
    private boolean init = false;
    
	private FTPClient ftp;
	private JPopupMenu popupMenu;
	private int selectRowIndex = -1;
	
	private RemoteFilePanel remote;

    public LocalFilePanel(FTPClient ftp2) {
        super(new BorderLayout());
        
        ftp = ftp2;
        
        JPanel jp = new JPanel(new BorderLayout());
        jcbPath = new JComboBox();
        jcbPath.addActionListener(this);
        jp.add(jcbPath, "North");
        dtmFile = new LocalTableModel();
        dtmFile.addColumn("����");
        dtmFile.addColumn("��С");
//        dtmFile.addColumn("����");
        dtmFile.addColumn("�޸�����");
        jtFile = new JTable(dtmFile);
        jtFile.setShowGrid(false);
        jtFile.addMouseListener(this);
        createPopMenuForTable();
        jlLocal = new JLabel("����״̬", JLabel.CENTER);

        add(jp, "North");
        add(new JScrollPane(jtFile), "Center");
        add(jlLocal, "South");

        //��ʾϵͳ�������ļ�·�� �� ��JTabel����ʾ��ǰ·�����ļ���Ϣ
        path = new File(System.getProperty("user.dir"));
        listFiles(path);    

        init = true;
    }

	private void createPopMenuForTable() {
		// TODO Auto-generated method stub
		popupMenu = new JPopupMenu();

		JMenuItem uploadItem = new JMenuItem("�ϴ�");
		uploadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = jtFile.getValueAt(selectRowIndex, 0).toString();
				File file = new File(currentPath, jtFile.getValueAt(selectRowIndex, 0).toString());
				if(!file.isDirectory()){
					try {
						File aFile = new File(currentPath, filename);
						ftp.stor(aFile);
						remote.listFiles();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		popupMenu.add(uploadItem);
	}
    
    //����·����ѡ���¼�
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        if (init == false)
        {
            return;
        }
        int index = jcbPath.getSelectedIndex();
        String item = (String)jcbPath.getSelectedItem();
        if (item.startsWith("  "))
        {
            int root = index - 1;
            while (((String)jcbPath.getItemAt(root)).startsWith("  "))
            {
                root--;
            }
            String path = (String)jcbPath.getItemAt(root);
            while (root < index)
            {
                path += ((String)jcbPath.getItemAt(++root)).trim();;
                path += "\\";
            }
            if (listFiles(new File(path)) == false)
            {
                jcbPath.setSelectedIndex(currentIndex);
            }
            else
            {
                currentIndex = index;
            }
        }
        else
        {
            if (listFiles(new File(item)) == false)
            {
                jcbPath.setSelectedIndex(currentIndex);
            }
            else
            {
                currentIndex = index;
            }
        }
    }

    //JTable���ļ���˫���¼�
    @Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
        if(e.getClickCount()==2) {
            int row = ((JTable)e.getSource()).getSelectedRow();
            String fileName = ((JTable)e.getSource()).getValueAt(row, 0).toString();
            if ((fileName.equals("�����ϼ�"))
                    && ((JTable)e.getSource()).getValueAt(row, 2).toString().equals(""))
            {
                listFiles(new File(currentPath).getParentFile());
            }
            else if ((new File(currentPath, fileName)).isDirectory())
            {
                File file;
                //�ж��Ƿ�Ϊ��Ŀ¼������ͬ����һ�� \ �Ĳ��
                if (currentPath.split("\\\\").length > 1)
                {
                    file = new File(currentPath + "\\" + ((JTable)e.getSource()).getValueAt(row, 0).toString());
                }
                else
                {                    
                    file = new File(currentPath + ((JTable)e.getSource()).getValueAt(row, 0).toString());
                }
                listFiles(file);
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
	
	public boolean ListFiles(){
		return listFiles(new File(currentPath));
	}
	
    //��ʾϵͳ�������ļ�·�� �� ��JTabel����ʾ��ǰ·�����ļ���Ϣ
    private boolean listFiles(File path) {
        String strPath = path.getAbsolutePath();
        if (path.isDirectory() == false)
        {
            JOptionPane.showMessageDialog(this, "��·�������ڣ����޴��ļ�");
            return false;
        }
        
        currentPath = path.getAbsolutePath();
        init = false;
        jcbPath.removeAllItems();
        File[] roots = File.listRoots();
        int index = 0;
        for (int i=0; i<roots.length; i++)
        {
            String rootPath = roots[i].getAbsolutePath();
            jcbPath.addItem(rootPath);
            if (currentPath.indexOf(rootPath) != -1)
            {
                String[] bufPath = currentPath.split("\\\\");
                for (int j=1; j<bufPath.length; j++)
                {
                    String buf = "  ";
                    for (int k=1; k<j; k++)
                    {
                        buf += "  ";
                    }
                    jcbPath.addItem(buf + bufPath[j]);
                    index = i + j;
                }
                if (bufPath.length == 1)
                {
                    index = i;
                }
            }
        }
        jcbPath.setSelectedIndex(index);
        init = true;
        currentIndex = index;

        //�����������
        dtmFile.setRowCount(0);

        //����ж�Ϊ�Ƿ�����Ŀ¼������� �����ϼ� һ��
        if (strPath.split("\\\\").length > 1)
        {
            dtmFile.addRow(new String[]{"�����ϼ�", "", ""});
        }

        //�г���ǰĿ¼����Ŀ¼���ļ�
        File[] files = path.listFiles();
        for (int i=0; i<files.length; i++)
        {
            String name = files[i].getName();
            if (files[i].isDirectory())
            {
//                dtmFile.addRow(new String[]{name, "", "�ļ���", ""});
            	dtmFile.addRow(new String[]{name, "", ""});
            }
            else
            {
//                if (name.lastIndexOf(".") != -1)
//                {
//                    dtmFile.addRow(new String[]{name.substring(0, name.lastIndexOf(".")),
//                    dtmFile.addRow(new String[]{name,		
//                            sizeFormat(files[i].length()), 
////                            name.substring(name.lastIndexOf(".") + 1),
//                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
//                }
//                else
//                {
                    dtmFile.addRow(new String[]{name, 
                            sizeFormat(files[i].length()), 
//                            "",
                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
//                }
            }
        }
        
        jlLocal.setText(currentPath);

        return true;
    }
    
	public void setRemoteFileList(RemoteFilePanel aRemote){
		remote = aRemote;
	}

    //���ļ���Сת������Ӧ�ַ�����ʽ
    private String sizeFormat(long length) {
        long kb;
        if (length < 1024)
        {
            return String.valueOf(length);
        }
        else if ((kb = length / 1024) < 1024)
        {
            return (String.valueOf(kb) + "KB");
        }
        else
        {
            return (String.valueOf(length / 1024 / 1024) + "MB");
        }
    }

    //����
    public static void main(String[] args) throws IOException {
        JFrame jf = new JFrame("����");
        jf.setSize(300, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation((int)(di.getWidth() - jf.getWidth()) / 2, 
                (int)(di.getHeight() - jf.getHeight()) / 2);
		FTPClient aFtp = new FTPClient();
		aFtp.connect("166.111.80.101", "ftptest", "ftptest");

        jf.add(new LocalFilePanel(aFtp));
        jf.setVisible(true);
    }

    //ʵ����Ӧ��tablemodel��
    class LocalTableModel extends DefaultTableModel
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
            return false;
        }  
    }


}

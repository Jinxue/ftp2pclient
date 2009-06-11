package FTP;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RemoteFilePanel extends JPanel implements ActionListener,
		MouseListener {

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
    private int currentIndex;
    private boolean init = false;
    private FTPClient ftp;

    public RemoteFilePanel(FTPClient ftp) {
        super(new BorderLayout());
        
        this.ftp = ftp;
        
        JPanel jp = new JPanel(new BorderLayout());
        jcbPath = new JComboBox();
        jcbPath.addActionListener(this);
        jp.add(jcbPath, "North");
        pathSet = new HashSet<String>();
        
        dtmFile = new LocalTableModel();
        dtmFile.addColumn("名称");
        dtmFile.addColumn("大小");
        dtmFile.addColumn("修改日期");
        dtmFile.addColumn("属性");
        jtFile = new JTable(dtmFile);
        jtFile.setShowGrid(false);
        jtFile.addMouseListener(this);

        add(jp, "North");
        add(new JScrollPane(jtFile), "Center");
    }

    //处理路径的选择事件
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
        if (init == false)
        {
            return;
        }
        int index = jcbPath.getSelectedIndex();
        String item = (String)jcbPath.getSelectedItem();
        {
            try {
				if (listFiles(item) == false)
				{
				    jcbPath.setSelectedIndex(currentIndex);
				}
				else
				{
				    currentIndex = index;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }

    //JTable里文件夹双击事件
    @Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
        if(e.getClickCount()==2) {
            int row = ((JTable)e.getSource()).getSelectedRow();
            String attr = ((JTable)e.getSource()).getValueAt(row, 3).toString();
            if (attr.startsWith("d"))
            {
                try {
					listFiles(((JTable)e.getSource()).getValueAt(row, 0).toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
            else if (((JTable)e.getSource()).getValueAt(row, 0).toString().equals("..")
                    && ((JTable)e.getSource()).getValueAt(row, 2).toString().equals(""))
            {
            	try {
					ftp.cdup();
					// return to the parent
					int end = currentPath.lastIndexOf('/', currentPath.length() - 2) + 1;
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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	// The first time to list the files after the FTP is connected
	public boolean initListFiles(String filename) throws IOException{
		listFiles(filename);
		init = true;
		return true;
	}
	
    private boolean listFiles(String path) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> fileList = new ArrayList<String>();
		if(ftp.list(path, fileList)){
			if(!path.equals("./")){
				ftp.cwd(path);
				if(!path.equals("/"))
					currentPath += path + "/";
				else
					currentPath = path;		// Now in root directory
			}
			init = false;
			if(!pathSet.contains(currentPath)){
				jcbPath.addItem(currentPath);
				pathSet.add(currentPath);
			}
			jcbPath.setSelectedItem(path);
			
	        //清空现有数据
	        dtmFile.setRowCount(0);

	        //如果判断为非分区根目录，则添加 返回上级 一行
	        if (!currentPath.equals("/"))
	        {
	            dtmFile.addRow(new String[]{"..", "", "", ""});
	        }

	        //列出当前目录所有目录及文件
	        // The format return is as following:
	        // -rw-------    1 14       50       185339904 May 28 01:20 Fedora-8-i386-DVD.iso
	        
	        for (int i=0; i<fileList.size(); i++)
	        {
	        	StringTokenizer tokenizer = new StringTokenizer(fileList.get(i)); 
	            String attr = tokenizer.nextToken();
	            // Note the "other" is necessary because of the token analyzer
	            String other = tokenizer.nextToken() + tokenizer.nextToken() + tokenizer.nextToken();
	            String size = sizeFormat(Long.parseLong(tokenizer.nextToken()));
	            String time = tokenizer.nextToken() + " " + 
	            				tokenizer.nextToken() + " " + tokenizer.nextToken();
	            String name = tokenizer.nextToken();
	            
	            dtmFile.addRow(new String[]{name, size, time, attr});
	        }
	        return true;
		}
		return false;
	
	}

    //将文件大小转换成相应字符串格式
    private String sizeFormat(long length) {
        long kb;
        if (length < 1024)
        {
            return String.valueOf(length);
        }
        else if ((kb = length / 1024) < 1024)
        {
            return (String.valueOf(kb) + "kb");
        }
        else
        {
            return (String.valueOf(length / 1024 / 1024) + "kb");
        }
    }

    //测试
    public static void main(String[] args) throws IOException {
        JFrame jf = new JFrame("测试");
        jf.setSize(300, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation((int)(di.getWidth() - jf.getWidth()) / 2, 
                (int)(di.getHeight() - jf.getHeight()) / 2);
        FTPClient aFtp = new FTPClient();
        aFtp.connect("166.111.80.101");
        RemoteFilePanel remote = new RemoteFilePanel(aFtp);
        remote.initListFiles("/");
        jf.add(remote);
        jf.setVisible(true);
    }

    //实现相应的tablemodel类
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

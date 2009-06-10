package FTP;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JTree;

import sun.net.TelnetInputStream;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpLoginException;

public class FTPFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	FtpClient ftp = null;

	private List list = new List();

	private JPanel FtpClientFrame = new JPanel(new BorderLayout());
	private JPanel FtpClientFrameOne = new JPanel(new FlowLayout(
			FlowLayout.LEFT));
	private JPanel FtpClientFrameTwo = new JPanel(new GridLayout(1, 8));
	private JPanel FtpClientFrameThree = new JPanel(new GridLayout(2, 1));
	private JPanel FtpClientFrameFour = new JPanel(new GridLayout(1, 2));

	// 连接、断开按钮
	private JButton linkButton = new JButton("Link");
	private JButton breakButton = new JButton("Break");
	// 连接状态
	private JLabel statusLabel = new JLabel();
	// 用户登录
	private JLabel urlLabel = new JLabel("Ftp   URL:");
	private JLabel usernameLabel = new JLabel("username:");
	private JLabel passwordLabel = new JLabel("password:");
	private JLabel portLabel = new JLabel("port:");
	private JTextField urlTextField = new JTextField(10);
	private JTextField usernameTextField = new JTextField(10);
	private JTextField passwordTextField = new JTextField(10);
	private JTextField portTextField = new JTextField(10);
	// 本地、远程窗口
	DefaultListModel modelList = new DefaultListModel();
	private JList localList = new JList();
	private JList distanceList = new JList();
	JScrollPane localScrollPane = new JScrollPane(localList);
	JScrollPane distanceScrollPane = new JScrollPane(distanceList);
	// 本地、远程目录树
	private JTree localTree;
	private JTree ServerTree;

	public FTPFrame() {

		FtpClientFrameOne.add(linkButton);
		FtpClientFrameOne.add(breakButton);
		FtpClientFrameOne.add(statusLabel);

		FtpClientFrameTwo.add(urlLabel);
		FtpClientFrameTwo.add(urlTextField);
		FtpClientFrameTwo.add(usernameLabel);
		FtpClientFrameTwo.add(usernameTextField);
		FtpClientFrameTwo.add(passwordLabel);
		FtpClientFrameTwo.add(passwordTextField);
		FtpClientFrameTwo.add(portLabel);
		FtpClientFrameTwo.add(portTextField);

		FtpClientFrameThree.add(FtpClientFrameOne);
		FtpClientFrameThree.add(FtpClientFrameTwo);

		FtpClientFrameFour.add(localScrollPane);
		FtpClientFrameFour.add(list);

		FtpClientFrame.add(FtpClientFrameThree, "North");
		FtpClientFrame.add(FtpClientFrameFour, "Center");

		setContentPane(FtpClientFrame);
		setTitle("Ftp客户端");
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		linkButton.addActionListener(this);
		breakButton.addActionListener(this);
	}

	public String getDir(String path) {
		String dirName;
		int ch;
		int begin = 55;
		dirName = path.substring(begin).trim();
		return dirName;
	}

	public void loadList() {
		StringBuffer buf = new StringBuffer();
		int ch;
		list.removeAll();
		try {
			TelnetInputStream t = ftp.list();
			t.setStickyCRLF(true);
			while ((ch = t.read()) >= 0) {
				if (ch == '\n') {
					list.add(getDir(buf.toString()));
					buf.setLength(0);
				} else {
					buf.append((char) ch);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		list.validate();
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == linkButton) {
			// 连接Ftp服务器

			try {
				if (ftp != null)
					ftp.closeServer();
				statusLabel.setText("连接中，请等待.....");
				ftp = new FtpClient(urlTextField.getText());
				ftp.login(usernameTextField.getText(), passwordTextField
						.getText());
				ftp.binary();
			} catch (FtpLoginException e) {
				JOptionPane.showMessageDialog(null, "Login   Failure!!!");
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, urlTextField.getText()
						+ "Connection   Failure!!!");
				e.printStackTrace();
			} catch (SecurityException e) {
				JOptionPane.showMessageDialog(null, "No   Purview!!!");
				e.printStackTrace();
			}
			if (urlTextField.getText().equals(""))
				JOptionPane.showMessageDialog(null, "Ftp服务器地址不能空!!!");
			else if (usernameTextField.getText().equals(""))
				JOptionPane.showMessageDialog(null, "用户名不能为空!!!");
			else if (passwordTextField.getText().equals(""))
				JOptionPane.showMessageDialog(null, "密码不能为空!!!");
			else
				statusLabel.setText("已连接到Ftp:" + urlTextField.getText());
			loadList();
		}
		if (source == breakButton) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FTPFrame ftpClientFrame = new FTPFrame();
	}

}
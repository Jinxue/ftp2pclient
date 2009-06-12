package FTP;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class ServerInfoPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String host, username, password, port;

	// This is the server info panel
	private JButton jbConnect;
	private JComboBox jcbHost;
	private JLabel jlHost, jlUser, jlPass, jlPort;
	private JPasswordField jbfPass;
	private JTextField jtfUser, jtfPort;

	private FTPClient ftp;
	
	private RemoteFilePanel remote;

	public ServerInfoPanel(FTPClient aFtp, RemoteFilePanel aRemote) {

		host = new String();
		username = new String();
		password = new String();
		port = new String();

		ftp = aFtp;
		
		this.remote = aRemote;

		jlHost = new JLabel();
		jcbHost = new JComboBox();
		jlUser = new JLabel();
		jtfUser = new JTextField();
		jlPass = new JLabel();
		jbfPass = new JPasswordField();
		jlPort = new JLabel();
		jtfPort = new JTextField();
		jbConnect = new JButton();

		setBorder(BorderFactory.createEtchedBorder());
		setName("jPanelServer"); // NOI18N
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		jlHost.setHorizontalAlignment(SwingConstants.LEFT);
		jlHost.setText("FTP服务器"); // NOI18N
		jlHost.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		jlHost.setName("jlHost"); // NOI18N
		add(jlHost);

		jcbHost.setEditable(true);
		jcbHost.setMinimumSize(new Dimension(200, 19));
		jcbHost.setName("jcbHost"); // NOI18N
		jcbHost.setPreferredSize(new Dimension(252, 21));
		add(jcbHost);

		jlUser.setText("用户名"); // NOI18N
		jlUser.setName("jlUser"); // NOI18N
		add(jlUser);

		// jtfUser.setText(resourceMap.getString("jtfUser.text")); //
		// NOI18N
		jtfUser.setMinimumSize(new Dimension(100, 21));
		jtfUser.setName("jtfUser"); // NOI18N
		jtfUser.setPreferredSize(new Dimension(80, 21));
		add(jtfUser);

		jlPass.setText("密码"); // NOI18N
		jlPass.setName("jlPass"); // NOI18N
		add(jlPass);

		// jbfPass.setText(resourceMap.getString("jbfPass.text"));
		// // NOI18N
		jbfPass.setName("jbfPass"); // NOI18N
		jbfPass.setPreferredSize(new Dimension(80, 21));
		add(jbfPass);

		jlPort.setText("端口"); // NOI18N
		jlPort.setName("jlPort"); // NOI18N
		add(jlPort);

		// jtfPort.setText(resourceMap.getString("jtfPort.text")); //
		// NOI18N
		jtfPort.setName("jtfPort"); // NOI18N
		jtfPort.setPreferredSize(new Dimension(40, 21));
		add(jtfPort);

		jbConnect.setText("连接"); // NOI18N
		jbConnect.setDoubleBuffered(true);
		jbConnect.setName("jbConnect"); // NOI18N
		jbConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				host = (String)jcbHost.getEditor().getItem();
				
				if ((username = jtfUser.getText()).isEmpty()) {
					username = "anonymous";
				}

				if (jbfPass.getPassword().length == 0) {
					password = "anonymous";
				} else
					password = new String(jbfPass.getPassword());

				if ((port = jtfPort.getText()).isEmpty()) {
					port = "21";
				}

				if (!host.isEmpty())
					try {
						if(ftp.connect(host, Integer.parseInt(port), username,
								password)){
							jcbHost.addItem(host);
							remote.initListFiles("/");
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		add(jbConnect);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		host = (String) jcbHost.getSelectedItem();
	}

	// 测试
	public static void main(String[] args) throws IOException,
			InterruptedException {
		JFrame jf = new JFrame("测试");
		jf.setSize(800, 70);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
		jf.setLocation((int) (di.getWidth() - jf.getWidth()) / 2, (int) (di
				.getHeight() - jf.getHeight()) / 2);
		FTPClient aFtp = new FTPClient();
		RemoteFilePanel aRemote = new RemoteFilePanel(aFtp);
		ServerInfoPanel serverInfo = new ServerInfoPanel(aFtp, aRemote);
		jf.add(serverInfo);
		jf.setVisible(true);
		// Thread.sleep(1000);
		// aFtp.setLocalWD("E:\\");
//		aFtp.connect("166.111.80.101", "zhang", "Myzhang123");
		// remote.initListFiles("/");
	}

}

package pro.network.btTransfer;
/*
 *初始化界面
 *作者：李腾飞 
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.InetAddress;
public class InitFrame extends JFrame {

	public InitFrame() {

		this.setSize(549, 312);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* 初始化panel */
		panel = new JPanel();
		
		/* 初始化"您的主机名"标签 */
		hostnameLabel = new JLabel("您的主机名：");
		
		/* 初始化"您的ip地址"标签 */
		ipLabel = new JLabel("您的ip地址：");
		
		/* 初始化其余两个Label */
		hostname = new JLabel();
		IP = new JLabel();
		setHostAndIP();
		
		/* 初始化comboBox */
		isServer = new JCheckBox("本机是否作为服务器");
		isServer.setSelected(true);
		isServer.addActionListener(new ComboListener());
		
		/* 初始化端口相关的label */
		portLabel = new JLabel("开启的端口号：");
		port = new JTextField();
		
		buttonOk = new JButton("确定");
		
		buttonOk.addActionListener(new OkListener());
		
		layOut();
		
	}
	
	/* 界面布局 */
	private void layOut() {
		org.jdesktop.layout.GroupLayout panelLayout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelLayout.createSequentialGroup()
                .add(161, 161, 161)
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(hostnameLabel)
                    .add(ipLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(24, 24, 24)
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, IP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, hostname, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                .addContainerGap(165, Short.MAX_VALUE))
            .add(panelLayout.createSequentialGroup()
                .add(216, 216, 216)
                .add(isServer)
                .addContainerGap(236, Short.MAX_VALUE))
            .add(panelLayout.createSequentialGroup()
                .add(182, 182, 182)
                .add(portLabel)
                .add(30, 30, 30)
                .add(port, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(237, Short.MAX_VALUE)
                .add(buttonOk)
                .add(235, 235, 235))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLayout.createSequentialGroup()
                .add(30, 30, 30)
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(hostnameLabel)
                    .add(hostname))
                .add(24, 24, 24)
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ipLabel)
                    .add(IP))
                .add(52, 52, 52)
                .add(isServer)
                .add(31, 31, 31)
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(portLabel)
                    .add(port, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(40, 40, 40)
                .add(buttonOk)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
	}
	
	/* 显示本机的主机名和IP地址 */
	private void setHostAndIP() {
		try{
			InetAddress inetAddress = InetAddress.getLocalHost();
			hostname.setText(inetAddress.getHostName());
			IP.setText(inetAddress.getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class ComboListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			if(isServer.isSelected()) {
				port.setVisible(true);
				portLabel.setVisible(true);
			} else {
				port.setVisible(false);
				portLabel.setVisible(false);
			}
		}
	}
	
	/**
	 * 添加在"确定"按钮上的操作
	 *
	 */
	private class OkListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			
			DownloadFrame downloadFrame = new DownloadFrame();
			downloadFrame.setVisible(true);
			
			if(isServer.isSelected()) {
				Server server = new Server(Integer.parseInt(port.getText().trim()));
				server.start();
			}
		}
	}
	
	/* 界面组件 */
	private JPanel panel;
	private JLabel hostnameLabel;
	private JLabel hostname;
	private JLabel ipLabel;
	private JLabel IP;
	private JLabel portLabel;
	private JTextField port;
	private JCheckBox isServer;
	private JButton buttonOk;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		InitFrame initFrame = new InitFrame();
		initFrame.setVisible(true);

	}

}

/*
 * FTPFrame.java
 *
 * Created on 2009年6月11日, 上午8:16
 */

package FTP;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 *
 * @author  zhang
 */
public class FTPFrame extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// This is the panel for FTP server infomation
	private ServerInfoPanel serverInfo;
	//This is the panel for local file system display
    private LocalFilePanel localFS;
    
    //This is the panel for remote FTP file system display
    private RemoteFilePanel remoteFS;
    
    //This the main frame
//    private JFrame jf;
    
    // This is the FTP client
	private FTPClient ftp;
    
    /** Creates new form FTPFrame */
    public FTPFrame() {
    	// For main frame initialization
//    	jf = new JFrame("FTP客户端");
//        jf.setSize(300, 400);
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
//        jf.setLocation((int)(di.getWidth() - jf.getWidth()) / 2, 
//                (int)(di.getHeight() - jf.getHeight()) / 2);
        
        this.setTitle("FTP客户端");
        ftp = new FTPClient();
        
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {

    	localFS = new LocalFilePanel(ftp);
        remoteFS = new RemoteFilePanel(ftp);
    	serverInfo = new ServerInfoPanel(ftp, remoteFS);
    	
    	localFS.setRemoteFileList(remoteFS);
    	remoteFS.setLocalFileList(localFS);
    	
        add(serverInfo, BorderLayout.NORTH);
        add(localFS, BorderLayout.WEST);
        add(remoteFS, BorderLayout.CENTER);
        
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        pack();
    }// </editor-fold>
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {                          
        System.exit(0);
    }                         
    
    public void actionPerformed(ActionEvent e){
    	
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	FTPFrame frame = new FTPFrame();
				Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation((int) (di.getWidth() - frame.getWidth()) / 2, (int) (di
						.getHeight() - frame.getHeight()) / 2);

            	frame.setVisible(true);
            }
        });
    }
}

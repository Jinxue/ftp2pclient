import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import pro.network.btTransfer.InitFrame;

import FTP.FTPFrame;


public class Ftp2pClient extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTabbedPane mainPane;
	
	public Ftp2pClient(){
//        super(new GridLayout(1, 1));
        
        mainPane = new JTabbedPane();
        String currentPath = System.getProperty("user.dir");
        System.out.println(currentPath);
//        ImageIcon icon = createImageIcon("/icon.png");
        
        FTPFrame panel1 = new FTPFrame();
//        mainPane.addTab("FTP Client", icon, panel1.getContentPane(),
//                "A FTP Client");
        mainPane.addTab("FTP Client", panel1.getContentPane());
        mainPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        InitFrame panel2 = new InitFrame();
//        mainPane.addTab("P2P Client", icon, panel2.getContentPane(),
//                "A P2P Client");
        mainPane.addTab("P2P Client", panel2.getContentPane());
        mainPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        //Add the tabbed pane to this panel.
        add(mainPane);
        
        //The following line enables to use scrolling tabs.
        mainPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        setName("Main"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        pack();
	}
	
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path) {
        URL imgURL = this.getClass().getResource(path);
//    	URL imgURL = null;
//		try {
//			imgURL = new URL(path);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {                          
        System.exit(0);
    }                         

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
            	Ftp2pClient frame = new Ftp2pClient();
            	frame.setSize(1024, 768);
            	frame.setTitle("FTPºÍP2P¿Í»§¶Ë");
				Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation((int) (di.getWidth() - frame.getWidth()) / 2, (int) (di
						.getHeight() - frame.getHeight()) / 2);

            	frame.setVisible(true);
            }
        });
    }
}

package FTP;

import java.io.IOException;

public class FTPClientTest {

	static String host = "tv6.sjtu.edu.cn";
//	static String host = "166.111.80.101";
	
	//For IPv6 testing
//	static String host = "fe80::21a:64ff:fea1:23c2";

	static FTPClient client = null;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println(System.getProperty("user.dir"));
		client = new FTPClient();
		
		// For connection test
		//client.connect(host, "zhang", "Myzhang123");
		client.connect(host, 5566, "sjtu", "sjtu");
//		client.connect(host);
		String pwd = client.pwd();
		System.out.println(pwd);
		
		// For List test
		client.list();
		client.list("大陆");
		client.cwd("大陆");

		client.list(new String(("[国产][大汉天子II 汉武雄风][国语][中文字幕][V2R][438K][40]").getBytes(), "gb2312"));
//		client.list("\\[国产\\]\\[大汉天子II\\ 汉武雄风\\]\\[国语\\]\\[中文字幕\\]\\[V2R\\]\\[438K\\]\\[40\\]");
		client.list();
		client.cwd("[国产][大汉天子II 汉武雄风][国语][中文字幕][V2R][438K][40]");
		client.list();
		
		client.cdup();
		client.list("[国产][三国演义][国语][D2R][84]");
		client.cwd("[国产][三国演义][国语][D2R][84]");
		client.list();
		client.bin();
//		client.download("[YYSoR]三国演义01.国语.灰色夏天.d-vb.rmvb");
		client.download("三国演义.jpg");
//		client.ascii();
		client.download("三国演义.txt");
		
		
		
		client.list("incoming");
		System.out.println("---------------------------------------");
		if(client.cwd("incoming")){
			System.out.println("Change the directory successfully!");
		}else
			System.out.println("Change the directory failed!");
		client.list("conf-out.xml");
//		client.cdup();
//		System.out.println(client.pwd());
//		client.list("conf-out.xml");
		
		// For download test
		client.download("conf-out.xml");
		client.download("conf-out1.xml");
		client.download("site-1.4.7.zip");

		// For file upload test
//		File file = new File("FTPClient.java");
//		client.stor(file);
//		
//		file = new File("sogou_pxp.exe");
//		client.stor(file);
		
		// For file delete test
//		client.delete("FTPClient.java");
		
		// For directory test
		client.mkDirectory("hello");
		client.rmDirectory("hello");
		client.rename("conf-out.xml", "conf.xml");
		
		//For IPv6 test
		client.setEPasv();
		
		client.disconnect();

	}

}

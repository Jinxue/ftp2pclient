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
		client.list("��½");
		client.cwd("��½");

		client.list(new String(("[����][������II �����۷�][����][������Ļ][V2R][438K][40]").getBytes(), "gb2312"));
//		client.list("\\[����\\]\\[������II\\ �����۷�\\]\\[����\\]\\[������Ļ\\]\\[V2R\\]\\[438K\\]\\[40\\]");
		client.list();
		client.cwd("[����][������II �����۷�][����][������Ļ][V2R][438K][40]");
		client.list();
		
		client.cdup();
		client.list("[����][��������][����][D2R][84]");
		client.cwd("[����][��������][����][D2R][84]");
		client.list();
		client.bin();
//		client.download("[YYSoR]��������01.����.��ɫ����.d-vb.rmvb");
		client.download("��������.jpg");
//		client.ascii();
		client.download("��������.txt");
		
		
		
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

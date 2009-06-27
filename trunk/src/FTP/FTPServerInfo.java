package FTP;

public class FTPServerInfo {
	public String host;
	public int port;
	public String username;
	public String password;

	public FTPServerInfo() {
		port = 21;
		username = "anonymous";
		password = "anonymous";
	}
}

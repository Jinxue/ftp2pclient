package pro.network.btTransfer;
/*
 * 结构化存储BT文件信息的类
 * 作者：苗亚杰
 */
public class FileInfo {
	private int id;                //序列号

	private String filename;       //文件名
	
	private String path;           //文件路径
	
	private String serverURL;      //服务器的URL地址
	
	private int port;              //服务器的端口号
	
	private Long offset;           //传送文件的偏移量
	
	private Long len;              //传送文件数据的长度
	
	
	/**
	 * id属性的setter和getter
	 */
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	/**
	 * filename属性的setter和getter
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * path属性的setter和getter
	 */
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return this.path;
	}
	
	/**
	 * server属性的setter和getter
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	public String getServerURL() {
		return this.serverURL;
	}
	
	/**
	 * port属性的setter和getter
	 */
	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return this.port;
	}
	
	/**
	 * offset属性的setter和getter
	 */
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	public Long getOffset() {
		return this.offset;
	}
	
	/**
	 * len属性的setter和getter
	 */
	public void setLen(Long len) {
		this.len = len;
	}
	public Long getLen() {
		return this.len;
	}
	
}

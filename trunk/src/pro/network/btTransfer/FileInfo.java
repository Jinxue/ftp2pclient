package pro.network.btTransfer;
/*
 * �ṹ���洢BT�ļ���Ϣ����
 * ���ߣ����ǽ�
 */
public class FileInfo {
	private int id;                //���к�

	private String filename;       //�ļ���
	
	private String path;           //�ļ�·��
	
	private String serverURL;      //��������URL��ַ
	
	private int port;              //�������Ķ˿ں�
	
	private Long offset;           //�����ļ���ƫ����
	
	private Long len;              //�����ļ����ݵĳ���
	
	
	/**
	 * id���Ե�setter��getter
	 */
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	/**
	 * filename���Ե�setter��getter
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * path���Ե�setter��getter
	 */
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return this.path;
	}
	
	/**
	 * server���Ե�setter��getter
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	public String getServerURL() {
		return this.serverURL;
	}
	
	/**
	 * port���Ե�setter��getter
	 */
	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return this.port;
	}
	
	/**
	 * offset���Ե�setter��getter
	 */
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	public Long getOffset() {
		return this.offset;
	}
	
	/**
	 * len���Ե�setter��getter
	 */
	public void setLen(Long len) {
		this.len = len;
	}
	public Long getLen() {
		return this.len;
	}
	
}

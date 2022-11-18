package inrowbackend.model;

public class GamePrivateModel {
	
	private String code; // CODE OF PRIVATE GAME.
	private String request; // REQUEST OF MESSAGE: CREATE OR FIND.
	
	public GamePrivateModel(String code, String request) {
		super();
		this.code = code;
		this.request = request;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
	
}

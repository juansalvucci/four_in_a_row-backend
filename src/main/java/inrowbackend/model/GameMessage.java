package inrowbackend.model;

import org.json.JSONObject;

public class GameMessage {
	
	private String gameId;
	private Object data;
	private Object detail;
	
	public GameMessage(Object gameId, Object data, Object detail) {
		this.gameId = (String) gameId;
		this.data = data;
		this.detail = detail;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Object getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		JSONObject jsonGameMessage = new JSONObject();
		jsonGameMessage.put("gameId", gameId);
		jsonGameMessage.put("data", data);
		jsonGameMessage.put("detail", detail);
		return jsonGameMessage.toString();
	}
	
}

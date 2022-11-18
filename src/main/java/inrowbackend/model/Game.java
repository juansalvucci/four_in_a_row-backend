package inrowbackend.model;

import org.javatuples.Pair;
import org.springframework.web.socket.WebSocketSession;

public class Game {

	private String gameId;
	private Pair<String, WebSocketSession> user1 = null;
	private Pair<String, WebSocketSession> user2 = null;
	private Integer initialTurn = null;
	
	public Game(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Pair<String, WebSocketSession>  getUser1() {
		return user1;
	}

	public void setUser1(Pair<String, WebSocketSession>  user1) {
		this.user1 = user1;
	}

	public Pair<String, WebSocketSession>  getUser2() {
		return user2;
	}

	public void setUser2(Pair<String, WebSocketSession>  user2) {
		this.user2 = user2;
	}
	
	public String getUserData1() {
		return this.getUser1().getValue0();
	}
	
	public String getUserData2() {
		return this.getUser2().getValue0();
	}
	
	public WebSocketSession getWs1() {
		return this.getUser1().getValue1();
	}
	
	public WebSocketSession getWs2() {
		return this.getUser2().getValue1();
	}
	
	public void setIntialTurn(Integer value) {
		if(this.getInitialTurn() == null) {
			this.initialTurn = value;
		}
	}
	
	public Integer getInitialTurn() {
		return initialTurn;
	}
	
	public void addUser(Pair<String, WebSocketSession>  user) {
		if (this.getUser1() == null) {
			this.user1 = user;
		}
		else {
			this.user2 = user;
		}
	}
	
	public boolean isReady() {
		return this.getUser1() != null && this.getUser2() != null;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", user1=" + user1 + ", user2=" + user2 + "]";
	}

}

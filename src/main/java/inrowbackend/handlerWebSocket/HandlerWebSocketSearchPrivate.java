package inrowbackend.handlerWebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import inrowbackend.model.GamePrivateModel;

@Service
public class HandlerWebSocketSearchPrivate extends TextWebSocketHandler {
	
	private Collection<WebSocketSession> webSocketSessions = Collections.synchronizedCollection(new ArrayList<>());
    private Map<String, WebSocketSession> users = Collections.synchronizedMap(new HashMap<String, WebSocketSession>());
	private Object flag = new Object();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("NEW PRIVATE OPEN SESSION");
		webSocketSessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		GamePrivateModel data = this.messageToGamePrivateModel(message);
		synchronized (flag) {
			if(data.getRequest().equals("CREATE")) {
				this.addCodeAndSession(data.getCode(), session);
			}
			else {
				this.findCodeAndSend(data.getCode(), session);
			}
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Entry<String, WebSocketSession> gameOfUser = this.getGameBySession(session);
		if(gameOfUser != null) {
			users.remove(gameOfUser);
		}
		webSocketSessions.remove(session);
	}
	
	private void addCodeAndSession(String code, WebSocketSession session) {
		users.put(code, session);
	}
	
	private void findCodeAndSend(String code, WebSocketSession session) throws IOException {
		Entry<String, WebSocketSession> game = this.getGameByCode(code);
		if(game != null) {
			TextMessage gameId = new TextMessage(UUID.randomUUID().toString());
			game.getValue().sendMessage(gameId);
			session.sendMessage(gameId);
		}
		else {
			session.sendMessage(new TextMessage("THE GAME WAS NOT FOUND"));
		}
	}
	
	private Entry<String, WebSocketSession> getGameByCode(String code) {
		for (Entry<String, WebSocketSession> entry : users.entrySet()) {
		    if(entry.getKey().equals(code)) {
		    	return entry;
		    }
		}
		return null;
	}
	
	private Entry<String, WebSocketSession> getGameBySession(WebSocketSession session) {
		for (Entry<String, WebSocketSession> entry : users.entrySet()) {
		    if(entry.getValue().getId().equals(session.getId())) {
		    	return entry;
		    }
		}
		return null;
	}
	
	private GamePrivateModel messageToGamePrivateModel(TextMessage message) {
		String[] data = message.getPayload().split(";", 2);
		return new GamePrivateModel(data[0], data[1]);
	}

}

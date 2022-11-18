package inrowbackend.handlerWebSocket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class HandlerWebSocketSearchPublic extends TextWebSocketHandler {

	private Collection<WebSocketSession> webSocketSessions = Collections.synchronizedCollection(new ArrayList<>());
	private WebSocketSession waitingUser = null;
	private Object flag = new Object();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		webSocketSessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		synchronized (flag) {
			if (waitingUser == null) {
				waitingUser = session;
			} else {
				TextMessage gameId = new TextMessage(UUID.randomUUID().toString());
				session.sendMessage(gameId);
				waitingUser.sendMessage(gameId);
				waitingUser = null;
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		synchronized (flag) {
			if(waitingUser != null) {
				waitingUser = null;
			}
			webSocketSessions.removeIf(s -> s.getId().equals(session.getId()));
		}
	}

}

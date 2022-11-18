package inrowbackend.handlerWebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class HandlerWebSocketHome extends TextWebSocketHandler {

	private Collection<WebSocketSession> usersInHome = Collections.synchronizedCollection(new ArrayList<>());

	public Collection<WebSocketSession> getWebSocketHomeSessions() {
		return usersInHome;
	}
	
	@Override
	public synchronized void afterConnectionEstablished(WebSocketSession session) throws Exception {
		usersInHome.add(session);
	}

	@Override
	protected synchronized void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		usersInHome.forEach(w -> {
			try {
				if(w.isOpen()) {
					w.sendMessage(new TextMessage(String.valueOf(usersInHome.size()).toString()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	@Override
	public synchronized void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		usersInHome.removeIf(s -> s.getId().equals(session.getId()));
		usersInHome.forEach(w -> {
			if(w.isOpen()) {
				try {
					w.sendMessage(new TextMessage(String.valueOf(usersInHome.size())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

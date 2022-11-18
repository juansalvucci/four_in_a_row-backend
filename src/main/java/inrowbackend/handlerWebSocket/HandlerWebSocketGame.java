package inrowbackend.handlerWebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import inrowbackend.model.Game;
import inrowbackend.model.GameMessage;

@Service
public class HandlerWebSocketGame extends TextWebSocketHandler {
	
	private List<Game> games = new ArrayList<Game>();
	private Object flag = new Object();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		synchronized (flag) {
			var gameData = this.messageToGameMessageObject(message);
			Game game = this.getGameById(gameData.getGameId());
			if(gameData.getDetail().equals("ADD ME TO GAME")) {
				this.addSessionToGame(gameData, game, session);
			}
        	else {
        		this.sendMessageToGame(gameData, game);
        	}
		}
    }

	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		this.endGame(session);
    }

	private void addSessionToGame(GameMessage gameData, Game game, WebSocketSession session) throws IOException {
		synchronized (flag) {
			if(game == null) {
				this.createGame(gameData.getGameId());
				game = this.getGameById(gameData.getGameId());
			}
			game.addUser(new Pair<String, WebSocketSession>((String) gameData.getData(),session));
			if(!game.isReady()) {
				this.sendMessageWaiting(gameData, session);
			}
			else {
				Random random = new Random();
				game.setIntialTurn(random.nextInt((2 - 1) + 1) + 1);
				this.sendMessageToGame(new GameMessage(gameData.getGameId(),new ArrayList<Object>(Arrays.asList(game.getUserData1(), game.getUserData2(), game.getInitialTurn())), "READY"), game);
			}
		}
	}

	private void createGame(String gameId) {
		this.games.add(new Game(gameId));
	}
	
	private void sendMessageToGame(GameMessage data, Game game) throws IOException {
			((Game) game).getWs1().sendMessage(new TextMessage(data.toString()));
			((Game) game).getWs2().sendMessage(new TextMessage(data.toString()));
	}
	
	private void sendMessageWaiting(GameMessage gameData, WebSocketSession session) throws IOException {
		GameMessage messageWaiting = new GameMessage(gameData.getGameId(), null, "WAITING");
		session.sendMessage(new TextMessage(messageWaiting.toString()));
	}
	
	private GameMessage messageToGameMessageObject(TextMessage message) {
		var messageData = message.getPayload();
		JSONObject jsonData = new JSONObject(messageData);
		return new GameMessage(jsonData.get("gameId"),jsonData.get("data"),jsonData.get("detail"));
	}
	
	private Game getGameById(String data) {
		synchronized (flag) {
			var game =  this.games.stream().filter(g -> g.getGameId().equals(data)).collect(Collectors.toList());
			if (game.size() == 0) {
				return null;
			}
			return game.get(0);
		}
	}
    
    private void endGame(WebSocketSession session) throws IOException {
		synchronized (flag) {
		GameMessage messageDisconnect = new GameMessage(null, null, "DISCONNECT");
        for(int i = 0; i < games.size(); i++) {
        	WebSocketSession session1 = games.get(i).getWs1();
        	WebSocketSession session2 = games.get(i).getWs2();
        		if(session1.isOpen()) {
        			session1.sendMessage(new TextMessage(messageDisconnect.toString()));
        		}
        		if(session2.isOpen()) {
            		session2.sendMessage(new TextMessage(messageDisconnect.toString()));
        		}
        		if (!(session1.isOpen() && session2.isOpen())) {
        			games.remove(i);
        		}  	
        	}
		}
    }
    
}
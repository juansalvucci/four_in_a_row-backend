package inrowbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import inrowbackend.handlerWebSocket.HandlerWebSocketGame;
import inrowbackend.handlerWebSocket.HandlerWebSocketHome;
import inrowbackend.handlerWebSocket.HandlerWebSocketSearchPrivate;
import inrowbackend.handlerWebSocket.HandlerWebSocketSearchPublic;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
	
	@Value("${ws.home.endpoint}")
	private String HOME_END_POINT;
	
	@Value("${ws.game.endpoint}")
	private String GAME_END_POINT;
	
	@Value("${ws.searchpublic.endpoint}")
	private String SEARCH_PUBLIC_END_POINT;
	
	@Value("${ws.searchprivate.endpoint}")
	private String SEARCH_PRIVATE_END_POINT;
	
	@Autowired
	private HandlerWebSocketHome handlerWebSocketHome;
	
	@Autowired
	private HandlerWebSocketGame handlerWebSocketGame;
	
	@Autowired
	private HandlerWebSocketSearchPublic handlerWebSocketSearchPublic;
	
	@Autowired
	private HandlerWebSocketSearchPrivate handlerWebSocketSearchPrivate;
	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(handlerWebSocketHome, HOME_END_POINT)
        .setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(handlerWebSocketGame, GAME_END_POINT)
                .setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(handlerWebSocketSearchPublic, SEARCH_PUBLIC_END_POINT)
        .setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(handlerWebSocketSearchPrivate, SEARCH_PRIVATE_END_POINT)
        .setAllowedOrigins("*");
    }

	public void setHandlerWebSocketGame(HandlerWebSocketGame handlerWebSocketGame) {
		this.handlerWebSocketGame = handlerWebSocketGame;
	}

	public HandlerWebSocketSearchPublic getHandlerWebSocketSearch() {
		return handlerWebSocketSearchPublic;
	}
	
	public HandlerWebSocketSearchPrivate getHandlerWebSocketPrivate() {
		return handlerWebSocketSearchPrivate;
	}

}

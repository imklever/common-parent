package com.isoftstone.common.api.support.newwebsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
@Component
public class WebSocketEventListener {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
	    System.out.println("新人加入！");
	}
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		System.out.println("有人退出！");
	    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
	    String username = (String) headerAccessor.getSessionAttributes().get("username");
	    System.out.println(username+"退出！！！");
	    if(username != null) {
	         logger.info("User Disconnected : " + username);
	    }
    }
}

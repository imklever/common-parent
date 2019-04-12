package com.isoftstone.common.api.controller.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.isoftstone.common.common.MessageDto;


//@Controller
public class ChatController {
		@Autowired
		SimpMessageSendingOperations messagingTemplate;
	
	 	@MessageMapping("/chat.addUser")
	    public MessageDto sendMessage(@Payload MessageDto msg
	    		,SimpMessageHeaderAccessor headerAccessor) {
	 		headerAccessor.getSessionAttributes().put("username",msg.getSender());
	        return msg;
	    }
}

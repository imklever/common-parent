package com.isoftstone.common.api.service.socket.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.isoftstone.common.api.service.socket.ChatService;
import com.isoftstone.common.common.MessageDto;

//@Service
public class ChatServiceImpl implements ChatService{
	@Autowired
	SimpMessageSendingOperations messagingTemplate;
	private final ConcurrentMap<String, String> userSessionIds =  new ConcurrentHashMap<String, String>();
	public void addUser(String username){
		System.out.println(username);
		userSessionIds.put(username,null);
	}
	public void delUser(String username) {
		System.out.println(username);
		userSessionIds.remove(username);
	}
	@Override
	public void sendRoldGropMsg(String role, String group, String destination,MessageDto messageDto) {
		messagingTemplate.convertAndSendToUser("username", "smg", messageDto);
		messagingTemplate.convertAndSend("/topic/public", messageDto);
		for (String name : userSessionIds.keySet()) {
			//messagingTemplate.convertAndSendToUser(name, destination, messageDto);
		}
	}
	
}

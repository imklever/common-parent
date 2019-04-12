package com.isoftstone.common.api.service.socket;

import com.isoftstone.common.common.MessageDto;

public interface ChatService {
	public void addUser(String username);
	public void delUser(String username);
	public void sendRoldGropMsg(String role, String group, String destination, MessageDto messageDto);
}

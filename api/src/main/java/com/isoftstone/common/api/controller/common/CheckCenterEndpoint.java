package com.isoftstone.common.api.controller.common;

import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.api.support.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/checkcenter")
public class CheckCenterEndpoint {
	@RequestMapping(value = "/sendAll", method = RequestMethod.GET)
	/**
	 * 群发消息内容
	 * 
	 * @param message
	 * @return
	 */
	public Object sendAllMessage(@RequestParam(required = true) String message) {
		try {
			WebSocketServer.BroadCastInfo(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return APIResult.createSuccess();
	}

	@RequestMapping(value = "/sendOne", method = RequestMethod.GET)
	/**
	 * 指定会话ID发消息
	 * 
	 * @param message
	 *            消息内容
	 * @param id
	 *            连接会话ID
	 * @return
	 */
	public Object sendOneMessage(@RequestParam(required = true) String message, @RequestParam(required = true) String id) {
		try {
			WebSocketServer.SendMessage(id, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return APIResult.createSuccess();
	}

}

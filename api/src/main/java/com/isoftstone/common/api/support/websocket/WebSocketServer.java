package com.isoftstone.common.api.support.websocket;

import com.isoftstone.common.api.service.HuaWeiAIService.HuaWeiServiceImpl;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint("/ws/asset")
@Component
public class WebSocketServer {

	private static HuaWeiServiceImpl huaWeiServiceImpl;
	@Autowired
	public void setChatService(HuaWeiServiceImpl huaWeiServiceImpl) {
		WebSocketServer.huaWeiServiceImpl = huaWeiServiceImpl;
	}
	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
	private static final AtomicInteger OnlineCount = new AtomicInteger(0);
	// concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
	private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		SessionSet.add(session);
		int cnt = OnlineCount.incrementAndGet(); // 在线数加1
		log.info("有连接加入，当前连接数为：{}", cnt);
		//SendMessage(session, "连接成功");
//		if(ZsyWarningPromptPush.dataList != null)
//			SendMessage(session, ZsyWarningPromptPush.dataList);
//		else
		Map<String,Object> map = new HashMap<>();
		map.put("msg","亲，欢迎来到蒙牛集团在线客服，请问有什么可以帮助您？");
		map.put("type",0);
//		SendMessage(session, JSONObject.fromObject(map).toString());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session) {
		SessionSet.remove(session);
		int cnt = OnlineCount.decrementAndGet();
		log.info("有连接关闭，当前连接数为：{}", cnt);
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("来自客户端的消息：{}",message);
//	        SendMessage(session, "收到消息，消息内容："+message);
		try {
			String sessionId = session.getId();
			Map m = (Map)JSONObject.fromObject(message);
			int type = Integer.parseInt(m.get("type").toString());
			Map<String,Object> map = new HashMap<>();
			switch (type){
				case 0:
					map = huaWeiServiceImpl.postAskMethod(message,sessionId);
					map.put("type",type);
					SendMessage(session, JSONObject.fromObject(map).toString());
					break;
				case 1:
					List<String> tips = huaWeiServiceImpl.postQuestionTips(m.get("msg").toString(),sessionId);
					map.put("type",type);
					if(tips.size() == 0)
						map.put("tipsList","[]");
					else
						map.put("tipsList",tips);
					SendMessage(session, JSONObject.fromObject(map).toString());
					break;
			}

//			SendMessage(session,huaWeiServiceImpl.postQuestionTips(message,session.getId()).toString());
		} catch (Exception e) {
			log.error("发送消息出错：{}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 出现错误
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
		error.printStackTrace();
	}

	/**
	 * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
	 * @param session
	 * @param message
	 */
	public static void SendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(String.format("%s",message));
		} catch (IOException e) {
			log.error("发送消息出错：{}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 群发消息
	 * @param message
	 * @throws IOException
	 */
	public static void BroadCastInfo(String message) throws IOException {
		for (Session session : SessionSet) {
			if(session.isOpen()){
				SendMessage(session, message);
			}
		}
	}

	/**
	 * 指定Session发送消息
	 * @param sessionId
	 * @param message
	 * @throws IOException
	 */
	public static void SendMessage(String sessionId,String message) throws IOException {
		Session session = null;
		for (Session s : SessionSet) {
			if(s.getId().equals(sessionId)){
				session = s;
				break;
			}
		}
		if(session!=null){
			SendMessage(session, message);
		}
		else{
			log.warn("没有找到你指定ID的会话：{}",sessionId);
		}
	}

}

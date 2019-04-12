package com.isoftstone.common.api.support.newwebsocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//@Configuration
//@EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller）
//开始支持@MessageMapping,就像是使用@requestMapping一样。
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
@Override//方法表示注册STOMP协议的节点，并指定映射的URL
	
	/** 
     * 将"/ws"路径注册为STOMP端点，这个路径与发送和接收消息的目的路径有所不同，
     * 这是一个端点，客户端在订阅或发布消息到目的地址前，要连接该端点， 
     * 即用户发送请求url="/applicationName/hello"与STOMP server进行连接。之后再转发到订阅url； 
     * PS：端点的作用——客户端在订阅或发布消息到目的地址前，要连接该端点。 
     * @param stompEndpointRegistry 
     */  
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        
        stompEndpointRegistry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
	}

    /** 
     * 配置了一个简单的消息代理，如果不重载，默认情况下回自动配置一个简单的内存消息代理，
     * 用来处理以"/topic"为前缀的消息。这里重载configureMessageBroker()方法， 
     * 消息代理将会处理前缀为"/topic"和"/queue"的消息。 
     * @param registry 
     */  
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//服务端发送消息给客户端的域,多个用逗号隔开
		registry.enableSimpleBroker("/topic","user/");  
        //定义一对一推送的时候前缀
        registry.setUserDestinationPrefix("/user");
        //定义websoket前缀
		registry.setApplicationDestinationPrefixes("/app");
        
	}
}

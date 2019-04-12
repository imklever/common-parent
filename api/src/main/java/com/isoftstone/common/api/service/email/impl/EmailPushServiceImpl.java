package com.isoftstone.common.api.service.email.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.api.service.email.EmailPushService;
import com.isoftstone.common.api.service.sms.impl.ALiSmsServiceImpl;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
@Service
public class EmailPushServiceImpl implements EmailPushService {
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	/*public static void main(String[] args) throws Exception {
		EmailPushServiceImpl emailPushServiceImpl=new EmailPushServiceImpl();
		emailPushServiceImpl.sendEmail("smtp.qq.com", "465","476763743@qq.com","xljpccvrdxwmbjcj","邮件主题","测试邮件","476763743@qq.com");
	}*/
    @Override
    public void sendEmail(String SMTPSERVER,String SMTPPORT,String ACCOUT,String PWD,String mailTheme,String mailText,String toemail) throws Exception {
    	// 创建邮件配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", SMTPPORT); 
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl
    
     // 根据邮件配置创建会话，注意session别导错包
        Session session = Session.getDefaultInstance(props);
        // 开启debug模式，可以看到更多详细的输入日志
        session.setDebug(true);
        //创建邮件
        MimeMessage message = createEmail(session,ACCOUT,mailTheme,toemail,mailText);
        //获取传输通道
        Transport transport = session.getTransport();
        transport.connect(SMTPSERVER,ACCOUT,PWD);
        //连接，并发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }
    
    public MimeMessage createEmail(Session session,String ACCOUT,String mailTheme,String toemail,String mailText) throws Exception {
        // 根据会话创建邮件
        MimeMessage msg = new MimeMessage(session);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(ACCOUT,
                "admin", "utf-8");
        // 设置邮件接收邮箱
        msg.setFrom(fromAddress);
        InternetAddress receiveAddress = new InternetAddress(
                toemail, "test", "utf-8");
        // 设置邮件接收方
        msg.setRecipient(RecipientType.TO, receiveAddress);
        // 设置邮件标题
        msg.setSubject(mailTheme, "utf-8");
        msg.setText(mailText);
        // 设置显示的发件时间
        msg.setSentDate(new Date());
        // 保存设置
        msg.saveChanges();
        return msg;
    }    
}

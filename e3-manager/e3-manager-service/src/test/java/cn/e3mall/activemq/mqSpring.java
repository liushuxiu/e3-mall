//package cn.e3mall.activemq;
//
//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;
//
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
//
//public class mqSpring {
//
//	@Test
//	public void sendMessage( ) throws Exception{
//		ApplicationContext context=
//				new ClassPathXmlApplicationContext(
//						"classpath:spring/applicationContext-activemq.xml");
//		
//		Destination destination= (Destination) context.getBean("queueDestination");
//		JmsTemplate jmsTemplate=context.getBean(JmsTemplate.class);
//		jmsTemplate.send(destination, new MessageCreator() {
//			
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				// TODO Auto-generated method stub
//				return session.createTextMessage("send activemq message2");
//			}
//		});
//		
//	}
//}

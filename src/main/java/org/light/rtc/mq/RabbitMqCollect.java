package org.light.rtc.mq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import org.light.rtc.base.MqConsumer;
import org.light.rtc.util.Constants;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMqCollect extends MqConsumer{
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	
	public RabbitMqCollect(){
		super();
		this.init();
	}
	
	public void init(){
		ConnectionFactory factory = new ConnectionFactory();
		factory.setPort(Constants.mqPort);
		factory.setHost(Constants.mqHost);
	    factory.setUsername(Constants.mqUser);
	    factory.setPassword(Constants.mqPswd);
	    factory.setVirtualHost(Constants.mqVhost);
	    try {
	    	connection = factory.newConnection();
	    	channel = connection.createChannel();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	    consumer = new QueueingConsumer(channel);
		try {
			channel.basicConsume(Constants.mqClickFirstQu, true, consumer);
			channel.basicConsume(Constants.mqClickSecondQu, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 为保证数据实时收集并及时提交到任务计算节点，务必将收集的消息，调用super.mqTimer.parseMqText(routingKey, message);
	 */
	public void run(){
		QueueingConsumer.Delivery delivery = null;
		String message = null, routingKey=null;
		while(true){
			try {
				delivery = consumer.nextDelivery();
			} catch (ShutdownSignalException | ConsumerCancelledException
					| InterruptedException e) {
				e.printStackTrace();
			}
			try {
				message = new String(delivery.getBody(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			routingKey = delivery.getEnvelope().getRoutingKey(); // 可获取路由关键词
			super.mqTimer.parseMqText(routingKey, message);
			message = null;
		}
	}
	
	/**
     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
     * @throws IOException
     */
     public void close() throws IOException{
         try {
			this.channel.close();
         } catch (TimeoutException e) {
			e.printStackTrace();
         }
         this.connection.close();
     }

//	public static void main(String[] args) {
//		RabbitMqCollect rmc = new RabbitMqCollect();
//		rmc.run();
//	}

}

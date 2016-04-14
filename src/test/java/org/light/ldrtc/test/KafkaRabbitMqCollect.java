package org.light.ldrtc.test;

import org.light.rtc.mq.KafkaMqCollect;
import org.light.rtc.mq.RabbitMqCollect;

public class KafkaRabbitMqCollect {
	
	public void run(){
		//如果选用Kafka
		KafkaMqCollect kmc = new KafkaMqCollect();
		kmc.collectMq();
		
		//如果选用RabbitMq
//		RabbitMqCollect rmc = new RabbitMqCollect();
//		rmc.run();
	}

	public static void main(String[] args) {
		KafkaRabbitMqCollect krc = new KafkaRabbitMqCollect();
		krc.run();
	}

}

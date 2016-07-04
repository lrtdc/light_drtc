package org.light.rtc.mq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.light.rtc.base.MqConsumer;
import org.light.rtc.util.Constants;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class KafkaMqCollect extends MqConsumer{
	
	private ConsumerConnector consumer;
	
	public KafkaMqCollect(){
		super();
		this.init();
	}
	
	public void init(){
		Properties props = new Properties();
        props.put("zookeeper.connect", Constants.kfZkServers);
        props.put("group.id", Constants.kfGroupId);
        props.put("auto.offset.reset", Constants.kfAutoOffsetReset);
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ConsumerConfig config = new ConsumerConfig(props);
        consumer = Consumer.createJavaConsumerConnector(config);
	}
	
	public void collectMq(){
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(Constants.kfTopic, new Integer(1));
 
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
 
        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
        
        KafkaStream<String, String> stream = consumerMap.get(Constants.kfTopic).get(0);
        ConsumerIterator<String, String> it = stream.iterator();
        MessageAndMetadata<String, String> msgMeta;
        while (it.hasNext()){
        	msgMeta = it.next();
        	super.mqTimer.parseMqText(msgMeta.key(), msgMeta.message());
        	//System.out.println(msgMeta.key()+"\t"+msgMeta.message());
        }
	}

//	public static void main(String[] args) {
//		KafkaMqCollect kmc = new KafkaMqCollect();
//		kmc.collectMq();
//	}

}

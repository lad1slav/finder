package en.ladislav.finderapi.services;

import en.ladislav.finderapi.model.FoundItems;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

    KafkaTemplate<String, FoundItems> kafkaTemplate;

    KafkaProducerService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public int sendResearchRequest(String topic, FoundItems query) {
        kafkaTemplate.send(topic, query);
        LOGGER.info("Send message to '" + topic + "': " + query);
        return 0;
    }
}


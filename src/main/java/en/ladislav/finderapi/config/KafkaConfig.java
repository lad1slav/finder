package en.ladislav.finderapi.config;

import en.ladislav.finderapi.model.FoundItems;
import en.ladislav.finderapi.model.ResearchQuery;
import en.ladislav.finderapi.model.Subscribe;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@EnableKafka
@Configuration
@EnableScheduling
public class KafkaConfig {

    @Value("${KAFKA_USERNAME}")
    String username;

    @Value("${KAFKA_PASSWORD}")
    String password;

    @Value("${KAFKA_BROKERS}")
    String brokers;

    @Bean
    public ProducerFactory<String, FoundItems> producerFactory() {
        Map<String, Object> properties = new HashMap<>();

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
//        properties.put("client.id", username + "-consumers");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("sasl.jaas.config", jaasCfg);
        properties.put("enable.idempotence", "false");
//        properties.put("ssl.ca.location", "C:/Users/Vladyslav.Manzhula/IdeaProjects/finder/ssl.ca");

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, FoundItems> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, ResearchQuery> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.manzhula.finder.models");
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, username + "-consumer");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//        properties.put("auto.commit.interval.ms", "1000");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
//        properties.put(ConsumerConfig.)
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("sasl.jaas.config", jaasCfg);

        JsonDeserializer<ResearchQuery> jsonDeserializer = new JsonDeserializer<>(ResearchQuery.class);
        jsonDeserializer.addTrustedPackages("com.manzhula.finder.models");

        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    public ConsumerFactory<String, Subscribe> consumerFactory1() {
        Map<String, Object> properties = new HashMap<>();
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.manzhula.finder.models");
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, username + "-consumer");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
//        properties.put("auto.commit.interval.ms", "1000");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
//        properties.put(ConsumerConfig.)
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-256");
        properties.put("sasl.jaas.config", jaasCfg);

        JsonDeserializer<Subscribe> jsonDeserializer = new JsonDeserializer<>(Subscribe.class);
        jsonDeserializer.addTrustedPackages("com.manzhula.finder.models");

        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ResearchQuery> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, ResearchQuery> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Subscribe> kafkaListenerContainerFactory1() {

        ConcurrentKafkaListenerContainerFactory<String, Subscribe> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory1());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}


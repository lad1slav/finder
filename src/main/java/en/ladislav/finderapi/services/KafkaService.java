package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.model.FoundItems;
import en.ladislav.finderapi.model.Query;
import en.ladislav.finderapi.model.ResearchQuery;
import en.ladislav.finderapi.model.Subscribe;
import en.ladislav.finderapi.repository.ItemRepository;
import en.ladislav.finderapi.repository.QueryRepository;
import en.ladislav.finderapi.repository.SubscribeRepository;
import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.property.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class KafkaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

    @Value("${KAFKA_TOPIC_PREFIX}")
    String prefix;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    QueryRepository queryRepository;

    @Autowired
    FinderService finderService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SubscribeRepository subscribeRepository;

    @Autowired
    ItemRepository itemRepository;

    @KafkaListener(topics = "${KAFKA_TOPIC_PREFIX}research-request", groupId = "${KAFKA_USERNAME}-consumer", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupFoo(@Payload ResearchQuery message, Acknowledgment ack) {
        LOGGER.info("Received Message: " + message);

        String sqlQuery = "SELECT id from query where query LIKE '%" + message.getQuery() + "%'";
        List<Integer> strings = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        List<Query> queries = new ArrayList<>();
        strings.forEach(s-> {
            queryRepository.findById(s).ifPresent(queries::add);
        });

        if (!queries.isEmpty()) {
            double avgPrice = 0;
            for (Query i : queries) {
                avgPrice += i.getAveragePrice();
            }
            avgPrice /= queries.size();
            kafkaProducerService.sendResearchRequest(prefix + "research-response",
                    FoundItems.builder()
                            .chatId(message.getChatId())
                            .query(message.getQuery())
                            .messageToDel(message.getMessageToDel())
                            .avgPrice(avgPrice)
                            .build());
        }

        Filter properties = new Filter();
        List<ItemDto> foundedItems = finderService.find(message.getQuery(), properties,
                Set.of(ParserList.PROM_PARSER, ParserList.COMFY_PARSER));

        kafkaProducerService.sendResearchRequest(prefix + "research-response",
                FoundItems.builder()
                        .chatId(message.getChatId())
                        .query(message.getQuery())
                        .messageToDel(message.getMessageToDel())
                        .avgPrice(-1d)
                        .foundItems(foundedItems)
                        .build());

        ack.acknowledge();
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_PREFIX}subscribe-request", groupId = "${KAFKA_USERNAME}-consumer", containerFactory = "kafkaListenerContainerFactory1")
    public void subscribe(@Payload Subscribe message, Acknowledgment ack) {
        LOGGER.info("Received Message: " + message);
        Subscribe newSub = Subscribe.builder()
                .userId(message.getUserId())
//                .itemId()
                .build();


        subscribeRepository.save(message);
        ack.acknowledge();
    }
}

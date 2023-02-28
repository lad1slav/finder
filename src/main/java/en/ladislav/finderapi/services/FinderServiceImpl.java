package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.mapper.ItemMapper;
import en.ladislav.finderapi.model.FoundItems;
import en.ladislav.finderapi.model.Query;
import en.ladislav.finderapi.model.Subscribe;
import en.ladislav.finderapi.repository.ItemRepository;
import en.ladislav.finderapi.repository.QueryRepository;
import en.ladislav.finderapi.repository.SubscribeRepository;
import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.Parser;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.property.Filter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FinderServiceImpl implements FinderService {

    @Value("${KAFKA_TOPIC_PREFIX}")
    String prefix;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public List<ItemDto> trim(List<ItemDto> itemDtos, Filter properties) {
        log.info("Searching for [{}] items", itemDtos.size());
        Long time = System.currentTimeMillis();

        ArrayList<Item> items = new ArrayList<>();
        itemDtos.forEach(itemDto -> {
//            try {
                items.add(new Item(itemDto));
//            } catch (IllegalArgumentException e) {
//                throw new BadRequestException(e.getMessage());
//            }
        });

//        log.info("Wrapped ItemDto to Item: left [{}] items", items.size());

        List<Item> result = properties.trim(items);

        time = System.currentTimeMillis() - time;
        log.info("Found [{}] items in [{}] ms", result.size(), time);

        return itemMapper.itemsToItemDtos(result);
    }

    @Override
    @Transactional
    public List<ItemDto> find(String findQuery, Filter properties, Set<ParserList> parserIdentifiers) {
        ArrayList<Item> result = new ArrayList<>();
        String sqlQuery = "SELECT id from query where query LIKE '%" + findQuery + "%'";
        List<Integer> strings = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        List<Query> queries = new ArrayList<>();
        strings.forEach(s-> {
            queryRepository.findById(s).ifPresent(queries::add);
        });

        List<String> queriesName = queries.stream().map(Query::getQueryName).collect(Collectors.toList());

        log.info("Searching for query \"{}\"", findQuery);
        Long time = System.currentTimeMillis();

        if (queries.isEmpty() || !queriesName.contains(findQuery)) {
            parserIdentifiers.forEach(identifier -> {
                result.addAll(identifier.getParser().find(findQuery));
            });
        } else {
            queries.forEach(q->{
                result.addAll(itemRepository.getAllByQuery(q.getQueryId()));
            });
        }

        log.info("Found {} items", result.size());

        time = System.currentTimeMillis() - time;
        log.info("Searching for query \"{}\" was finished in {} ms", findQuery, time);

        double avgPrice = 0;
        for (Item i : result) {
            avgPrice+=i.getItemPrice();
        }
        avgPrice/=result.size();

        if(queries.isEmpty() || !queriesName.contains(findQuery)) {
            Query query = Query.builder()
                    .queryName(findQuery)
                    .averagePrice(avgPrice)
                    .build();

            Query savedquery = queryRepository.save(query);
            Set<Item> items = new HashSet<>(result);
            items.forEach(i->i.setQuery(savedquery.getQueryId()));
            itemRepository.saveAll(items);
        }

        return itemMapper.itemsToItemDtos(properties.trim(result));
    }

    @Override
    public List<ItemDto> find(String findQuery, Filter properties) {
        return this.find(findQuery, properties, ParserList.allParserIdentifiers);
    }

    @Scheduled(fixedDelay = 100000, initialDelay = 2000)
    public void findSubscribers() {
        subscribeRepository.findAll().forEach(subscribe -> {
            Optional<Item> item = itemRepository.findById(Integer.parseInt(subscribe.getItemId()));
            if (item.isPresent()) {
                Item currentItem = item.get();
                Parser parser = ParserList.PROM_PARSER.getParser();
                try {
                    if (parser.getElementExistFromPage(Jsoup.connect(currentItem.getItemURL()).get())) {
                        log.info("Item went on sale, " + currentItem);
                        currentItem.setItemPresence(true);
                        kafkaProducerService.sendResearchRequest(prefix + "research-response",
                                FoundItems.builder()
                                        .chatId(Long.parseLong(subscribe.getUserId()))
                                        .isSubscribed(true)
                                        .foundItems(itemMapper.itemsToItemDtos(List.of(currentItem)))
                                        .build());
                        itemRepository.save(currentItem);
                        subscribeRepository.delete(subscribe);
                    } else {
                        log.info("Item is no available for sale, " + currentItem);
                        kafkaProducerService.sendResearchRequest(prefix + "research-response",
                                FoundItems.builder()
                                        .chatId(Long.parseLong(subscribe.getUserId()))
                                        .isSubscribed(true)
                                        .query(currentItem.getItemName())
                                        .build());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

//    @Scheduled(fixedDelay = 100000000, initialDelay = 2000)
//    public void findComfy() {
//        ParserList.COMFY_PARSER.getParser().find("ноутбук lenovo");
//    }
}

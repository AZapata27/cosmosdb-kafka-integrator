package com.data.lake.cosmosdb.listeners;

import com.azure.cosmos.CosmosContainer;
import com.data.lake.cosmosdb.config.TopicsConfig;
import com.data.lake.cosmosdb.repository.CosmosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryConsumer {

    private final CosmosRepository cosmosRepository;
    private final CosmosContainer cosmosContainer;

    public CategoryConsumer(CosmosRepository cosmosRepository,
                            @Qualifier("category") CosmosContainer cosmosContainer) {
        this.cosmosRepository = cosmosRepository;
        this.cosmosContainer = cosmosContainer;
    }

    @KafkaListener(
            id = "category",
            topics = TopicsConfig.CATEGORY,
            groupId = TopicsConfig.CONSUMER_GROUP,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeMessages(@Payload String messages, @Header(KafkaHeaders.RECEIVED_KEY) String keys) {

        cosmosRepository.saveIntoCosmos(messages, keys, cosmosContainer, TopicsConfig.CATEGORY);

    }
}

package com.ms.artistservice.messaging;

import com.ms.dtos.ArtistDTO;
import com.ms.dtos.EventAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ArtistProducer {
    private final KafkaTemplate<String, ArtistDTO> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ArtistProducer.class);
    public ArtistProducer(KafkaTemplate<String, ArtistDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void handleArtistEvent(EventAction eventAction, ArtistDTO artistDTO){
        kafkaTemplate.send("artist-events", artistDTO.id().toString(), artistDTO)
                .whenComplete(((result, throwable -> {
                    if(throwable==null){
                        logger.info("Message sent successfully! topic : {}, partition: {}, offset: {}",
                                result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else{
                        logger.warn("Problem to sent message! Error: {}", throwable.getMessage());
                    }
                })))
    }
}

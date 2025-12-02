package com.ms.artistservice.messaging;

import com.ms.dtos.artist.ArtistDTO;
import com.ms.dtos.EventAction;
import com.ms.dtos.artist.ArtistEventMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistProducer {
    private final KafkaTemplate<String, ArtistEventMessage> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ArtistProducer.class);
    private static final String TOPIC = "artist-events";
    public void handleArtistEvent(EventAction eventAction, ArtistDTO artistDTO){
        var eventMessage = new ArtistEventMessage(eventAction, artistDTO);
        var key = artistDTO.id().toString();

        kafkaTemplate.send("artist-events",key,eventMessage )
                .whenComplete((result, throwable )-> {
                    if(throwable==null){
                        logger.info("Message sent successfully! topic : {}, partition: {}, offset: {}",
                                result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else{
                        logger.warn("Problem to sent message! Error: {}", throwable.getMessage());
                    }
                });
    }
}

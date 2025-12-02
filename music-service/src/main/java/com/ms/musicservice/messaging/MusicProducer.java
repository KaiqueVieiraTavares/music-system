package com.ms.musicservice.messaging;
import com.ms.dtos.music.MusicEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MusicProducer {
    private static final Logger logger = LoggerFactory.getLogger(MusicProducer.class);
    private final KafkaTemplate<String, MusicEventDTO> kafkaTemplate;

    @Value("${spring.kafka.topic.music-events}")
    private String topicName;
    public MusicProducer(KafkaTemplate<String, MusicEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }
    public void handleMusicEvent(MusicEventDTO musicEventDTO){
        String key = musicEventDTO.musicData().songId().toString();
        CompletableFuture<SendResult<String, MusicEventDTO>> future = kafkaTemplate.send(topicName, key, musicEventDTO);
        future.whenComplete(((stringMusicEventDTOSendResult, throwable) -> {
            if(throwable == null){
                logger.info("Message sent successfully!");
                logger.info("Topic: {}, Partition: {}, Offset: {}", stringMusicEventDTOSendResult.getRecordMetadata().topic(),
                        stringMusicEventDTOSendResult.getRecordMetadata().partition(), stringMusicEventDTOSendResult.getRecordMetadata().offset());
            } else{
               logger.warn("Problem to send message! error: {}", throwable.getMessage());
            }
        }));
    }
}

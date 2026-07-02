package com.ms.musicservice.messaging.producer;

import com.ms.dtos.music.MusicEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class MusicProducer {

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
                log.info("Message sent successfully!");
                log.info("Topic: {}, Partition: {}, Offset: {}", stringMusicEventDTOSendResult.getRecordMetadata().topic(),
                        stringMusicEventDTOSendResult.getRecordMetadata().partition(), stringMusicEventDTOSendResult.getRecordMetadata().offset());
            } else{
                log.warn("Problem to send message! error: {}", throwable.getMessage());
            }
        }));
    }
}

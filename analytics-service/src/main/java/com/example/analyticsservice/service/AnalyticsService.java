package com.example.analyticsservice.service;

import com.example.analyticsservice.Entity.AnalyticsEvent;
import com.example.analyticsservice.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    public void save(AnalyticsEvent analyticsEvent){
        analyticsRepository.save(analyticsEvent);
    }
}

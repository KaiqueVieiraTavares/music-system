package com.example.analyticsservice.repository;

import com.example.analyticsservice.Entity.AnalyticsEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends MongoRepository<AnalyticsEvent, String> {
}

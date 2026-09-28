package com.example.microservices.product.config;

import java.util.Map;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LearningConfigInfoContributor implements InfoContributor {

    private final LearningConfigProperties properties;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("learning", Map.of("serviceMessage", properties.getServiceMessage()));
    }
}

package com.tugaskelompok.rumahsehat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointsListener.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        var applicationContext = event.getApplicationContext();
        applicationContext.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods().forEach((key, value) -> LOGGER.info("{} {}", key, value));
    }
}
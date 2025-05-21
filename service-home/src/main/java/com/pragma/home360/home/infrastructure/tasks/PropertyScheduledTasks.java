package com.pragma.home360.home.infrastructure.tasks;

import com.pragma.home360.home.domain.ports.in.PropertyServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;

@Component
public class PropertyScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(PropertyScheduledTasks.class);
    private final PropertyServicePort propertyServicePort;

    public PropertyScheduledTasks(PropertyServicePort propertyServicePort) {
        this.propertyServicePort = propertyServicePort;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updatePendingProperties() {
        log.info(SCHEDULED_TASK_PROPERTY_UPDATE_STARTED);
        try {
            propertyServicePort.processPendingPropertiesToPublish();
            log.info(SCHEDULED_TASK_PROPERTY_UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(SCHEDULED_TASK_PROPERTY_UPDATE_ERROR, e.getMessage(), e);
        }
    }
}

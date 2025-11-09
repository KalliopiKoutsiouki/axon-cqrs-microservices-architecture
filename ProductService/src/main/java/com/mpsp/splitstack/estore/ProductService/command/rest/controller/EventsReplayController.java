package com.mpsp.splitstack.estore.ProductService.command.rest.controller;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/events-replay")
public class EventsReplayController {

    @Autowired
    private EventProcessingConfiguration eventProcessingConfig;

    @PostMapping("/eventProcessor/{processorName}/reset")
    public ResponseEntity<String> resetEventProcessor(@PathVariable String processorName) {
        Optional<TrackingEventProcessor> eventProcessor =  eventProcessingConfig.eventProcessor(processorName, TrackingEventProcessor.class);
        if (eventProcessor.isPresent()){
            eventProcessor.get().shutDown();
            eventProcessor.get().resetTokens();
            eventProcessor.get().start();
            return new ResponseEntity<>(eventProcessor.get().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

}

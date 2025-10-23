package com.runehive.game.world.cronjobs.impl;

import com.runehive.content.ai.LazyAIManager;
import com.runehive.content.ai.OpenAIService;
import com.runehive.game.world.cronjobs.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAIRelayJob extends Job {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenAIRelayJob.class);
    
    public OpenAIRelayJob() {
        super("OpenAI Relay");
    }
    
    @Override
    public void execute() {
        if (!LazyAIManager.isInitialized()) {
            logger.debug("OpenAI services not initialized, skipping relay job");
            return;
        }
        
        OpenAIService service = LazyAIManager.getOpenAIService();
        if (service != null) {
            int activeSessions = service.getActiveSessionCount();
            if (activeSessions > 0) {
                logger.debug("OpenAI Relay Job - Active conversation sessions: {}", activeSessions);
            }
        }
    }
}

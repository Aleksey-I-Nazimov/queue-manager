package org.numamo.lib.test.queue.manager.configuration;

import org.numamo.lib.test.queue.manager.service.api.input.output.InputOutputJmsProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableQueueManager
public class JmsConfig {

    @ConditionalOnMissingBean (InputOutputJmsProvider.class)
    public void ifJmsTemplateWasNotCreated () {
        throw new IllegalStateException("JmsTemplateProvider was not found." +
                " It must be created!");
    }

}

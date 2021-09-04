package org.numamo.lib.test.queue.manager.service.api.input.output;


import java.util.List;

public interface InputOutputJmsConnectionManager {

    void makeJmsTemplateForAllReadySessions();

    void removeJmsSessions(List<String> id);

}

package org.numamo.lib.test.queue.manager.service.impl;

import org.numamo.lib.test.queue.manager.service.api.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


/**
 * The default generator which uses UUID
 *
 * @author Nazimov Aleksey I
 */
public final class IdGeneratorImpl implements IdGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGeneratorImpl.class);

    @Override
    public String next() {
        final String uuid = UUID.randomUUID().toString();
        LOGGER.trace("Requesting ID = {}", uuid);
        return uuid;
    }

}

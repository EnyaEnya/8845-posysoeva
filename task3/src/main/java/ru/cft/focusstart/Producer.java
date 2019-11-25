package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

import static ru.cft.focusstart.Main.PRODUCER_EXECUTION_TIME_MS;

class Producer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private BlockingQueue<Resource> queue;

    Producer(BlockingQueue<Resource> queue) {
        this.queue = queue;
    }

    public void run() {
        log.info("Producer started");
        try {
            while (!Thread.interrupted()) {
                Resource resource = new Resource();
                queue.put(resource);
                log.info("Resource {} has been produced ", resource.getId());
                Thread.sleep(PRODUCER_EXECUTION_TIME_MS);
                log.info("Resource {} has been put to the store ", resource.getId());
            }
        } catch (Exception e) {
            log.info("Producer interrupted");
        }
    }
}

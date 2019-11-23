package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

import static ru.cft.focusstart.Main.CONSUMER_EXECUTION_TIME_MS;

class Consumer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    private BlockingQueue<Resource> queue;

    Consumer(BlockingQueue<Resource> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            log.info("Consumer started");
            while (!Thread.interrupted()) {
                long resourceId = queue.take().getId();
                log.info("Resource {} has been taken from the store", resourceId);
                log.info("Resource {} has been consumed", resourceId);
                Thread.sleep(CONSUMER_EXECUTION_TIME_MS);
            }
        } catch (InterruptedException e) {
            log.info("Consumer interrupted");
        }
    }
}

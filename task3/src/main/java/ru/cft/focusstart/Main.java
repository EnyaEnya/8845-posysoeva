package ru.cft.focusstart;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    private final static int PRODUCER_THREADS = 5;
    private final static int CONSUMER_THREADS = 5;
    private final static int RESOURCE_STORE_CAPACITY = 40;
    final static int PRODUCER_EXECUTION_TIME_MS = 400;
    final static int CONSUMER_EXECUTION_TIME_MS = 500;

    private Main() {
        BlockingQueue<Resource> queue = new LinkedBlockingQueue<>(RESOURCE_STORE_CAPACITY);
        Producer producer = new Producer(queue);
        ExecutorService producerPool = Executors.newFixedThreadPool(PRODUCER_THREADS);
        Consumer consumer = new Consumer(queue);
        ExecutorService consumerPool = Executors.newFixedThreadPool(CONSUMER_THREADS);
        for (int i = 0; i < PRODUCER_THREADS; i++) {
            producerPool.execute(producer);
        }
        for (int i = 0; i < CONSUMER_THREADS; i++) {
            consumerPool.execute(consumer);
        }
    }
    public static void main(String[] args) {
        new Main();
    }
}

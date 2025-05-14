package org.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorDemo {

    public static void main(String[] args) throws Exception {
        // Executor that will run the consumers
        ExecutorService executor = Executors.newCachedThreadPool();

        // The factory for the event
        LongEventFactory factory = new LongEventFactory();

        // Specify the size of the ring buffer, must be power of 2
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(
                factory,
                bufferSize,
                executor,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        // Connect the handlers
        disruptor.handleEventsWith(
                new LongEventHandler("Handler 1"),
                new LongEventHandler("Handler 2")
        );

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l < 10; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            Thread.sleep(100);
        }

        disruptor.shutdown();
        executor.shutdown();
    }

}

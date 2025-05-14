package org.disruptor;

import com.lmax.disruptor.EventHandler;

public class LongEventHandler implements EventHandler<LongEvent> {

    private final String handlerName;

    public LongEventHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println(handlerName + " processed: " + event.getValue());
    }

}

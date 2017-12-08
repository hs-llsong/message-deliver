package hs.ontheroadstore.message.deliver.handle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/29.
 */
public class ProducerExecutorServiceHandlerImpl implements ProducerExecutorServiceHandler {
    private ExecutorService executorService;
    private HandleManager handleManager;
    public ProducerExecutorServiceHandlerImpl(final HandleManager handleManager, int threadPoolSize) {
        this.handleManager = handleManager;
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public void doWork(String topic,String message,JsonCacheHandler handler) {
        executorService.execute(new AliOnsProducerWorker(handleManager,handler,topic,message));
    }

    @Override
    public void release() {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
    }

}

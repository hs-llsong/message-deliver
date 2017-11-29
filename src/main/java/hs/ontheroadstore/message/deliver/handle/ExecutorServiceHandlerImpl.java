package hs.ontheroadstore.message.deliver.handle;

import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 17/11/29.
 */
public class ExecutorServiceHandlerImpl implements ExecutorServiceHandler{
    private ExecutorService executorService;
    private HandleManager handleManager;
    public ExecutorServiceHandlerImpl(final HandleManager handleManager,int threadPoolSize) {
        this.handleManager = handleManager;
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public void doWork(int messageType,String message,JsonCacheHandler handler) {
        Runnable worker = null;
        if(messageType == AppPropertyKeyConst.MESSAGE_TYPE_HEISHI_ALIONS_PUSH) {
            String topic = "";
            worker = new AliOnsProducerWorker(handleManager,handler,topic,message);
        }
        if(worker== null) return;
        executorService.execute(worker);
    }
    
    @Override
    public void release() {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
    }

}

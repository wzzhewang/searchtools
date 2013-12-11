package com.alipay.tools.search.thread;

import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MutiThreadSearchImpl<T> implements MutiThreadSearch<T> {
    private static final Logger logger = LoggerFactory.getLogger(MutiThreadSearchImpl.class);

    private ExecutorService executorService = null;
    private Map<String, MergeResult<T>> result = new ConcurrentHashMap<String, MergeResult<T>>();
    private FutureContext context = new FutureContext<T>();


    public List<T> getResult(String key) {
        MergeResult merge = result.get(key);
        if (merge!=null&& merge.isDone()) {
            return merge.getResult();
        } else {
            return null;
        }
    }

    public MutiThreadSearchImpl() {
        executorService = Executors.newFixedThreadPool(100);
    }


    public void searchContent(final String key, final Callable<T> callable) throws IOException {
        context.addFuture(executorService.submit(callable), key);
    }

    public void reduceResult(String key) {
        List<Future<T>> list = context.getFutureList(key);

        for (Future<T> future : list) {
            outputResultFromFuture(future, key);
        }
        result.get(key).setDone(true);
    }

    private void outputResultFromFuture(Future<T> future, String key) {

        while (true) {
            try {
                synchronized (this) {
                    if (future.isDone() && !future.isCancelled()) {
                        T content = future.get();

                        if (result.get(key) == null) {
                            result.put(key, new MergeResult<T>());
                        }
                        result.get(key).getResult().add(content);

                        break;
                    } else {
                        this.wait(1000);
                    }

                }
            } catch (Exception e) {
                logger.error("œﬂ≥Ã“Ï≥£", e);
            }
        }

    }

    class MergeResult<T> {
        private boolean done = false;
        private List<T> result = new ArrayList<T>();

        boolean isDone() {
            return done;
        }

        void setDone(boolean done) {
            this.done = done;
        }

        List<T> getResult() {
            return result;
        }

        void setResult(List<T> result) {
            this.result = result;
        }
    }


    class FutureContext<T> {
        private Map<String, List<Future<T>>> futureMap = new ConcurrentHashMap<String, List<Future<T>>>();

        public void addFuture(Future<T> future, String key) {
            synchronized (this) {
                if (futureMap.get(key) == null) {
                    futureMap.put(key, new ArrayList<Future<T>>());
                }
            }
            futureMap.get(key).add(future);
        }

        public List<Future<T>> getFutureList(String key) {
            return this.futureMap.get(key);
        }
    }
}

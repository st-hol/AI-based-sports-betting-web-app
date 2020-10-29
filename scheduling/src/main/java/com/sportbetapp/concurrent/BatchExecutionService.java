package com.sportbetapp.concurrent;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class BatchExecutionService {
    private static final Logger LOG = LoggerFactory.getLogger(BatchExecutionService.class);
    private static final int SHUTDOWN_WAIT_TIMEOUT_IN_SEC = 10;

    private BatchExecutionService() {
        //to hide implicit constr.
    }

    public static void execute(Collection<? extends Runnable> tasks, int numThreads, String name,
                               Runnable finalizing) {
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        } else if (tasks.size() == 1 || numThreads <= 1) {
            tasks.iterator().next().run();
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(numThreads, new InheritPriorityThreadFactory(name));
        try {
            CompletableFuture<?>[] futures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(task, executor))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).join();
            executor.shutdown();
            executor.awaitTermination(SHUTDOWN_WAIT_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("Batch process interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            if (!executor.isTerminated()) {
                LOG.error("Cancel non-finished batch tasks");
            }
            executor.shutdownNow();
            if (finalizing != null) {
                finalizing.run();
            }
        }
    }

}

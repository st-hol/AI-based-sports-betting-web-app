package com.sportbetapp.concurrent;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduledTasksExecutionHelper {

    private static final int DEFAULT_THREAD_NUM = 4;

    private final int numThreads;

    public ScheduledTasksExecutionHelper() {
        this(DEFAULT_THREAD_NUM);
    }

    public ScheduledTasksExecutionHelper(int numThreads) {
        this.numThreads = numThreads;
    }

    public void process() {
        List<? extends Runnable> tasks = buildCodesGroupProcessingTasks();
        BatchExecutionService.execute(tasks, numThreads, "name");
    }

    private List<? extends Runnable> buildCodesGroupProcessingTasks() {
        final List<PredictingSportEventScheduledTask> tasks = new LinkedList<>();
        PredictingSportEventScheduledTask currentTask = null;

//        for () {
//                currentTask = new PredictingSportEventScheduledTask();
//                tasks.add(currentTask);
//        }

//        tasks.sort(Comparator.comparing().reversed());
        return tasks;
    }

    private class PredictingSportEventScheduledTask implements Runnable {

        private String message;

        public PredictingSportEventScheduledTask(String message){
            this.message = message;
        }

        @Override
        public void run() {
            System.out.println(new Date()+" Runnable Task with "+message
                    +" on thread "+Thread.currentThread().getName());
        }
    }
}


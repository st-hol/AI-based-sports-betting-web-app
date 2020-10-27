package com.sportbetapp.concurrent;

import java.util.concurrent.ThreadFactory;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

/**
 * Inherit parent thread priority when created children threads.
 * Note: for reusable thread pools, use still need to reassign thread priority,
 * as the same thread may be reused by different jobs with different parent priority
 */
public class InheritPriorityThreadFactory implements ThreadFactory {

    private CustomizableThreadFactory customizableThreadFactory;
    private int parentPriority;

    public InheritPriorityThreadFactory(String prefix) {
        customizableThreadFactory = new CustomizableThreadFactory(prefix);
        parentPriority = Thread.currentThread().getPriority();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread childThread = customizableThreadFactory.newThread(r);
        childThread.setPriority(parentPriority);
        return childThread;
    }
}

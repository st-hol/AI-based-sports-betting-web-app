package com.sportbetapp.concurrent;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

@RunWith(MockitoJUnitRunner.class)
public class BatchExecutionServiceTest {

    private static final String TEST = "test";
    private static final Runnable FINALIZING = () -> System.out.println("finalized");

    @Mock
    private Runnable task;
    @Mock
    private Runnable taskWithException;

    @Test
    public void executeSingleTask() {
        BatchExecutionService.execute(Collections.singletonList(task), 2, TEST, FINALIZING);
        verify(task).run();
    }

    @Test
    public void executeMultiTasks() {
        int tasksNum = 100;
        List<Runnable> tasks = IntStream.rangeClosed(1, tasksNum).mapToObj(i -> task).collect(Collectors.toList());
        BatchExecutionService.execute(tasks, 20, TEST, FINALIZING);
        verify(task, times(tasksNum)).run();
    }

    @Test(expected = CompletionException.class)
    public void throwExceptionWhenTaskFailed() {
        doThrow(new DataIntegrityViolationException(TEST)).when(taskWithException).run();
        List<Runnable> tasks = Arrays.asList(task, task, taskWithException, task, task);
        BatchExecutionService.execute(tasks, 10, TEST, FINALIZING);
    }
}

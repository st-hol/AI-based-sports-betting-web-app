package com.sportbetapp.prediction.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class LearningPredictorTest {

    @Before
    public void setUp() {
    }

    @Test
    public void processLearn_homeShouldLose() {
        LearningPredictor lp = new LearningPredictor();
        List<Boolean> bbbb = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Map<String, String> res = lp.processLearn(0.36, 0.15);
            boolean b = "win".equals(res.get("away"));
            bbbb.add(b);
        }
//        bbbb.forEach(x-> System.out.println(x ? "right" : "wrong"));
        System.out.println(bbbb.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting())));
    }

    @Test
    public void processLearn_awayShouldLose() {
        LearningPredictor lp = new LearningPredictor();
        List<Boolean> bbbb = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Map<String, String> res = lp.processLearn(0.15, 0.36);
            boolean b = "win".equals(res.get("home"));
            bbbb.add(b);
        }
//        bbbb.forEach(x-> System.out.println(x ? "right" : "wrong"));
        System.out.println(bbbb.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting())));
    }
}

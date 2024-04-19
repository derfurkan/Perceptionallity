package de.furkan.perceptionallity.util;

import java.util.function.Supplier;

public class RunnableUtil {

        public static void runTimerCondition(Runnable runnable, int delay, Supplier<Boolean> condition) {
            new Thread(() -> {
                while (condition.get()) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runnable.run();
                }
            }).start();
    }


}

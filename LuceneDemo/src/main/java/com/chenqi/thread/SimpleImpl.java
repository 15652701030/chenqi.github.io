package com.chenqi.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 *  1、每个线程对应的副本变量的生命周期不是有线程决定的，而是共享变量的生命周期决定的
 *  即便线程执行完，只要number变量穿着，线程的副本变量依然存在，存放在number的cacheMap中。
 *  而作为特定线程的副本变量，该变量的生命周期应该由线程决定，线程消亡之后，该变量也应该被回收。
 *  2、多个线程有可能会同时操作cacheMap，需要对cacheMap进行同步处理。
 * @file: SimpleImpl.java project: chenqi.github.io
 * @author: chenqi6
 * @date: 2017/12/6
 */
public class SimpleImpl {

    public static class CustomThreadLocal {
        private Map<Long, Integer> cacheMap = new HashMap<Long, Integer>();

        private int defaultValue ;

        public CustomThreadLocal(int value) {
            defaultValue = value;
        }

        public Integer get() {
            long id = Thread.currentThread().getId();

            if (cacheMap.containsKey(id)) {
                System.out.println("------------id="+id);
                return cacheMap.get(id);
            }
            return defaultValue;
        }

        public void set(int value) {
            long id = Thread.currentThread().getId();
            cacheMap.put(id, value);
        }
    }

    public static class Number {
        private CustomThreadLocal value = new CustomThreadLocal(0);

        public void increase() throws InterruptedException {
            value.set(10);
            // Thread.sleep(10000);
            System.out.println("increase value: " + value.get());
        }

        public void decrease() throws InterruptedException {
            value.set(-10);
            // Thread.sleep(10000);
            System.out.println("decrease value: " + value.get());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Number number = new Number();
        Thread increaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    number.increase();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread decreaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    number.decrease();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        increaseThread.start();
        decreaseThread.start();
    }
}

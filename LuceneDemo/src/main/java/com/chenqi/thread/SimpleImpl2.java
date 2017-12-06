package com.chenqi.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 *  当线程消亡之后，线程中cacheMap也会被回收，存放的副本变量也会被全部回收，并且
 *  cacheMap是线程私有的，不会出现多个线程同时访问一个cacheMap的情况。
 *  java中，ThreadLocal类的实现就是采用这种思想
 * @file: SimpleImpl2.java project: chenqi.github.io
 * @author: chenqi6
 * @date: 2017/12/6
 */
public class SimpleImpl2 {

    public static class CommonThread extends Thread {
        Map<Integer, Integer> cacheMap = new HashMap<Integer, Integer>();
    }

    public static class CustomThreadLocal {
        private int defaultValue;

        public CustomThreadLocal(int value) {
            defaultValue = value;
        }

        public Integer get() {
            Integer id = this.hashCode();
            Map<Integer, Integer> cacheMap = getMap();
            if (cacheMap.containsKey(id)) {
                return cacheMap.get(id);
            }
            return defaultValue;
        }

        public void set(int value) {
            Integer id = this.hashCode();
            Map<Integer, Integer> cacheMap = getMap();
            cacheMap.put(id, value);
        }

        public Map<Integer, Integer> getMap() {
            CommonThread thread = (CommonThread) Thread.currentThread();
            return thread.cacheMap;
        }
    }

    public static class Number {
        private CustomThreadLocal value = new CustomThreadLocal(0);

        public void increase() throws InterruptedException {
            value.set(10);
            Thread.sleep(10);
            System.out.println("increase value: " + value.get());
        }

        public void decrease() throws InterruptedException {
            value.set(-10);
            Thread.sleep(10);
            System.out.println("decrease value: " + value.get());
        }
    }


    public static void main(String[] args) throws InterruptedException {
        final Number number = new Number();
        Thread increaseThread = new CommonThread() {
            @Override
            public void run() {
                try {
                    number.increase();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread decreaseThread = new CommonThread() {
            @Override
            public void run() {
                try {
                    number.decrease();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        increaseThread.start();
        decreaseThread.start();
    }
}

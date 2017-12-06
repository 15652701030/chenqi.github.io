package com.chenqi.thread;

/**
 * @description:
 *  increase线程和decrease线程会操作同一个number中value，那么输出的结果是不可预测的，
 *  因为当前线程修改变量之后但是还没输出的时候，变量有可能被另外一个线程修改.
 *
 * @file: MultiThreadDemo.java project: chenqi.github.io
 * @author: chenqi6
 * @date: 2017/12/6
 */
public class MultiThreadDemo {

    public static class Number {
        private int value = 0;

        public void increase() throws InterruptedException {
            value = 10;
            Thread.sleep(10);
            System.out.println("increase value: " + value);
        }

        public void decrease() throws InterruptedException {
            value = -10;
            Thread.sleep(10);
            System.out.println("decrease value: " + value);
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

package com.chenqi.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @file: AtomicIntegerDemo.java project: chenqi.github.io
 * @description: ${DESCRIPTION}
 * @author: chenqi6
 * @date: 2017/12/6
 */
public class AtomicIntegerDemo {
    public static void main(String[] args) {
        AtomicInteger hashCode = new AtomicInteger();
        int hash_increment = 0x61c88647;
        int size = 16;
        // int size = 32;
        //  int size = 64;
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(hashCode.getAndAdd(hash_increment) & (size - 1));
        }
        System.out.println("original:" + list);
        Collections.sort(list);
        System.out.println("sort:    " + list);
    }
}

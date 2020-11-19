package com.demo.Util;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Timer<T> {
    int num;
    volatile AtomicInteger count;
    List<T> list;

    public Timer(int num, AtomicInteger count, List<T> list) {
        this.num = num;
        this.count = count;
        this.list = list;
    }

    public Timer() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

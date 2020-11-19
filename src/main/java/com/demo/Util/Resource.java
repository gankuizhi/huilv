package com.demo.Util;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Resource<E,T> {
    AtomicInteger cout;//计时器
    public LinkedBlockingQueue<E> resourceQueue;//阻塞队列

    List<T> list;

    public Resource(AtomicInteger cout, LinkedBlockingQueue<E> resourceQueue) {
        this.cout = cout;
        this.resourceQueue = resourceQueue;
    }

    public AtomicInteger getCout() {
        return cout;
    }

    public void setCout(AtomicInteger cout) {
        this.cout = cout;
    }

    public LinkedBlockingQueue<E> getResourceQueue() {
        return resourceQueue;
    }

    public void setResourceQueue(LinkedBlockingQueue<E> resourceQueue) {
        this.resourceQueue = resourceQueue;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

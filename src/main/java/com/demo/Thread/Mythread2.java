package com.demo.Thread;

import com.demo.Util.Resource;
import com.demo.model.Compress;


public class Mythread2 extends Thread{
    public Resource resource;

    public Mythread2(Resource resource) {
        this.resource = resource;
    }
    public void run() {
        while (resource.getResourceQueue().size() > 0) {
            try {
                Compress details = (Compress) resource.getResourceQueue().take();

                resource.getCout().decrementAndGet();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

package com.demo.Thread;

import com.demo.Util.Resource;
import com.demo.model.Details;

public class Mythread1 extends Thread{
    public Resource resource;

    public Mythread1(Resource resource) {
        this.resource = resource;
    }
    public void run() {
        while (resource.getResourceQueue().size() > 0) {
            try {
                Details details = (Details) resource.getResourceQueue().take();
                details.setCash("10000");
                resource.getCout().decrementAndGet();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    }

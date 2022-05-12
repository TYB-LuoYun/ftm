package top.anets.semaphore.res;

import lombok.Data;

import java.time.Instant;
import java.util.Random;

@Data
class WorkerThread extends Thread {
    private Resource resource;
    private SemaphoreWithResource semaphore;

    public WorkerThread(SemaphoreWithResource semaphore) {
        resource = null;
        this.semaphore = semaphore;

    }

    @Override
    public void run() {
        acquire();

        int availablePermits = semaphore.getSemaphore().availablePermits();//当前可用的许可数,空闲资源

        boolean b = semaphore.getSemaphore().hasQueuedThreads();

        int queueLength = semaphore.getSemaphore().getQueueLength();//等待许可的线程数

        System.out.println(b+"=========================总资源: | 活动资源/线程:"+(5-availablePermits) + " | 空闲资源:"+ availablePermits+" | 等待资源的线程"+queueLength);


        int number = resource.getNumber();
        int randomInt = new Random(Instant.now().toEpochMilli()).nextInt(10);
        resource.setNumber(number + randomInt);
        System.out.println("第" + getName() + "线程把资源类" + resource.getName() + "的值从"
                + number + "改为了" + resource.getNumber());
        release();
    }
    private void acquire() {
        if (resource == null) {
            semaphore.acquire(this);
        }
    }
    private void release() {
        semaphore.release(this);
    }
}
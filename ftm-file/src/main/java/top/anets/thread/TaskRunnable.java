package top.anets.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 售票任务
 */
class TaskRunnable extends  Thread{
    volatile boolean stop = false;
    /**
     * 剩余车票数量
     */
    private AtomicInteger count;

    /**
     * 当前任务名称
     */
    private String taskName;

    /**
     * 任务锁
     */
    private ReentrantLock lock;

    private static Object lock2 = new Object();//锁

    public TaskRunnable(AtomicInteger count, String taskName, ReentrantLock lock) {
        this.count = count;
        this.taskName = taskName;
        this.lock = lock;
    }

    @Override
    public void run() {

        System.out.println(taskName + "号售票厅的开始售票~~");
        while (!stop) {
            synchronized (lock){
                this.lock.lock();
                try {
                    lock.notify();
                    Thread.sleep(1000);
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.count.get() <= 0) {
                    //任务结束解锁
//                this.lock.unlock();
//                break;

                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(taskName+"等待售票--");
                }
                System.out.println("恭喜抢到" + taskName+"号销售厅的车票! 当前剩余车票数" + this.count.decrementAndGet());
                this.lock.unlock();

            }

        }
        System.out.println(taskName + "号售票厅的结束=======================================");

    }
}

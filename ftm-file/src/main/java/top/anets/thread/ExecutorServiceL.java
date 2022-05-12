package top.anets.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutorServiceL {

    public static void main(String[] args) {

        //创建线程池

        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                2,
                8,
                TimeUnit.SECONDS,
                new SynchronousQueue(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        //执行任务


//        //总车票数
        AtomicInteger total = new AtomicInteger(10000);
//        //线程锁
        ReentrantLock lock = new ReentrantLock();

        Condition c1 = lock.newCondition();
        Condition c2 = lock.newCondition();
        Condition c3 = lock.newCondition();



        TaskRunnable taskRunnable = new TaskRunnable(total, "100组织", lock);
        executorService.execute(taskRunnable);
        TaskRunnable taskRunnable1 = new TaskRunnable(total, "200组织", lock);
        executorService.execute(taskRunnable1);
        TaskRunnable taskRunnable2 = new TaskRunnable(total, "300组织", lock);
        executorService.execute(taskRunnable2);
        executorService.execute(new TaskRunnable(total, "400组织", lock));
        executorService.execute(new TaskRunnable(total, "500组织", lock));
        executorService.execute(new TaskRunnable(total, "600组织", lock));
        executorService.execute(new TaskRunnable(total, "700组织", lock));
        executorService.execute(new TaskRunnable(total, "800组织", lock));

        System.out.println("提交完成");


        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        while (true) {

            if( total.doubleValue()<0&&total.doubleValue()>-10){
                taskRunnable.stop=true;
                taskRunnable.interrupt();
            }

            if( total.doubleValue()<-20&&total.doubleValue()>-30){
                taskRunnable1.stop=true;
                taskRunnable1.interrupt();
            }

            if( total.doubleValue()<-40&&total.doubleValue()>-50){
                taskRunnable2.stop=true;
                taskRunnable2.interrupt();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int activeCount = threadPoolExecutor.getActiveCount();
            long taskCount = threadPoolExecutor.getTaskCount();
            long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
            long tasksToDo = taskCount - completedTaskCount - activeCount;
            System.out.println( "|||||||||已完成任务("+completedTaskCount+"),"+
                    "总任务("+taskCount+"),"+"活动任务("+activeCount+"),"+"剩余任务("+tasksToDo+")");

            if (((ThreadPoolExecutor) executorService).getActiveCount() <= 0) {
                System.out.println("售票结束~~");
                break;
            }
        }
        try {
            /**
             * 关闭线程池:
             * 1.不在接收新的任务
             * 2.等待已执行任务执行完毕
             * 3.关闭线程池(清除尚未执行任务,试图停止正在执行的任务)
             */
            executorService.shutdown();
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                System.out.println("尚未执行任务列表: "+executorService.shutdownNow());
            }

        } catch (Exception e) {
            System.out.println("超出时间强制结束!");
        }
    }


}
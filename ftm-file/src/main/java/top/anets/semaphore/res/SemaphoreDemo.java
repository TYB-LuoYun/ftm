package top.anets.semaphore.res;

public class SemaphoreDemo {
    public static void main(String[] args) {
        int ThreadCount = 8;
        SemaphoreWithResource semaphoreWithResource = new SemaphoreWithResource();

        //创建8个线程来操作5个资源类
        for (int i = 0; i < ThreadCount; i++) {
            WorkerThread workerThread = new WorkerThread(semaphoreWithResource);
            workerThread.setName(String.valueOf(i));
            workerThread.start();
        }
        System.out.println("main has been dead");
    }

}
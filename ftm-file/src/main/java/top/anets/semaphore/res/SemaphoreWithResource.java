package top.anets.semaphore.res;

import lombok.Data;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

@Data
class SemaphoreWithResource {
    Semaphore semaphore = new Semaphore(5);
    CopyOnWriteArrayList<Resource> resources = new CopyOnWriteArrayList<>();


    public SemaphoreWithResource() {
        //构建5个资源类
        for (int i = 0; i < 5; i++) {
            Resource resource = new Resource(i, String.valueOf(i));
            resources.add(resource);
        }
    }

    //能够同时获得许可和资源
    public  void acquire(WorkerThread workerThread) {
        Resource poll = null;
        try {
            semaphore.acquire();
            poll = resources.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerThread.setResource(poll);
        }
    }

    //能够同时释放许可和资源
    public   void release(WorkerThread workerThread) {
        try {
            semaphore.release();
            resources.add(workerThread.getResource());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerThread.setResource(null);
        }
    }
}

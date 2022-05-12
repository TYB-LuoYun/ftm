package top.anets.file.controller;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {
    public static void main(String[] args) {
        // 所有票源
        ArrayList<Thread> lst = new ArrayList<>();
        lst.add(new Thread(new Tickets2("网上       售票")));
        lst.add(new Thread(new Tickets2("手机APP 售票")));
        lst.add(new Thread(new Tickets2("实地窗口售票")));

        // 随机产生一个客户端类型
        Random rdom = new Random();
        // 设置一个线程池
        ExecutorService es = Executors.newFixedThreadPool(2);
        // 模拟有 15个客户端 来购票
        for (int i = 0; i < 15; i++) {
            int index = rdom.nextInt(3);
            Thread thread = lst.get(index);
            // 购票线程进入线程池
            es.submit(thread);
        }
        // 关闭线程池
        es.shutdown();
    }
}

/**
 *
 * @author lztkdr
 *
 */
class Tickets2 implements Runnable {

    // 安全锁对象
    public static Lock locker = new ReentrantLock();

    // 静态的票总数(固定)
    public static int TicketCount = 10;

    public String name;

    /**
     *
     * @param name
     *            客户端 票源
     */
    public Tickets2(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        // 线程锁 队列式 售票
        locker.lock();
        try {
            // 模拟购票需要200毫秒
            Thread.sleep(200);
            if (TicketCount > 0) {
                System.out.println(this.name + "\t出售1一张，剩余\t " + (--TicketCount));
            } else {
                System.out.println(this.name + "\t沒有抢到票！！！");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }
}
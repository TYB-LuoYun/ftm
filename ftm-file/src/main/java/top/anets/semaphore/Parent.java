package top.anets.semaphore;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class Parent implements Runnable {
    private String name;
    private Semaphore wc;
    private List<Org> orgs;
    public Parent(String name, Semaphore wc, List<Org> orgs){
        this.name=name;
        this.wc=wc;
        this.orgs = orgs;
    }


//简单版本
//3个线程对 5个组织进行开票：申请1,2,3资源
//其中1个组织一直有票开，就占有着1资源一直开
//其中2个组织没有票开，就释放2,3资源，又申请资源
//
//
//
//




    @Override
    public void run() {
        try {


            while (true){
                int queueLength = wc.getQueueLength();
                System.out.println( "等待许可的线程数:"+queueLength);
                // 剩下的资源(剩下的茅坑)
                int availablePermits = wc.availablePermits();
                System.out.println( "可用资源数:"+availablePermits);
                if (availablePermits > 0) {
                    System.out.println(name+":"+orgs.get(availablePermits-1)+"组织尚未开票");
                    System.out.println(name+"申请对"+orgs.get(availablePermits-1)+"组织开票");
                    wc.acquire();
                    System.out.println(name+"对"+orgs.get(availablePermits-1)+"组织申请成功");
                    System.out.println(name+orgs.get(availablePermits-1)+"开完票了");
                } else {
                    System.out.println(name+"没有空闲的组织了");
                    wc.acquire();//等待空闲的组织被释放
                    System.out.println(name+"我又获取到资源了"+availablePermits);
                }

                //申请茅坑 如果资源达到3次，就等待
                Thread.sleep(new Random().nextInt(1000)); // 模拟上厕所时间。
                wc.release();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
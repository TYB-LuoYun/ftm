package top.anets.file.controller;

public class TestTicket {

    public static void main(String[] args) {

        Runnable st = new SellTicket(new Tick());
        new Thread(st, "A").start();
        new Thread(st, "B").start();
        new Thread(st, "C").start();
        new Thread(st, "D").start();
    }

    public static class SellTicket implements Runnable {

        public Tick tick;

        Object mutex = new Object();

        public SellTicket(Tick tick) {
            this.tick = tick;
        }

        public void run() {

            while (tick.getCount() > 0) {
                synchronized(mutex) { //需要有一个锁变量
                    if(tick.getCount() <=0) break; //synchronized之前没锁住其他线程（有可能进入到while等待，当进入后需要重新判断count值是大于0，不然就会变成0或负数）
                    int temp = tick.getCount();
                    System.out.println(Thread.currentThread().getName()
                            + "-----sale" + temp--);

                    tick.setCount(temp);
                }
            }

        }
    }

    public static class Tick {
        private int count = 10;

        private Tick() {
        }

        private static final class lazyhodler {
            public static final Tick INSTANCE = new Tick();
        }

        public static final Tick getInstance() {
            return lazyhodler.INSTANCE;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }
}

package top.anets.file.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author echo
 * @Date 2020/8/28 15:26
 * @Description 买票，假设有5个卖票窗口，有100张票，120个人去买票（多线程）
 */
public class BuyTicket {

    private static int ticket = 0;

    private static int person = 5;

    private static synchronized void buyTicket(int tickets,int person,String thraedName){

    }

    public static void main(String[] arg){

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 1;i<=person;i++){

            synchronized (BuyTicket.class){
                ticket = ticket+1;
                if (ticket > 100){//定义票卖完的条件
                    ticket = 101;
                }
            }
            executorService.execute(new BuyThread(ticket,i));
        }
    }
}

class BuyThread implements Runnable{

    private int ticket;
    private int person;

    public BuyThread(int ticket,int person){
        this.ticket =  ticket;
        this.person = person;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        if (ticket==101){
            System.out.println("票卖完了，第"+person+"人在"+threadName+"窗口买不到票");
        }else {
            System.out.println("第"+person+"人在窗口"+threadName+"买了第"+ticket+"张票");
        }
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}
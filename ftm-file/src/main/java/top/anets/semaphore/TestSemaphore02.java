package top.anets.semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class TestSemaphore02 {
    public static void main(String[] args) {
        // 一个厕所只有3个坑位，但是有10个人来上厕所，那怎么办？假设10的人的编号分别为1-10，并且1号先到厕所，10号最后到厕所。那么1-3号来的时候必然有可用坑位，顺利如厕，4号来的时候需要看看前面3人是否有人出来了，如果有人出来，进去，否则等待。同样的道理，4-10号也需要等待正在上厕所的人出来后才能进去，并且谁先进去这得看等待的人是否有素质，是否能遵守先来先上的规则。

        List<Org> orgs = new ArrayList<>();
        for (int i = 2000; i <=2005; i++) {
            Org org = new Org();org.setOrgId(i+"");
            org.setOrgMachine("00");
            org.setOrgName(i+"");
            orgs.add(org);
        }


        Semaphore semaphore = new Semaphore(orgs.size());//有5个组织

        for (int i = 1; i <=10; i++) {
            Parent parent = new Parent("第"+i+"个线程,",semaphore,orgs);
            new Thread(parent).start();
        }
    }
}

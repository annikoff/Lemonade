package annikoff.lemonade;

import java.lang.Thread;
import annikoff.lemonade.*;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class Dispatcher extends Thread {

    private int maxThreadsCount = 5;
    public int threadsCount = 0;
    final ArrayList<String> list = new ArrayList<String>();

    public Dispatcher(){
        super();
    }

    public void run(){
        String startUrl = "";
        Queue<String> qe = new LinkedList<String>();
        threadsCount = 1;
        Worker worker = new Worker(startUrl, qe, this);
        Thread thread = new Thread(worker);
        worker.run();
        list.add(startUrl);

        while(!qe.isEmpty()) {

            String url = qe.poll();
            if (!list.contains(url)) {
                if (threadsCount < maxThreadsCount) {
                    threadsCount++;
                    new Thread(new Worker(url, qe, this)).start();  
                    list.add(url);              
                }
                try {
                    this.sleep(100);
                }catch (InterruptedException e) {
                  System.out.println(e.getMessage());
                }
            }
        }
    }

}
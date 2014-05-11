package annikoff.lemonade;

import java.lang.Thread;
import annikoff.lemonade.*;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;

public class Dispatcher extends Thread {

    private int maxThreadsCount = 10;
    public int threadsCount = 0;
    final ArrayList<String> list = new ArrayList<String>();
    private String startUrl;

    public Dispatcher(String startUrl){
        super();
        this.startUrl = startUrl;
    }

    public void run(){
        Queue<String> qe = new LinkedList<String>();
        threadsCount = 1;
        Worker worker = new Worker(startUrl, qe, list);
        Thread thread = new Thread(worker);
        worker.run();

        ExecutorService service = Executors.newFixedThreadPool(maxThreadsCount);
        while(!qe.isEmpty()) {
            String url = qe.poll();
            service.submit(new Worker(url, qe, list));
            try {
                this.sleep(100);
            }catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println(qe.size());
        }
    }

}
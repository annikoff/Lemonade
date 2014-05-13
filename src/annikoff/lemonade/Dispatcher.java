package annikoff.lemonade;

import java.lang.Thread;
import annikoff.lemonade.*;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.concurrent.*;

public class Dispatcher extends Thread {

    private int maxThreadsCount = 10;
    public int threadsCount = 0;
    final ArrayList<String> list = new ArrayList<String>();
    final Hashtable<String, Link> hashtable = new Hashtable<String, Link>();
    private Link startUrl;

    public Dispatcher(Link startUrl){
        super();
        this.startUrl = startUrl;
    }

    public void run(){
        Queue<Link> qe = new LinkedList<Link>();
        threadsCount = 1;
        Worker worker = new Worker(startUrl, qe, list, hashtable);
        Thread thread = new Thread(worker);
        worker.run();

        ExecutorService service = Executors.newFixedThreadPool(maxThreadsCount);
        while(!qe.isEmpty()) {
            Link url = qe.poll();
            if (hashtable.get(url.href) == null) {
                service.submit(new Worker(url, qe, list, hashtable));
                try {
                    this.sleep(100);
                }catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            //System.out.println(qe.size());
        }
    }

}
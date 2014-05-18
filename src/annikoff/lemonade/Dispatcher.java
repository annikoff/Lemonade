package annikoff.lemonade;

import java.lang.Thread;
import java.util.Queue;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.concurrent.*;

public class Dispatcher extends Thread {

    private int maxThreadsCount = 10;
    public int threadsCount = 0;
    final Hashtable<String, Link> hashtable = new Hashtable<String, Link>();
    private Link startUrl;
    private boolean doWork = true;
    private ExecutorService service;
    public  Queue<Link> qe = new LinkedList<Link>();
    final Table table;
    final Display display;

    public Dispatcher(Link startUrl, Table table, Display display){
        super();
        this.startUrl = startUrl;
        this.table = table;
        this.display = display;
    }

    @Override
    public void run(){
        threadsCount = 1;
        Worker worker = new Worker(startUrl, qe, hashtable, table, display);
        worker.run();

        service = Executors.newFixedThreadPool(maxThreadsCount);
        while(!qe.isEmpty() && doWork) {
            Link url = qe.poll();
            if (hashtable.get(url.href) == null) {
                service.submit(new Worker(url, qe, hashtable, table, display));
                try {
                    this.sleep(100);
                }catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            //System.out.println(qe.size());
        }
    }

    public void stopWork() {
        doWork = false;
        if (service != null) {
            service.shutdown();
        }
        qe.clear();
    }

}
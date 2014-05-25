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
    private Link startUrl;
    private boolean doWork = true;
    private ExecutorService service;
    public  Queue<Link> qe = new LinkedList<Link>();
    private int delay = 100;
    final Hashtable<String, Link> hashtable = new Hashtable<String, Link>();
    final Table table;
    final Display display;

    public Dispatcher(Link startUrl, Table table, Display display, int maxThreadsCount, int delay){
        super();
        this.startUrl = startUrl;
        this.table = table;
        this.display = display;
        this.maxThreadsCount = maxThreadsCount;
        this.delay = delay;
    }

    @Override
    public void run(){
        Worker worker = new Worker(startUrl, qe, hashtable, table, display);
        worker.run();

        service = Executors.newFixedThreadPool(maxThreadsCount);
        while(!qe.isEmpty()) {
            if (!doWork) {
                return;
            }
            Link url = qe.poll();
            if (hashtable.get(url.href) == null) {
                service.submit(new Worker(url, qe, hashtable, table, display));
                try {
                    this.sleep(this.delay);
                }catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            //System.out.println(qe.size());
        }
        service.shutdownNow();
    }

    public void stopWork() {
        doWork = false;
        if (service != null) {
            service.shutdownNow();
        }
        qe.clear();
    }

}
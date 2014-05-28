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
    private boolean isPaused = false;
    private ExecutorService service;
    public Lemonade lemonade;
    public Queue<Link> qe = new LinkedList<Link>();
    private int delay = 100;
    final Hashtable<String, Link> hashtable = new Hashtable<String, Link>();
    final Table table;
    final Display display;

    public Dispatcher(Table table, Display display){
        super();
        this.table = table;
        this.display = display;
    }

    public void init(Link startUrl, int maxThreadsCount, int delay) {
        this.startUrl = startUrl;
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
            if (isPaused) {
                continue;
            }
            Link url = qe.poll();
            System.out.println(hashtable.get(url.href));
            if (hashtable.get(url.href) == null) {
                service.submit(new Worker(url, qe, hashtable, table, display));
                try {
                    this.sleep(this.delay);
                }catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }else {
                System.out.println(hashtable.entrySet());
            }
        }
        service.shutdownNow();
        throwDone();
    }

    public void stopWork() {
        doWork = false;
        if (service != null) {
            service.shutdownNow();
        }
        qe.clear();
    }

    public void pause(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void addThrowListener(Lemonade lemonade){
        this.lemonade = lemonade;
    }

    public void throwDone() {
        display.syncExec(new Runnable() {
            public void run() {
                lemonade.done();
            }
        });
    }


}
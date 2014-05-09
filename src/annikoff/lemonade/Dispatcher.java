package annikoff.lemonade;

import java.lang.Thread;
import annikoff.lemonade.*;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class Dispatcher extends Thread {

    private int maxThreadsCount = 5;
    public int threadsCount = 0;
    final ArrayList<Link> list = new ArrayList<Link>();

    public Dispatcher(){
        super();
    }

    public void run(){
        Queue<String> qe = new LinkedList<String>();
        threadsCount = 1;
        Worker worker = new Worker("", qe, this);
        worker.run();
        

        while(!qe.isEmpty()) {
            if (threadsCount < maxThreadsCount) {
                threadsCount++;
                new Worker(qe.poll(), qe, this).start();
                System.out.println(threadsCount);
            }
            //
        }
    }

}
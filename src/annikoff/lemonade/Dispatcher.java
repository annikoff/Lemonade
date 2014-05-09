package annikoff.lemonade;

import java.lang.Thread;
import annikoff.lemonade.*;

public class Dispatcher extends Thread {

    private int maxTreadsCount = 5;
    private int treadsCount = 0;

    public Dispatcher(){
        super();
    }

    public void run(){
        Worker worker = new Worker("http://yournewbusiness.ru/");
        worker.start();
    }

}
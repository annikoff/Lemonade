package annikoff.lemonade;

import annikoff.lemonade.*;

public class Lemonade {

    public static void main(String[] args) {
        Thread d = new Dispatcher(args[0]);
        d.start();
    }

}

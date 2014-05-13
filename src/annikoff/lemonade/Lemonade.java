package annikoff.lemonade;

import annikoff.lemonade.*;

public class Lemonade {

    public static void main(String[] args) {
        Link link = new Link(args[0]);
        Thread d = new Dispatcher(link);
        d.start();
    }

}

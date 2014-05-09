package annikoff.lemonade;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import annikoff.lemonade.*;
import java.util.Queue;

public class Worker extends Thread {

    private String urlToParse;
    public Queue<String> qe;
    public ArrayList<Link> list;
    public Dispatcher parent;

    public Worker(String url, Queue<String> qe, Dispatcher parent){
        super();
        this.urlToParse = url;
        this.qe = qe;
        this.parent = parent;
    }

    private String readStreamToString(InputStream in) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while ((line = reader.readLine()) != null) {
          builder.append(line);
          builder.append("\n"); 
      }
      return builder.toString();
  }

  public void run() {
    try {
        URL url = new URL(urlToParse);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setFollowRedirects(false);
        connection.setRequestProperty("User-Agent", "Lemonade");
        System.out.println(connection.getResponseCode() + " " + urlToParse);
          //String html = lemonade.readStreamToString(connection.getInputStream());
        Reader reader = new InputStreamReader(connection.getInputStream());
        LinkCollector lc = new LinkCollector(urlToParse);
        new ParserDelegator().parse(reader, lc, false);
        lc.flush();
        for (Link l: lc.getList()) {
            qe.add(l.href);
        }
    }catch (Exception e) {
      System.out.println(e.getMessage());
    }
    parent.threadsCount--;
    System.out.println("test");
  }
}
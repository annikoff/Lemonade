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

public class Worker implements Runnable {

    private String urlToParse;
    public Queue<String> qe;
    public ArrayList<String> list;

    public Worker(String url, Queue<String> qe, ArrayList<String> list){
        super();
        this.urlToParse = url;
        this.qe = qe;
        this.list = list;
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
       // if (url.toURI().getScheme() == "http") {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setFollowRedirects(false);
            connection.setRequestProperty("User-Agent", "Lemonade");
            System.out.println(connection.getResponseCode() + " " + urlToParse);
              //String html = lemonade.readStreamToString(connection.getInputStream());
            if (connection.getContentType().indexOf("text/html") != -1) {
                Reader reader = new InputStreamReader(connection.getInputStream());
                LinkCollector lc = new LinkCollector(urlToParse);
                new ParserDelegator().parse(reader, lc, false);
                lc.flush();
                synchronized (this) {
                    for (Link l: lc.getList()) {
                        if (!list.contains(l.href) && !qe.contains(l.href) && !l.external) {
                            qe.add(l.href);
                        }
                    }
                }
            }
       // }
    }catch (Exception ex) {
        System.out.println(urlToParse);  
        ex.printStackTrace();
    }
    synchronized (this) {
        list.add(urlToParse);
    }
  }
}
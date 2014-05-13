package annikoff.lemonade;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import annikoff.lemonade.*;
import java.util.Queue;
import java.util.Hashtable;

public class Worker implements Runnable {

    private Link urlToParse;
    public Queue<Link> qe;
    public ArrayList<String> list;
    public Hashtable<String, Link> hashtable;

    public Worker(Link url, Queue<Link> qe, ArrayList<String> list, Hashtable<String, Link> hashtable){
        super();
        this.urlToParse = url;
        this.qe = qe;
        this.list = list;
        this.hashtable = hashtable;
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
            URL url = new URL(urlToParse.href);
            // if (url.toURI().getScheme() == "http") {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setFollowRedirects(false);
                connection.setRequestProperty("User-Agent", "Lemonade");
                try {
                    urlToParse.statusCode = connection.getResponseCode();
                    System.out.println(urlToParse.statusCode + " " + urlToParse.href);
                    //String html = lemonade.readStreamToString(connection.getInputStream());
                    if (connection.getContentType().indexOf("text/html") != -1) {
                        Reader reader = new InputStreamReader(connection.getInputStream());
                        LinkCollector lc = new LinkCollector(urlToParse.href);
                        new ParserDelegator().parse(reader, lc, false);
                        lc.flush();
                        synchronized (this) {
                            for (Link l: lc.getList()) {
                                if (hashtable.get(l.href) == null && !l.external && !qe.contains(lc)) {
                                    qe.add(l);
                                }
                            }
                        }
                    }
                    //}
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } 
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }catch (MalformedURLException ex) {
            System.out.println(ex.getMessage());
        }
        synchronized (this) {
            list.add(urlToParse.href);
            hashtable.put(urlToParse.href, urlToParse);
        }
  }
}
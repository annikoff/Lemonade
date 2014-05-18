package annikoff.lemonade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.net.IDN;
import java.lang.Exception;
import javax.swing.text.html.parser.*;
import java.util.Queue;
import java.util.Hashtable;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Worker implements Runnable {

    private Link urlToParse;
    public Queue<Link> qe;
    public Hashtable<String, Link> hashtable;
    final Table table;
    final Display display;

    public Worker(Link url, Queue<Link> qe, Hashtable<String, Link> hashtable, Table table, Display display){
        super();
        this.urlToParse = url;
        this.qe = qe;
        this.hashtable = hashtable;
        this.table = table;
        this.display = display;
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
            try {
                url = createSafeURI(url).toURL();
            }catch (URISyntaxException ex) {
                ex.getStackTrace();
            }
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
                    urlToParse.errorMessage = ex.getMessage();
                } 
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                urlToParse.errorMessage = ex.getMessage();
            }
        }catch (MalformedURLException ex) {
            System.out.println(ex.getMessage());
            urlToParse.errorMessage = ex.getMessage();
        }
        hashtable.put(urlToParse.href, urlToParse);
        display.syncExec(new Runnable() {
            public void run() {
                if (table.isDisposed())
                    return;
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, Integer.toString(urlToParse.statusCode));
                item.setText(1, urlToParse.href);
            }
        });
    }

    public static URI createSafeURI(final URL url) throws URISyntaxException {

        String path = url.getPath();
        String query = url.getQuery();
        String host = url.getHost();
        try {
            //host = new String(host.getBytes(), "utf-8"); 
            if (path != null) {
                path = URLDecoder.decode(path, "UTF-8");
            }
            if (url.getQuery() != null) {
                query = URLDecoder.decode(url.getQuery(), "UTF-8");
            }
        }catch (UnsupportedEncodingException ex) {
            ex.getStackTrace();
        }
        return new URI(url.getProtocol(), url.getUserInfo(), IDN.toASCII(host), url.getPort(), path, query, url.getRef());    
    }

}
package annikoff.lemonade;

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
import annikoff.lemonade.LinkCollector;

public class Lemonade {

  final ArrayList<String> list = new ArrayList<String>();

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
  

  public static void main(String[] args) throws IOException {
    Lemonade lemonade = new Lemonade();
    URL url = new URL("http://yournewbusiness.ru/");
    try {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setFollowRedirects(false);
      connection.setRequestProperty("User-Agent", "Lemonade");
      System.out.println(connection.getResponseCode());
      //String html = lemonade.readStreamToString(connection.getInputStream());
      Reader reader = new InputStreamReader(connection.getInputStream());
      new ParserDelegator().parse(reader, new LinkCollector(), false);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
 }

}
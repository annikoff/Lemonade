package annikoff.lemonade;

import java.util.Hashtable;

public class Link {

    public String href;
    public String text;
    public String alt;
    public String title;
    public boolean noFollow = false;
    public boolean external = false;
    public int statusCode;
    public String errorMessage;
    //public Hashtable<String, boolean> referrers;

    public Link(String href, String text,  String alt, String title, boolean noFollow) {
        this.href = href;
        this.text = text;
        this.alt = alt;
        this.title = title;
        this.noFollow = noFollow;
    }

    public Link(String href, boolean noFollow) {
        this.href = href;
        this.noFollow = noFollow;
    }

    public Link(String href) {
        this.href = href;
    }

    public Link() {}

   /* public void addReferrer(String referrer, boolean isNofollow) {
        if (referrers.get(referrer) == null) {
            referrers.put(referrer, isNofollow);
        }
    }*/

}
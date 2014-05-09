package annikoff.lemonade;

public class Link {

    public String href;
    public String text;
    public String alt;
    public String title;
    public boolean noFollow = false;
    public boolean external = false;

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
}
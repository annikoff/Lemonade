package annikoff.lemonade;

public class Link {
    public String text;
    public String path;
    public String alt;
    public String title;
    public boolean noFollow;
    public Link(String text, String path, String alt, String title, boolean noFollow) {
        this.text = text;
        this.path = path;
        this.alt = alt;
        this.title = title;
        this.noFollow = noFollow;
    }
    public Link() {
    }
}
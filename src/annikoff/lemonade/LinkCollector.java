package annikoff.lemonade;

import java.net.URL;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.BadLocationException;
import annikoff.lemonade.Link;
import java.util.ArrayList;
import java.net.URI;
import java.lang.String;

public class LinkCollector extends HTMLEditorKit.ParserCallback {

    final ArrayList<Link> list = new ArrayList<Link>();
    public String base = "";
    public String referer = "";

    public LinkCollector(String r) {
        referer = r;
    }

    public ArrayList<Link> getList() {
        return list;
    }

    @Override
    public void handleSimpleTag(Tag tag, MutableAttributeSet attribute, int pos) {
        if (tag == Tag.A) {
            String href = (String) attribute.getAttribute(Attribute.HREF);
            if (href == null) {
                return;
            }
            String rel = (String) attribute.getAttribute(Attribute.REL);
            Boolean noFollow = false;
            if (rel != null) {
                if (rel.equalsIgnoreCase("nofollow")) {
                    noFollow = true;
                }
            }
            list.add(new Link(href, noFollow));
            
        }
        if (tag == Tag.BASE) {
            if (base == "") {
                base = (String) attribute.getAttribute(Attribute.HREF);
            }
        }
    }

    @Override
    public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
        handleSimpleTag(tag, attribute, pos);
    }

    @Override
    public void flush() throws BadLocationException {
        if (base == "") {
            base = referer;
        }
        try {
            URI baseURI = new URI(base);
            for (Link link: list) {
                URI uri = new URI(link.href);
                uri.normalize();
                uri = baseURI.resolve(uri);
                if (!uri.getHost().equalsIgnoreCase(baseURI.getHost())) {
                    link.external = true;
                }
                link.href = uri.toString();
                list.set(list.indexOf(link), link);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

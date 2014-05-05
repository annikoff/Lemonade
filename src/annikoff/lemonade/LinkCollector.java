package annikoff.lemonade;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.BadLocationException;
import annikoff.lemonade.Link;

public class LinkCollector extends HTMLEditorKit.ParserCallback {

    public LinkCollector() {

    }

    public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
        if (tag == Tag.A) {
            String address = (String) attribute.getAttribute(Attribute.HREF);
            System.out.println(address);
        }
    }

}

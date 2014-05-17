package annikoff.lemonade;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

public class Lemonade {

    public static void main (String [] args) {
        Display display = new Display ();
        Shell shell = new Lemonade ().open (display);
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }

    public Shell open (Display display) {
        Shell shell = new Shell (display);
        shell.setText("Lemonade - website scanner");
        shell.setSize(700,300);
        shell.setLayout(new RowLayout());

        Label label = new Label(shell, SWT.LEFT);
        label.setText("URL");

        final Text textUrl = new Text(shell, SWT.BORDER);
        textUrl.setText("http://yournewbusiness.ru/");

        final Button buttonStart = new Button(shell, SWT.PUSH);
        buttonStart.setText("Start");

        buttonStart.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                buttonStart.setText("Stop");
                Link link = new Link(textUrl.getText());
                Thread d = new Dispatcher(link);
                d.start();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION| SWT.CHECK);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        String[] titles = { "Status Code", "URL"};
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(titles[i]);
        }

        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, "200");
        item.setText(1, "http://test/");

        for (int i=0; i<titles.length; i++) {
            table.getColumn (i).pack ();
        }

        table.setSize(table.computeSize(SWT.DEFAULT, 200));

        shell.open ();
        return shell;
    }
}

package annikoff.lemonade;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

public class Lemonade {

    private Dispatcher dispatcher;

    public static void main (String [] args) {
        Display display = new Display ();
        Shell shell = new Lemonade ().open (display);
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }

    public Shell open (final Display display) {
        Shell shell = new Shell (display);
        shell.setText("Lemonade - website scanner");
        shell.setSize(700,300);

        GridLayout gridLayout = new GridLayout(3, false);
        shell.setLayout(gridLayout);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        Label label = new Label(shell, SWT.LEFT);
        label.setText("URL");
        label.setLayoutData(gridData);

        final Text textUrl = new Text(shell, SWT.BORDER);
        
        textUrl.setLayoutData(gridData);

        final Button buttonStart = new Button(shell, SWT.PUSH);
        buttonStart.setText("Start");
        buttonStart.setLayoutData(gridData);

        gridData = new GridData();
        gridData.horizontalSpan = 3;
        gridData.horizontalAlignment = GridData.CENTER;

        final Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION| SWT.CHECK);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        table.setLayoutData(gridData);

        String[] titles = { "Status Code", "URL"};
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(titles[i]);
        }

        for (int i=0; i<titles.length; i++) {
            table.getColumn (i).pack ();
        }

        table.setSize(table.computeSize(500, 200));

        buttonStart.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (dispatcher == null) {
                    buttonStart.setText("Stop");
                    Link link = new Link(textUrl.getText());
                    dispatcher = new Dispatcher(link, table, display);
                    dispatcher.start();
                }else {
                    dispatcher.stopWork();
                    buttonStart.setText("Stopped");
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        shell.open ();
        return shell;
    }
}

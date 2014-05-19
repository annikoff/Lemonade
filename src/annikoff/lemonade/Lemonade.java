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
        Label label = new Label(shell, SWT.LEFT);
        label.setText("URL");
        label.setLayoutData(gridData);

        final Text textUrl = new Text(shell, SWT.BORDER);
        textUrl.setText("http://yournewbusiness.ru/");
        textUrl.setLayoutData(gridData);

        final Button buttonStart = new Button(shell, SWT.PUSH);
        buttonStart.setText("Start");
        buttonStart.setLayoutData(gridData);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;

        final Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        table.setLayoutData(gridData);

        String[][] titles = { {"Status Code", "100"}, {"URL", "500"}};
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setWidth(Integer.parseInt(titles[i][1]));
            column.setText(titles[i][0]);
        }

        for (int i=0; i<titles.length; i++) {
            table.getColumn (i).pack ();
        }

        table.setSize(500, SWT.DEFAULT);


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

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
        shell.setText("Lemonade - Website Scanner");
        shell.setSize(700,300);

        GridLayout gridLayout = new GridLayout(10, false);
        shell.setLayout(gridLayout);

        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 2;
        Label label = new Label(shell, SWT.LEFT);
        label.setText("URL:");
        label.setLayoutData(gridData);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        final Text textUrl = new Text(shell, SWT.BORDER);
        textUrl.setText("http://localhost/");
        textUrl.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 2;
        final Button buttonStart = new Button(shell, SWT.PUSH);
        buttonStart.setText("Start");
        buttonStart.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        final Button buttonStop = new Button(shell, SWT.PUSH);
        buttonStop.setText("Stop");
        buttonStop.setEnabled(false);
        buttonStop.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        Label labelThreads = new Label(shell, SWT.LEFT);
        labelThreads.setText("Threads:");
        labelThreads.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        final Spinner spinnerThreads = new Spinner(shell, SWT.BORDER);
        spinnerThreads.setMinimum(1);
        spinnerThreads.setMaximum(50);
        spinnerThreads.setSelection(5);
        spinnerThreads.setIncrement(1);
        spinnerThreads.setPageIncrement(10);
        spinnerThreads.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        Label labelDelay = new Label(shell, SWT.LEFT);
        labelDelay.setText("Delay:");
        labelDelay.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        final Spinner spinnerDelay = new Spinner(shell, SWT.BORDER);
        spinnerDelay.setMinimum(100);
        spinnerDelay.setMaximum(5000);
        spinnerDelay.setSelection(500);
        spinnerDelay.setIncrement(100);
        spinnerDelay.setPageIncrement(1000);
        spinnerDelay.setLayoutData(gridData);
        
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 10;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;

        final Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(gridData);

        String[][] titles = { {"Status", "100"}, {"URL", "400"}};
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setWidth(Integer.parseInt(titles[i][1]));
            column.setText(titles[i][0]);
        }

        buttonStart.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (dispatcher == null) {
                    buttonStart.setText("Pause");
                    Link link = new Link(textUrl.getText());
                    dispatcher = new Dispatcher(link, table, display, spinnerThreads.getSelection(), spinnerDelay.getSelection());
                    dispatcher.start();
                    spinnerThreads.setEnabled(false);
                    textUrl.setEnabled(false);
                    buttonStop.setEnabled(true);
                }else {
                    dispatcher.stopWork();
                    buttonStart.setText("Start");
                    spinnerThreads.setEnabled(true);
                    textUrl.setEnabled(true);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
        final Label statusLine = new Label(shell, SWT.LEFT);
        statusLine.setText("test");
        shell.open ();
        return shell;
    }
}

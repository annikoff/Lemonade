package annikoff.lemonade;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;

public class Lemonade  {

    private Dispatcher dispatcher;
    private Text textUrl;
    private Button buttonStart;
    private Button buttonStop;
    private Spinner spinnerThreads;
    private Spinner spinnerDelay;
    private Label statusLine;
    public Table table;
    private MessageBox messageBox;

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
        shell.setSize(700,500);
        shell.setImage(new Image(display, "icon.png"));
        messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);

        GridLayout gridLayout = new GridLayout(10, false);
        shell.setLayout(gridLayout);

        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 2;
        Label label = new Label(shell, SWT.LEFT);
        label.setText("URL:");
        label.setLayoutData(gridData);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        textUrl = new Text(shell, SWT.BORDER);
        textUrl.setText("http://localhost/");
        textUrl.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gridData.horizontalSpan = 2;
        buttonStart = new Button(shell, SWT.PUSH);
        buttonStart.setText("Start");
        buttonStart.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        buttonStop = new Button(shell, SWT.PUSH);
        buttonStop.setText("Stop");
        buttonStop.setEnabled(false);
        buttonStop.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        Label labelThreads = new Label(shell, SWT.LEFT);
        labelThreads.setText("Threads:");
        labelThreads.setLayoutData(gridData);

        gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        spinnerThreads = new Spinner(shell, SWT.BORDER);
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
        spinnerDelay = new Spinner(shell, SWT.BORDER);
        spinnerDelay.setMinimum(100);
        spinnerDelay.setMaximum(5000);
        spinnerDelay.setSelection(100);
        spinnerDelay.setIncrement(100);
        spinnerDelay.setPageIncrement(1000);
        spinnerDelay.setLayoutData(gridData);
        
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 10;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;

        table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(gridData);

        String[][] titles = { {"Status", "50"}, {"URL", "400"}};
        for (int i = 0; i < titles.length; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setWidth(Integer.parseInt(titles[i][1]));
            column.setText(titles[i][0]);
        }

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 10;
        gridData.horizontalAlignment = GridData.FILL;
        statusLine = new Label(shell, SWT.BORDER);
        statusLine.setText(" ");
        statusLine.setLayoutData(gridData);

        dispatcher = new Dispatcher(table, display);
        dispatcher.addThrowListener(this);

        buttonStart.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!dispatcher.isAlive()) {
                    table.removeAll();
                    buttonStart.setText("Pause");
                    Link link = new Link(textUrl.getText());
                    dispatcher.init(link, spinnerThreads.getSelection(), spinnerDelay.getSelection());
                    dispatcher.start();
                    spinnerThreads.setEnabled(false);
                    textUrl.setEnabled(false);
                    buttonStop.setEnabled(true);
                    spinnerDelay.setEnabled(false);
                    statusLine.setText("Processing...");
                }else {
                    if (dispatcher.isPaused()) {
                        dispatcher.pause(false);
                        buttonStart.setText("Pause");
                        statusLine.setText("Paused");
                    }else {
                        dispatcher.pause(true);
                        buttonStart.setText("Start");
                        statusLine.setText("Processing...");
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        buttonStop.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dispatcher.stopWork();
                spinnerThreads.setEnabled(true);
                textUrl.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                spinnerDelay.setEnabled(true);
                statusLine.setText("Stopped");
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        shell.open ();
        return shell;
    }

    public void done() {
        statusLine.setText("Done");
        spinnerThreads.setEnabled(true);
        textUrl.setEnabled(true);
        buttonStop.setEnabled(false);
        spinnerDelay.setEnabled(true);
        buttonStart.setText("Start");
        buttonStart.setEnabled(false);
        messageBox.setMessage("Work is done!");
        messageBox.open();
    }

}
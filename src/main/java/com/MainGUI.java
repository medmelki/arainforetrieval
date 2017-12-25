package com;

import com.indexing.IndexingImpl;
import com.search.SearchImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class MainGUI {
    private final JTextArea consoleOutput = new JTextArea();
    private final JTextField textField = new JTextField();
    private static JMenuBar menuBar;

    SwingWorker worker = new SwingWorker<Void, Void>() {
        long totalTime = 0;

        @Override
        public Void doInBackground() {
            long startTime = System.currentTimeMillis();
            IndexingImpl.index();
            long endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            return null;
        }

        @Override
        public void done() {
            consoleOutput.append("Done in " + totalTime / 1000 + " seconds\n");
        }
    };

    public JComponent makeUI() {
        try {
            menuBar = new JMenuBar();
            JMenu indexMenu = new JMenu("Indexing");
            JMenuItem indexItem = new JMenuItem("Start");
            indexMenu.add(indexItem);
            menuBar.add(indexMenu);

            indexItem.addActionListener(e -> {
                consoleOutput.append("\nStart Indexing...");
                worker.execute();
            });
            consoleOutput.setFont(consoleOutput.getFont().deriveFont(18f));
            consoleOutput.setLineWrap(true);
            consoleOutput.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            consoleOutput.setWrapStyleWord(true);
            OutputStream os = new TextAreaOutputStream(consoleOutput);
            System.setOut(new PrintStream(os, true, "UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Box box = Box.createHorizontalBox();
        box.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box.add(Box.createHorizontalGlue());
        box.add(textField);
        box.add(Box.createHorizontalStrut(5));
        JButton jButton = new JButton(new AbstractAction("بحث") {
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleOutput.setText("");
                SearchImpl.search(textField.getText());
//                System.out.println("\n    " + textField.getText());
            }
        });
        jButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        box.add(jButton);
        consoleOutput.setEditable(false);

        JPanel p = new JPanel(new BorderLayout());
        p.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(new JScrollPane(consoleOutput));
        p.add(box, BorderLayout.SOUTH);
        return p;
    }

    public static void main(String... args) {
        EventQueue.invokeLater(MainGUI::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame f = new JFrame("Arabic Info Retrieval");
        f.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.getContentPane().add(new MainGUI().makeUI());
        f.setJMenuBar(menuBar);
        f.setSize(720, 540);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

class TextAreaOutputStream extends OutputStream {
    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();
    private final JTextArea consoleOutput;

    public TextAreaOutputStream(JTextArea consoleOutput) {
        super();
        this.consoleOutput = consoleOutput;
    }

    @Override
    public void flush() throws IOException {
        consoleOutput.append(buf.toString("UTF-8"));
        buf.reset();
    }

    @Override
    public void write(int b) {
        buf.write(b);
    }
}



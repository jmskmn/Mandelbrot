package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsGUI extends JDialog {

    private JTextField widthField, heightField, maxIterationsField, numWorkersField, pixelsPerTaskField, rField, gField, bField;
    
    
    public SettingsGUI(JFrame parent) {
        super(parent, "Settings", true); 
        setSize(350, 400);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
        Main.s.lock.readLock().lock();
        panel.add(new JLabel("Width:"));
        widthField = new JTextField(Integer.toString(Main.s.WIDTH));
        panel.add(widthField);
        panel.add(new JLabel("Height:"));
        heightField = new JTextField(Integer.toString(Main.s.HEIGHT));
        panel.add(heightField);
        panel.add(new JLabel("Number of Workers:"));
        numWorkersField = new JTextField(Integer.toString(Main.s.NUM_WORKERS));
        panel.add(numWorkersField);
        panel.add(new JLabel("Max Iterations:"));
        maxIterationsField = new JTextField(Integer.toString(Main.s.MAX_ITERATIONS));
        panel.add(maxIterationsField);
        panel.add(new JLabel("Pixels Per Task:"));
        pixelsPerTaskField = new JTextField(Integer.toString(Main.s.TASK_PIXELS));
        panel.add(pixelsPerTaskField);
        panel.add(new JLabel("Mandelbrot Set Color"));
        panel.add(new JLabel(""));
        Color c = new Color(Main.s.COLOR);
        panel.add(new JLabel("Red:"));
        rField = new JTextField(Integer.toString(c.getRed()));
        panel.add(rField);

        panel.add(new JLabel("Green:"));
        gField = new JTextField(Integer.toString(c.getGreen()));
        panel.add(gField);
        
        panel.add(new JLabel("Blue:"));
        bField = new JTextField(Integer.toString(c.getBlue()));
        panel.add(bField);
        
        
        
        
        
        
        
        
        Main.s.lock.readLock().unlock();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	Main.s.lock.writeLock().lock();
            	
            	Main.s.MAX_ITERATIONS = Integer.parseInt(maxIterationsField.getText());
            	Main.s.NUM_WORKERS = Integer.parseInt(numWorkersField.getText());
            	Main.s.TASK_PIXELS = Integer.parseInt(pixelsPerTaskField.getText());
            	
            	Main.s.COLOR = (new Color(Integer.parseInt(rField.getText()),Integer.parseInt(gField.getText()),Integer.parseInt(bField.getText()))).getRGB();
            	Main.s.lock.writeLock().unlock();
            	
            	
            	Main.s.lock.readLock().lock();
            	Main.s.saveSettings(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()), Main.s.MAX_ITERATIONS, Main.s.NUM_WORKERS, Main.s.TASK_PIXELS, Main.s.COLOR);
            	Main.s.lock.readLock().unlock();
            	Main.assignTasks(Main.s);
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.add((new JLabel("When changing width, height, or number of workers,")));
        textPanel.add((new JLabel("please restart the program to see effects.")));
        textPanel.add((new JLabel("")));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(textPanel,BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

}
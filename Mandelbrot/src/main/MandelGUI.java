package main;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class MandelGUI extends JFrame {


    //Stores the mandelbrot set graph, displayed onto a JPanel created when the MandelGUI is instantiated.
    private BufferedImage image;
    public static final int HEIGHT = Main.s.HEIGHT;
    public static final int WIDTH = Main.s.WIDTH;
    public MandelGUI() {
        setTitle("Mandelbrot Set");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        
        //Uses a BufferedImage inside of a JPanel where the buffered image gets updated pixel by pixel
        //Idea from https://stackoverflow.com/questions/3325546/how-to-color-a-pixel
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, this);
            }
        };
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //Right click to reset zoom settings
                	Main.s.cameraLock.writeLock().lock();
                	Main.s.ZOOM = 1;
                	Main.s.MOVE_X = -0.5;
                	Main.s.MOVE_Y = 0;
                	Main.s.cameraLock.writeLock().unlock();
                	Main.assignTasks(Main.s);
                } else if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                	//Left click event to center
                	Main.s.cameraLock.readLock().lock();
                    double i = 1.5 * (x - WIDTH / 2) / (0.5 * Main.s.ZOOM * WIDTH) + Main.s.MOVE_X;
                    double j = (y - HEIGHT / 2) / (0.5 * Main.s.ZOOM * HEIGHT) + Main.s.MOVE_Y;
                    Main.s.cameraLock.readLock().unlock();
                    //Reading to perform calculations on how far to zoom.
                    
                    
                    Main.s.cameraLock.writeLock().lock();
                    Main.s.MOVE_X = i;
                    Main.s.MOVE_Y = j;
                    Main.s.cameraLock.writeLock().unlock();
                    Main.assignTasks(Main.s);
                }
            }
        });

        panel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                // gets direction and strength of scroll
                Main.s.cameraLock.writeLock().lock();
                if (notches < 0) {
                	Main.s.ZOOM = Main.s.ZOOM + 0.2 *(Main.s.ZOOM);
                } else {
                	Main.s.ZOOM = Main.s.ZOOM - 0.2 *(Main.s.ZOOM);
                    if (Main.s.ZOOM < 1) {
                    	Main.s.ZOOM = 1;
                    }
                }
                Main.s.cameraLock.writeLock().unlock();
                Main.assignTasks(Main.s);
            }
        });
        

        
        
        
        JLabel legendLabel = new JLabel("Zoom: Scroll          Reset: Right Click          Re-Center: Left Click");
        legendLabel.setFont(new Font("Arial", Font.BOLD, 16));
        legendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        legendLabel.setOpaque(true);
        legendLabel.setBackground(Color.BLACK);
        legendLabel.setForeground(Color.WHITE);
        // Create a JPanel to hold the legend label and image
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(legendLabel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        
        
        /*
         * Sets up button for settings menu.
         */
        MandelGUI ref = this;
        JButton settingsButton = new JButton("Settings");
        //frontend stuff
        
        settingsButton.setOpaque(false);
        settingsButton.setBackground(Color.BLACK); 
        settingsButton.setBorder(new LineBorder(Color.WHITE));
        settingsButton.setForeground(Color.WHITE); 
        
        
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsGUI dialog = new SettingsGUI(ref);
                dialog.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(settingsButton);
        buttonPanel.setBackground(Color.BLACK);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        
        
        
        
        
        
        
        this.pack();
        
    }
    public void color(int x, int y,int rgb) {
    	if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            image.setRGB(x, y, rgb);
            
        }
    }
    

    
}
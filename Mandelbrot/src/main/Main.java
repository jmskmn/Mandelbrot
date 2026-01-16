package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/*
 * Explanation of each class:
 * Coordinate: Store a point on the complex plain with doubles
 * Task: Stores information about tasks to be completed by Workers
 * Main: Handles the initial master thread which creates workers, the gui, and assigns tasks
 * Worker: Handles pulling tasks off queue and completing them
 * MandelGUI: Creates the user interface and handles the internal storage of the current rendering of the mandelbrot set
 * Settings: Stores information about the project that can be changed by the user
 * SettingsGUI: Handles the creation of the gui that lets the user change the settings.
 * 
 */
public class Main {
	public static Settings s = new Settings();
	public static Queue<Task> q;
	public static void main(String[] args) {
		MandelGUI x = new MandelGUI();
        x.setVisible(true);
        x.color(0, 0, Color.BLACK.getRGB());
        x.color(5, 6, Color.BLACK.getRGB());
        x.color(5, 7, Color.BLACK.getRGB());
        x.repaint();
        
        Main.q = new LinkedList<Task>();
        assignTasks(s);//Fills task queue with all pixels to cover the image
        
        
        
        //I have a readlock and a writelock and whenever I access data in the settings object
        //I have to lock the readlock so I don't try to read from it while the user is changing settings
        //At runtime.
        s.lock.readLock().lock();
        ArrayList<Thread> lst = new ArrayList<Thread>(s.NUM_WORKERS);
        for (int i = 0;i<s.NUM_WORKERS;i++) {
        	lst.add(new Thread(new Worker(x, q)));
        	lst.get(i).start();
        }
        s.lock.readLock().unlock();
        
       
        
        
		
		
	}
	
	public static void assignTasks(Settings s) {
		s.lock.readLock().lock();
		synchronized (Main.q) {
			q.clear();//Needs to clear tasks since if user spams a bunch of inputs the task queue can get flooded.
			for (int i = 0;i<s.WIDTH * s.HEIGHT;i+=s.TASK_PIXELS) {
				q.add(new Task(i,s.TASK_PIXELS,s.WIDTH));
				q.notify();
			}
		}
		s.lock.readLock().unlock();
	}

}

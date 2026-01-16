package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Queue;

public class Worker implements Runnable {
	MandelGUI gui;
	Queue<Task> q;
	
	public Worker(MandelGUI i, Queue<Task> queue) {
		this.gui = i;
		this.q = queue;
	}
	

	
	public void run() {
		try {
				while (true) {
				Task t;
				synchronized (q) {
					while (q.isEmpty())//ensures that the q isn't empty even after the thread has been notified
						q.wait();//blocking call that waits till more tasks have been added
					t = q.poll();
				}
				
				for (int i = t.startPixel;i < t.pixels + t.startPixel;i++) {
					int x = i%t.width;
					int y = i/t.width;
					Main.s.lock.readLock().lock();
					int result = calcMandel(turnPixelIntoCoordinates(x,y),Main.s.MAX_ITERATIONS);
					int color = this.scaleColor(Main.s.COLOR, result, Main.s.MAX_ITERATIONS);
					Main.s.lock.readLock().unlock();
					
					
					//Store calculated data inside of image
					synchronized (gui) {
						gui.color(x, y, color);
					}
					

				}
				//After completing task it updates gui
				gui.repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
	
	
	public Coordinate turnPixelIntoCoordinates(int i, int j) {//given function
		Main.s.cameraLock.readLock().lock();
	    double x = 1.5 * (i - Main.s.WIDTH / 2) / (0.5 * Main.s.ZOOM * Main.s.WIDTH) + Main.s.MOVE_X;
	    double y = (j - Main.s.HEIGHT / 2) / (0.5 * Main.s.ZOOM * Main.s.HEIGHT) + Main.s.MOVE_Y;
	    Main.s.cameraLock.readLock().unlock();
	    
	    return new Coordinate(x, y);
	}
	
	public int calcMandel(Coordinate c, int maxIterations) {
	    double x = 0;
	    double y = 0;
	    int iterations = 0;
	    double xtemp;
	    
	    while (x * x + y * y < 4 && iterations < maxIterations) {
	        xtemp = x * x - (y * y) + c.getX();//Real term of (a + bi)^2 is just a^2 - b^2, then we add the constant term
	        y = 2 * x * y + c.getY();//Complex term of (a + bi)^2 is the 2abi term, then we just add
	        x = xtemp;
	        iterations++;
	    }

	    return iterations;
	}
	
	public int scaleColor(int rgb, int iterations, int max_iterations) {
	   	//srgb into normal rgb values
		Color c = new Color(rgb);
	    int red = c.getRed();
	    int green = c.getGreen();
	    int blue = c.getBlue();

	    // scale the color based on how many iterations were taken
	    red = (int) (red * ((double) iterations / max_iterations));
	    green = (int) (green * ((double) iterations / max_iterations));
	    blue = (int) (blue * ((double) iterations / max_iterations));
	    
	    // back into srgb format.
	    c = new Color(red, green, blue);
	    return c.getRGB();
	}
	
	
	

}

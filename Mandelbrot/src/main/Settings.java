package main;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Settings {
	public final ReadWriteLock lock = new ReentrantReadWriteLock();
	//Used for reader/writer exclusive control over MAX_ITERATIONS, NUM_WORKERS, and TASK_PIXELS
	
	public final ReadWriteLock cameraLock = new ReentrantReadWriteLock();
	//Used for reader/writer exclusive control over MOVE_X, MOVE_Y, and ZOOM
	
	//Number of pixels in the GUI
	//I don't use a lock for these variables because there's no functionality for them to change while running.
    public int WIDTH;
    public int HEIGHT;
    
    //Controls grid scaling for the mandelbrot set graph
    public double MOVE_X;
    public double MOVE_Y;
    public double ZOOM;
    
    //Calculation variables
    public int MAX_ITERATIONS;
    public int NUM_WORKERS;
    public int TASK_PIXELS; //Pixels per task
    
    public int COLOR;

    
    public Settings() {

    	Properties properties = new Properties();
        try (FileReader reader = new FileReader("settings.properties")) {
            properties.load(reader);
            WIDTH = Integer.parseInt(properties.getProperty("WIDTH"));
            HEIGHT = Integer.parseInt(properties.getProperty("HEIGHT"));
            MAX_ITERATIONS = Integer.parseInt(properties.getProperty("MAX_ITERATIONS"));
            NUM_WORKERS = Integer.parseInt(properties.getProperty("NUM_WORKERS"));
            TASK_PIXELS = Integer.parseInt(properties.getProperty("TASK_PIXELS"));
            COLOR = Integer.parseInt(properties.getProperty("COLOR"));
            MOVE_X = -0.5;
        	MOVE_Y = 0;
        	ZOOM = 1;

        } catch (IOException e) {
        	e.printStackTrace();
            System.out.println("Something was wrong with the settings file, using default settings instead.");
            WIDTH = 800;
        	HEIGHT = 400;
        	MOVE_X = -0.5;
        	MOVE_Y = 0;
        	ZOOM = 1;
        	MAX_ITERATIONS = 1000;
        	NUM_WORKERS = 8;
        	TASK_PIXELS = 32;
        	COLOR = Color.BLUE.getRGB();
        }
    	
    	
    	
    	
    }
    
    public static void saveSettings(int WIDTH, int HEIGHT, int MAX_ITERATIONS, int NUM_WORKERS, int TASK_PIXELS, int COLOR) {
    	Properties properties = new Properties();
        try (FileWriter writer = new FileWriter("settings.properties")) {
            properties.setProperty("WIDTH", Integer.toString(WIDTH));
            properties.setProperty("HEIGHT", Integer.toString(HEIGHT));
            properties.setProperty("MAX_ITERATIONS", Integer.toString(MAX_ITERATIONS));
            properties.setProperty("NUM_WORKERS", Integer.toString(NUM_WORKERS));
            properties.setProperty("TASK_PIXELS", Integer.toString(TASK_PIXELS));
            properties.setProperty("COLOR", Integer.toString(COLOR));
            properties.store(writer, "Settings for the Mandelbrot Set Project");
        } catch (IOException e) {
        	System.out.println("There was a problem with writing to the file.");
            e.printStackTrace();
        }
    }
}

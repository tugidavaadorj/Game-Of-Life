import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JPanel;



public class GameOfLife extends JPanel implements Runnable {
	public static final int WIDTH = 1400, HEIGHT = 800;
	private static final int SIZE = 20;
	private Thread thread;
	private boolean running;
	private int speed;
	private ArrayList<AliveCell> aliveCells;
	private int[][] allCells;
	
	public GameOfLife() {
		aliveCells = new ArrayList<AliveCell>();
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		allCells = new int[HEIGHT/SIZE][WIDTH/SIZE];
		repaint();
		speed = 250000000;
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		for(int i = 0; i < WIDTH/SIZE; i++) {
			g.drawLine(i*SIZE, 0, i*SIZE, HEIGHT);
		}
		for(int i = 0; i < HEIGHT/SIZE; i++) {
			g.drawLine(0, i*SIZE, WIDTH, i*SIZE);
		}
		for (int i = 0; i < aliveCells.size(); i++) {
			aliveCells.get(i).draw(g);
		}
	}
	
	public void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	public void stop() {
		//running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	   * This take an input from a file that has the initial state.
	   * The initial alive cells are given by their x and y coordinates
	   * This assumes that the all the coordinates are valid and are within the graph
	   */
	public void importState(String filename) {
		Scanner scan;
        try {
            scan = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file.");
            return;
        }
        while (scan.hasNext()) {
            int xcoord = scan.nextInt();
            int ycoord = scan.nextInt();
            allCells[ycoord][xcoord] = 1;
            AliveCell b = new AliveCell(xcoord, ycoord, SIZE);
			aliveCells.add(b);
        }
        //System.out.println(Arrays.deepToString(allCells).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
        repaint();
        start();
	}
	
	
	/**
	 * This method creates a new random game where the initial alive cells are
	 * randomly placed based on input of probability 
	 * 
	 * @param probability has to be a number between 0 and 1.0
	 * **/
	public void randomGame(double probability) {
		
		for(int i=0; i<allCells.length; i++) {
			  for(int j=0; j<allCells[0].length; j++) {
				if (Math.random() <= probability) {
					allCells[i][j] = 1;
			        AliveCell b = new AliveCell(j, i, SIZE);
					aliveCells.add(b);
				}
			}
		}
		repaint();
        start();
	}
	
	
  /**
   * An iteration of the state. This method will apply the 
   * rules of the Game of Life to the current state.
   * The rules are: 
   * 1. Any dead cell with three neighbors becomes alive
   * 2. Live cell that doesn't have 2 or 3 neighbors dies
   *
   * Instead of checking every possible location, I am only going to look
   * at alive cells and their surrounding neighbors and at each spot apply the rule
   */
	public void GameRules() { 
		//check for neighbors
		Set<Point2D> curCells = new HashSet<Point2D> ();
		Set<Point2D> deadCells = new HashSet<Point2D> ();
		int[][] newCells = new int[HEIGHT/SIZE][WIDTH/SIZE];
		ArrayList<AliveCell> newAliveCells = new ArrayList<AliveCell>();
		
		for (int i = 0; i < aliveCells.size(); i++) {
			AliveCell curCell = aliveCells.get(i);
			//Get Neighbors
			for (int x = -1; x < 2 ; x++) {
				if (curCell.getxCoor()+ x < 0 || curCell.getxCoor()+ x > WIDTH/SIZE - 1) {
					continue;
				}
				for (int y = -1; y< 2; y++) {
					if(curCell.getyCoor()+ y < 0 || curCell.getyCoor()+ y > HEIGHT/SIZE - 1) {
						continue;
					}
					Point2D.Double curCoord = new Point2D.Double(curCell.getxCoor() + x, curCell.getyCoor() + y);
					if (allCells[curCell.getyCoor() + y][curCell.getxCoor() + x] == 1) {
						curCells.add(curCoord);
					} else {
						deadCells.add(curCoord);
					}
				}
			}
		} 
		
		//First rule of Game of Life: Any dead cell with three neighbors becomes alive
		for (Point2D coord: deadCells) {
			int counter = 0;
			for (int x = -1; x < 2 ; x++) {
				if (coord.getX()+ x < 0 || coord.getX()+ x > WIDTH/SIZE - 1) {
					continue;
				}
				for (int y = -1; y< 2; y++) {
					if(coord.getY()+ y < 0 || coord.getY()+ y > HEIGHT/SIZE - 1) {
						continue;
					}
					//Don't want to consider current spot
					if(x == 0 && y == 0) {
						continue;
					}
					if (allCells[(int) (coord.getY() + y)][(int) (coord.getX() + x)] == 1) {
						counter++;
					}
					
				}
			}
			//Dead Cell comes alive
			if (counter == 3) {
				int x = (int)coord.getX();
				int y = (int)coord.getY();
				newCells[y][x] = 1;
				AliveCell b = new AliveCell(x, y, SIZE);
				newAliveCells.add(b);
			}
		}
		
		//Second rule of Game of Life: Live cell that doesn't have 2 or 3 neighbors dies
		for (Point2D coord: curCells) {
			int counter = 0;
			for (int x = -1; x < 2 ; x++) {
				if (coord.getX()+ x < 0 || coord.getX()+ x > WIDTH/SIZE - 1) {
					continue;
				}
				for (int y = -1; y< 2; y++) {
					if(coord.getY()+ y < 0 || coord.getY()+ y > HEIGHT/SIZE - 1) {
						continue;
					}
					if(x == 0 && y == 0) {
						continue;
					}
					if (allCells[(int) (coord.getY() + y)][(int) (coord.getX() + x)] == 1) {
						counter++;
					}
				}
			}
			//Alive Cell stays alive
			if (counter == 2 || counter == 3) {
				int x = (int)coord.getX();
				int y = (int)coord.getY();
				newCells[y][x] = 1;
				AliveCell b = new AliveCell(x, y, SIZE);
				newAliveCells.add(b);
			}
		}
		
		//replace the old 2D grid with the new one
		for(int i=0; i<allCells.length; i++)
			  for(int j=0; j<allCells[i].length; j++)
				  allCells[i][j] = newCells[i][j];
		
		
		//replace Alive Cell arraylist with the new one
		aliveCells.clear();
		aliveCells.addAll(newAliveCells);
		
	}
	@Override
	public void run() {
		int val = 0;
		while (running) {
			if (val > speed) {
				GameRules();
				repaint();
				val = 0;
			}
			val++;
		}
		
	}
}


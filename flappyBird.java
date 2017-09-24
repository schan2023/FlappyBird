package FlappyBird;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;


//ActionListener is an interface
public class flappyBird implements ActionListener, MouseListener, KeyListener {
	
	//Static flappyBird variable(can access static from anywhere)
	public static flappyBird flappyBird;
	
	//Size of window
	public final int WIDTH = 800, HEIGHT = 800;
	
	//Renderer
	public Renderer renderer;
	
	//Create rectangles
	public Rectangle bird;
	
	//Bird movement
	public int ticks, yMotion, score;
	
	//Determines if game is over or started
	public boolean gameOver, started;
	
	//ArrayList to hold rectangles
	public ArrayList<Rectangle> columns;
	
	public Random rand;
	
	//Constructor
	public flappyBird() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);
		
		renderer = new Renderer();
		rand = new Random();
		
		jframe.add(renderer);
		jframe.setTitle("Flappy Bird");
		jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);
		
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		
		//Adds columns into window
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
		
	}
	
	public void addColumn(boolean start) {
		int space = 300;
		int width = 100;
		//Min height is 50 and max height is 300
		int height = 50 + rand.nextInt(300);
		
		//If it is a starting column
		if(start) {
			//Creates columns to add into game
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0 , width, HEIGHT - height - space));
		}
		else {
			//Gets last column from columns array list if it is not starting column
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0 , width, HEIGHT - height - space));
		}
	}
	
	//Creates the columns
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	//Controls mouse movement
	public void jump() {
		if(gameOver) {
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
			
			//Adds columns into window
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		
		if(!started) {
			started = true;
		}
		else if(!gameOver) {
			if(yMotion > 0) {
				yMotion = 0;
			}
			yMotion -= 10;
		}
	}
	
	//Actions performed in game
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Determines how fast columns are moving
		int speed = 5;
		ticks++;
		
		if(started) {
		
			//For each column created till the end, move columns in x direction
			for(int i = 0; i< columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			
			if(ticks % 2 == 0 && (yMotion < 15)) {
				yMotion += 2;
			}
			
			for(int i = 0; i< columns.size(); i++) {
				Rectangle column = columns.get(i);
				if(column.x + column.width < 0) {
					columns.remove(column);
					//If it is top column, then add another column
					if(column.y == 0) {
						addColumn(false); //false -> not the starting column
					}
				}
			}
		
			//Makes bird rectangle move up and down
			bird.y += yMotion;
			
			//Check for collision-> if bird rectangle touches columns
			for(Rectangle column : columns) {
				//Increase score if it is in middle of columns
				if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - speed && bird.x + bird.width / 2 < column.x + column.width / 2 + speed) {
					score++;
				}
				if(column.intersects(bird)) {
					if(column.intersects(bird)) {
						gameOver = true;
						bird.x = column.x - bird.width;
					}
				}
			}
			//Game over if bird touches top or if bird touches grass
			if(bird.y > HEIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}
			if(bird.y + yMotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
			}
			
		}
		
		renderer.repaint();
	}
	
	//Creates graphics of game
	public void repaint(Graphics g) {
		//Color of whole window
		g.setColor(Color.cyan.brighter().brighter().brighter().brighter().brighter().brighter());
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Color of ground
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);
		
		//Grass
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);
		
		//Bird image
		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		//Paints columns for each column in arrayList
		for(Rectangle column : columns) {
			paintColumn(g, column);
		}
		
		//Screen when game is over
		g.setColor(Color.white); //Color of font
		g.setFont(new Font("Arial", 1, 100));
		if(!started) {
			g.drawString("Click to start!", 75, HEIGHT/2 - 50);
		}
		if(gameOver) {
			g.drawString("Game Over!", 100, HEIGHT/2 - 50);
		}
		if(!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
		
		
		
	}
	
	public static void main(String[] args) {
		//Creates new flappyBird object instance
		flappyBird = new flappyBird();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}	
}



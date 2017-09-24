package FlappyBird;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel {
	
	private static final long serialVersionsUID = 1L;
	
	//Passes graphics into flappy bird
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		flappyBird.flappyBird.repaint(g);
		
	}
}

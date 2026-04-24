package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyBoardInput;
import inputs.MouseInput;

import static main.Game.*;
public class GamePanel extends JPanel{
	private Game game;
	private MouseInput mouseInput;
	
	public GamePanel(Game game) {
		this.game = game;
		setPanelSize();
		mouseInput = new MouseInput(this);
		addKeyListener(new KeyBoardInput(this));
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);	
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
		setPreferredSize(size);
		System.out.println("size: "+GAME_WIDTH+" : "+ GAME_HEIGHT);
	}
	public void updateGame() {
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}
	public Game getGame() {
		return game;
	}
}


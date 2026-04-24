package main;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
	private JFrame jframe;
	public GameWindow(GamePanel gamePanel) {
		
		JFrame jframe = new JFrame();
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(jframe);
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLoss();
			}
		});
	
	} 
}

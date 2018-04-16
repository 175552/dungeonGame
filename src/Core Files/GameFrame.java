import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.*;

public class GameFrame extends JFrame implements ActionListener{

	static int x = 1600, y = 1000;
	GamePanels panel;
	JButton gameButton, mapButton;

	GameFrame(){
		setLayout(null);
		setSize(x, y);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		gameButton = new JButton("Start Game");
		mapButton = new JButton("Open Map Maker");

		gameButton.setBounds(600, 200, 400, 200);
		gameButton.addActionListener(this);

		mapButton.setBounds(600, 500, 400, 200);
		mapButton.addActionListener(this);

		add(gameButton);
		add(mapButton);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		String test = e.getActionCommand();
		System.out.println(test);
		if(test.equals("Open Map Maker")){
			panel = new CreatorPanel();
		}
		else if (test.equals("Start Game")){
			panel = new DungeonGamePanel();
		}
		gameButton.setVisible(false);
		mapButton.setVisible(false);
		add(panel);
		setContentPane(panel);
		pack();
		panel.setFocusable(true);
		panel.requestFocusInWindow();
		repaint();
	}

	static int getXSize(){
		return x;
	}
	static int getYSize(){
		return y;
	}
}

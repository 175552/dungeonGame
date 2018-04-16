import javax.swing.JPanel;
import java.awt.Dimension;

abstract class GamePanels extends JPanel {
	void createPanel(){
		setLayout(null);
		setLocation(0, 0);
		setPreferredSize(new Dimension(GameFrame.getXSize(), GameFrame.getYSize()));
	}
}

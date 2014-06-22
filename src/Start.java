import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Start {

	public static void main(String[] args) {

		JFrame window = new JFrame("2048");
		Game game1 = new Game(1);
		Game game2 = new Game(2);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		JOptionPane.showMessageDialog(window, "勝利條件和原版相同 (達到2048)，但有兩個新增規則"
				+ "\n \"REVERSE\" 隨機的上下左右相反事件"
				+ "\n \"BLOCK-BOUNCE\" 兩人分數出現差距，高者遊戲盤會出現方塊阻撓" + "\n 開心玩吧!");
		window.setLayout(new GridLayout(1, 2));
		window.add(game2);
		window.add(game1);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		game1.start();
		game2.start();
	}

}
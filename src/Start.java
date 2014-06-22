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
		JOptionPane.showMessageDialog(window, "�ӧQ����M�쪩�ۦP (�F��2048)�A������ӷs�W�W�h"
				+ "\n \"REVERSE\" �H�����W�U���k�ۤϨƥ�"
				+ "\n \"BLOCK-BOUNCE\" ��H���ƥX�{�t�Z�A���̹C���L�|�X�{�������" + "\n �}�ߪ��a!");
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
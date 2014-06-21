import java.awt.event.KeyEvent;


public class Keyboard {
	public static boolean[] pressed = new boolean[256];
	public static boolean[] prev = new boolean[256];
	public Keyboard(){};

	public static void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}

	public static void keyreleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}

	public static boolean typed(int keyEvent) {
		return !pressed[keyEvent] && prev[keyEvent];
	}

}

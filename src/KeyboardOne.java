import java.awt.event.KeyEvent;

public class KeyboardOne extends Keyboard {

	public static boolean[] pressed = Keyboard.pressed;
	public static boolean[] prev = Keyboard.prev;

	private KeyboardOne() {
		super();
	}

	public static void update() {
		for (int i = 0; i < 5; i++) {
			switch (i) {

			case 0:
				prev[KeyEvent.VK_LEFT] = pressed[KeyEvent.VK_LEFT];
				break;
			case 1:
				prev[KeyEvent.VK_RIGHT] = pressed[KeyEvent.VK_RIGHT];
				break;
			case 2:
				prev[KeyEvent.VK_UP] = pressed[KeyEvent.VK_UP];
				break;
			case 3:
				prev[KeyEvent.VK_DOWN] = pressed[KeyEvent.VK_DOWN];
				break;
			case 4:
				prev[KeyEvent.VK_SPACE] = pressed[KeyEvent.VK_SPACE];
				break;

			}
		}
	}
}

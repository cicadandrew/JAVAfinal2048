import java.awt.event.KeyEvent;

public class KeyboardOne extends Keyboard{

	public static boolean[] pressed = Keyboard.pressed;
	public static boolean[] prev = Keyboard.prev;

	private KeyboardOne() {
		super();
	}

	public static void update() {
		for (int i = 0; i < 6; i++) {
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

			case 5:
				prev[KeyEvent.VK_R] = pressed[KeyEvent.VK_R];
				break;
			case 6:
				prev[KeyEvent.VK_W] = pressed[KeyEvent.VK_W];
				break;
			case 7:
				prev[KeyEvent.VK_A] = pressed[KeyEvent.VK_A];
				break;
			case 8:
				prev[KeyEvent.VK_S] = pressed[KeyEvent.VK_S];
				break;
			case 9:
				prev[KeyEvent.VK_D] = pressed[KeyEvent.VK_D];
				break;
			}
		}
	}
}

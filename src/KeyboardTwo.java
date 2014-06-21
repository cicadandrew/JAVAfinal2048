import java.awt.event.KeyEvent;

public class KeyboardTwo extends Keyboard{
	public static boolean[] pressed = Keyboard.pressed;
	public static boolean[] prev = Keyboard.prev;

	private KeyboardTwo() {
		super();
	}

	public static void update() {
		for (int i = 0; i < 4; i++) {
			switch (i) {

			case 0:
				prev[KeyEvent.VK_W] = pressed[KeyEvent.VK_W];
				break;
			case 1:
				prev[KeyEvent.VK_A] = pressed[KeyEvent.VK_A];
				break;
			case 2:
				prev[KeyEvent.VK_S] = pressed[KeyEvent.VK_S];
				break;
			case 3:
				prev[KeyEvent.VK_D] = pressed[KeyEvent.VK_D];
				break;
			}
		}
	}
}

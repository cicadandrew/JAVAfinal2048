import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tile {

	public static final int WIDTH = 80;
	public static final int HEIGHT = 80;
	public static final int SLIDE_SPEED = 30;
	public static final int ARC_WIDTH = 15;
	public static final int ARC_HEIGHT = 15;

	private int value;
	private BufferedImage tileImage;
	private Color background;
	private Color text;
	private Font font;
	private Point slideTo;
	private int x;
	private int y;

	private boolean beginningAnime = true;
	private double scaleFirst = 0.1;
	private BufferedImage beginningImage;

	private boolean combineAnime = false;
	private double scaleCombine = 1.3;
	private BufferedImage combineImage;
	private boolean canCombine = true;

	public Tile(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
		setSlideTo(new Point(x, y));
		tileImage = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		beginningImage = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		combineImage = new BufferedImage(WIDTH * 2, HEIGHT * 2,
				BufferedImage.TYPE_INT_ARGB);
		drawImage();
	}

	private void drawImage() {
		Graphics2D g = (Graphics2D) tileImage.getGraphics();
		switch (this.value) {
		case 0:
			background = new Color(0xFCFF19);
			break;
		case 2:
			background = new Color(0xe9e9e9);
			text = new Color(0x000000);
			break;
		case 4:
			background = new Color(0xe6daab);
			text = new Color(0x000000);
			break;
		case 8:
			background = new Color(0xf79d3d);
			text = new Color(0xffffff);
			break;
		case 16:
			background = new Color(0xf28007);
			text = new Color(0xffffff);
			break;
		case 32:
			background = new Color(0xf55e3b);
			text = new Color(0xffffff);
			break;
		case 64:
			background = new Color(0xff0000);
			text = new Color(0xffffff);
			break;
		case 128:
			background = new Color(0xe9de84);
			text = new Color(0xffffff);
			break;
		case 256:
			background = new Color(0xf6e873);
			text = new Color(0xffffff);
			break;
		case 512:
			background = new Color(0xf5e455);
			text = new Color(0xffffff);
			break;
		case 1024:
			background = new Color(0xf7e12c);
			text = new Color(0xffffff);
			break;
		case 2048:
			background = new Color(0xffe400);
			text = new Color(0xffffff);
			break;
		default:
			background = Color.darkGray;
		}

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(background);
		g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

		if (value <= 0)
			return;

		g.setColor(text);

		if (value <= 64) {
			font = Game.main.deriveFont(36f);
		} else {
			font = Game.main;
		}

		g.setFont(font);

		int drawX = WIDTH / 2 - DrawUtils.getMessageWidth("" + value, font, g)
				/ 2;
		int drawY = HEIGHT / 2
				+ DrawUtils.getMessageHeight("" + value, font, g) / 2;
		g.drawString("" + value, drawX, drawY);

		g.dispose();

	}

	public void update() {
		if (beginningAnime) {

			AffineTransform transform = new AffineTransform();
			transform.translate(WIDTH / 2 * (1 - scaleFirst), HEIGHT / 2
					* (1 - scaleFirst));
			transform.scale(scaleFirst, scaleFirst);
			Graphics2D g2d = (Graphics2D) beginningImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setColor(new Color(0, 0, 0, 0));
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			g2d.drawImage(tileImage, transform, null);
			scaleFirst += 0.1;
			g2d.dispose();
			if (scaleFirst >= 1)
				beginningAnime = false;

		} else if (combineAnime) {

			AffineTransform transform = new AffineTransform();
			transform.translate(WIDTH / 2 * (1 - scaleCombine), HEIGHT / 2
					* (1 - scaleCombine));
			transform.scale(scaleCombine, scaleCombine);
			Graphics2D g2d = (Graphics2D) combineImage.getGraphics();//
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setColor(new Color(0, 0, 0, 0));
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			g2d.drawImage(tileImage, transform, null);
			scaleCombine -= 0.05;
			g2d.dispose();
			if (scaleCombine <= 1)
				combineAnime = false;

		}

	}

	public boolean isCombineAnime() {
		return combineAnime;
	}

	public void setCombineAnime(boolean combineAnime) {
		this.combineAnime = combineAnime;
	}

	public void render(Graphics2D g) {
		if (beginningAnime)
			g.drawImage(beginningImage, x, y, null);
		else if (combineAnime)
			g.drawImage(combineImage,
					(int) (x + WIDTH / 2 * (1 - scaleCombine)),
					(int) (y + HEIGHT / 2 * (1 - scaleCombine)), null);
		else
			g.drawImage(tileImage, x, y, null);

	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
		drawImage();

	}

	public boolean canCombine() {
		return canCombine;
	}

	public void setCanCombine(boolean canCombine) {
		this.canCombine = canCombine;
	}

	public Point getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Point slideTo) {
		this.slideTo = slideTo;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

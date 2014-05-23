import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Tile{

	public static final int WIDTH = 80;
	public static final int HEIGHT = 80;
	public static final int SLIDR_SPEED = 20;
	public static final int ARC_WIDTH = 15;
	public static final int ARC_HEIGHT = 15;

	private int value;
	private BufferedImage tileimage;
	private Color background;
	private Color text;
	private Font font;
	private Point slideTo;
	private int x;
	private int y;
	
	private boolean canCombine;

	public Tile(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
		setSlideTo(new Point(x, y));
		tileimage = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = tileimage.getGraphics();
//		paint(g);
		drawImage();
	}
	
	

//	@Override
//	public void paint(Graphics g) {
//		// TODO Auto-generated method stub
//		super.paint(g);
//		switch (value) {
//		case 2:
//			background = new Color(0xe9e9e9);
//			text = new Color(0x000000);
//			break;
//		case 4:
//			background = new Color(0xe6daab);
//			text = new Color(0x000000);
//			break;
//		case 8:
//			background = new Color(0xf79d3d);
//			text = new Color(0xffffff);
//			break;
//		case 16:
//			background = new Color(0xf28007);
//			text = new Color(0xffffff);
//			break;
//		case 32:
//			background = new Color(0xf55e3b);
//			text = new Color(0xffffff);
//			break;
//		case 64:
//			background = new Color(0xff0000);
//			text = new Color(0xffffff);
//			break;
//		case 128:
//			background = new Color(0xe9de84);
//			text = new Color(0xffffff);
//			break;
//		case 256:
//			background = new Color(0xf6e873);
//			text = new Color(0xffffff);
//			break;
//		case 512:
//			background = new Color(0xf5e455);
//			text = new Color(0xffffff);
//			break;
//		case 1024:
//			background = new Color(0xf7e12c);
//			text = new Color(0xffffff);
//			break;
//		case 2048:
//			background = new Color(0xffe400);
//			text = new Color(0xffffff);
//			break;
//		default:
//			background = Color.black;
//			text = Color.white;
//		}
//
//		g.setColor(new Color(0, 0, 0, 0));
//		g.fillRect(0, 0, WIDTH, HEIGHT);
//
//
//		g.setColor(background);
//		g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
//
//		g.setColor(text);
//
//		if (value <= 64) {
//			font = Game.main.deriveFont(36f);
//		} else {
//			font = Game.main;
//		}
//		g.setFont(font);
//
//		int drawX = WIDTH / 2 - DrawUtils.getMessageWidth("" + value, font, (Graphics2D) g)
//				/ 2;
//		int drawY = HEIGHT / 2
//				- DrawUtils.getMessageHeight("" + value, font, (Graphics2D) g) / 2;
//		g.drawString("" + value, drawX, drawY);
//		this.render((Graphics2D) g);
//		g.dispose();
//	}



	private void drawImage() {
		Graphics2D g = (Graphics2D) tileimage.getGraphics();
		switch (value) {
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
			background = Color.black;
			text = Color.white;
		}

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);


		g.setColor(background);
		g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

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
				- DrawUtils.getMessageHeight("" + value, font, g) / 2;
		g.drawString("" + value, drawX, drawY);
		this.render(g);
		g.dispose();

	}
	
	public void update(){
		
	}
	
	public void render(Graphics2D g){
		g.drawImage(tileimage, x,y,null);
		
	}
	
	public int getValue(){
		return this.value;
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
}

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;

public class GameBoardOne {

	public static final int ROWS = 4;
	public static final int COLS = 4;

	private final int startingTiles = 2;
	private Tile[][] board;
	private Tile block;
	private static boolean dead;
	private static boolean win;

	private BufferedImage gameBoard;
	private BufferedImage finalBoard;
	private int x;
	private int y;
	private static int score = 0;
	private int highScore = 0;
	private Font scoreFont;

	private static int SPACING = 10;
	public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.WIDTH;
	public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.HEIGHT;

	private static long elapsedMS;
	private long fastestMS;
	private static long startTime;
	private static long pauseTime;
	private static long restartTime = 0;
	private static int pauseCount = 0;
	private int marqueeTime = 1000;
	private boolean hasStarted;
	private boolean reverse;
	private boolean blockBoo;
	private boolean crash;
	private int crashChek;
	private int marqueeCheck1;
	private int marqueeCheck2;
	private int reverseCheck = 0;
	private int blockCheck = 0;
	private static String formattedTime = "00:00:000";
	private String overTime;
	private long finalGrade;

	// Saving
	private String saveDataPathone;
	private String fileName = "SaveDatatwo";
	private int timeBonus;
	private int BraveBonus;

	public GameBoardOne(int x, int y) {
		try {
			// saveDataPathone = GameBoardOne.class.getProtectionDomain()
			// .getCodeSource().getLocation().toURI().getPath();
			saveDataPathone = System.getProperty("user.home")
					+ "\\workspace\\2048finalJAVA";
		} catch (Exception e) {
			e.printStackTrace();
		}

		scoreFont = Game.main.deriveFont(24f);
		this.x = x;
		this.y = y;
		board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		startTime = System.nanoTime();

		loadHighScore();
		createBoardImage();
		start();

	}

	private void createSaveData() {
		try {
			File file = new File(saveDataPathone, fileName);

			FileWriter output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);

			writer.write("" + 0);
			writer.newLine();
			writer.write("" + Integer.MAX_VALUE);
			writer.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void loadHighScore() {

		try {
			File f = new File(saveDataPathone, fileName);
			if (!f.isFile()) {
				createSaveData();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			highScore = Integer.parseInt(reader.readLine());
			fastestMS = Long.parseLong(reader.readLine());
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setHighScore() {

		FileWriter output = null;

		try {
			File f = new File(saveDataPathone, fileName);
			output = new FileWriter(f);
			BufferedWriter writer = new BufferedWriter(output);

			writer.write("" + highScore);
			writer.newLine();
			if (elapsedMS <= fastestMS && win) {
				writer.write("" + elapsedMS);
			} else {
				writer.write("" + fastestMS);
			}
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createBoardImage() {

		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(Color.lightGray);

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int x = SPACING * (col + 1) + Tile.WIDTH * col;
				int y = SPACING * (row + 1) + Tile.HEIGHT * row;
				g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH,
						Tile.ARC_HEIGHT);

			}
		}
	}

	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
		g2d.drawImage(gameBoard, 0, 0, null);

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if (current == null)
					continue;
				current.render(g2d);

			}
		}

		g.drawImage(finalBoard, x, y, null);
		g2d.dispose();

		g.setColor(Color.lightGray);
		g.setFont(scoreFont);
		g.drawString("" + score, 30, 40);
		g.setColor(Color.red);
		g.drawString(
				"Best: " + highScore,
				Game.WIDTH
						- DrawUtils.getMessageWidth("Best: " + highScore,
								scoreFont, g) - 20, 34);

		g.setColor(Color.black);
		g.drawString("Time: " + formattedTime, 30, 75);
		g.setColor(Color.red);
		g.drawString(
				"Fastes: " + formateTime(fastestMS),
				Game.WIDTH
						- DrawUtils.getMessageWidth("Fastes: "
								+ formateTime(fastestMS), scoreFont, g) - 20,
				75);

		// Marquee
		// Start
		if (hasStarted && elapsedMS < 1000) {
			Marquee("START", g, 0);
		}

		// Block will come out randomly if rival's score is less than yours 100
		if (this.getScore() - GameBoardTwo.getScore() > 100)
			blockBoo = true;

		if (blockBoo) {
			if (blockCheck == 0) {
				marqueeCheck1 = (int) elapsedMS;
				blockCheck++;
				blockSpawn();
			} else if (blockCheck == 1
					&& elapsedMS - marqueeCheck1 > marqueeTime * 10) {
				marqueeCheck1 = (int) elapsedMS;
				blockCheck++;
				blockSpawn();
			}
			Marquee("BLOCK!", g, marqueeCheck1);
			blockBoo = false;
		}

		// Direction will be reversed if time % this.score = 7,
		// in 3 sec.
		Random random = new Random();
		if (this.elapsedMS % (this.score  + ((57+random.nextInt(10))*(random.nextInt(10)+1))) == 9)
			reverse = true;

		if (reverse && blockBoo || crashChek == 1) {
			crash = true;
			crashChek++;
		}

		if (crash) {
			marqueeCheck2 = +500;
			crashChek--;
			crash = false;
		}

		if (reverse) {
			if (reverseCheck == 0) {
				marqueeCheck2 = (int) elapsedMS;
				reverseCheck++;
			} else if (reverseCheck == 1
					&& elapsedMS - marqueeCheck2 > marqueeTime * 25) {
				marqueeCheck2 = (int) elapsedMS;
				reverseCheck++;
			} else if (reverseCheck == 1
					&& elapsedMS - marqueeCheck2 > marqueeTime * 40) {
				marqueeCheck2 = (int) elapsedMS;
				reverseCheck++;
			}

			Marquee("REVERSE!", g, marqueeCheck2);
			System.out.println(true);
			if (elapsedMS - marqueeCheck2 > marqueeTime * 3)
				reverse = false;
		}

		// Win, Dead and Game over
		if (!win || !dead) {
			g.setColor(Color.red);
			g.drawString("SPEC. to pause/ start", 30, 85 + DrawUtils
					.getMessageHeight("SPEC. to pause/ start",
							Game.main.deriveFont(15f), g));
		}

		if (pauseCount % 2 == 0 && pauseCount != 0) {

			g.setColor(new Color(0x00BE61));
			g.drawString(
					"P A U S E",
					Game.WIDTH
							/ 2
							- DrawUtils.getMessageWidth("P A U S E",
									Game.main.deriveFont(50f), g) / 2,
					BOARD_HEIGHT
							- DrawUtils.getMessageHeight("P A U S E",
									Game.main.deriveFont(50f), g) - 10);
		}
		if (!GameBoardOne.isWin() && !GameBoardOne.isDead()) {
			if (win && !dead) {
				g.setColor(new Color(0x006BDC));
				g.drawString(
						"You Win",
						Game.WIDTH
								/ 2
								- DrawUtils.getMessageWidth("You Win",
										Game.main.deriveFont(50f), g) / 2,
						BOARD_HEIGHT
								- DrawUtils.getMessageHeight("You Win",
										Game.main.deriveFont(50f), g) - 10);
			}

			else if (dead && win) {
				g.setColor(new Color(0x006BDC));
				g.drawString(
						"Game Over",
						Game.WIDTH
								/ 2
								- DrawUtils.getMessageWidth("You Win",
										Game.main.deriveFont(50f), g) / 2,
						BOARD_HEIGHT
								- DrawUtils.getMessageHeight("You Win",
										Game.main.deriveFont(50f), g) - 10);
			} else if (!win && dead) {
				g.setColor(new Color(0x006BDC));
				g.drawString(
						"You Lose",
						Game.WIDTH
								/ 2
								- DrawUtils.getMessageWidth("You Win",
										Game.main.deriveFont(50f), g) / 2,
						BOARD_HEIGHT
								- DrawUtils.getMessageHeight("You Win",
										Game.main.deriveFont(50f), g) - 10);
			}
		} else if (win || dead) {
			g.setColor(new Color(0xA00200));
			g.drawString(
					"GRADE",
					Game.WIDTH
							/ 2
							- DrawUtils.getMessageWidth("GRADE",
									Game.main.deriveFont(70f), g) / 2,
					BOARD_HEIGHT
							- DrawUtils.getMessageHeight("GRADE",
									Game.main.deriveFont(70f), g) - 100);
			g.drawString(
					"SCORE: " + this.score,
					Game.WIDTH
							/ 2
							- DrawUtils.getMessageWidth("SCORE: " + this.score,
									Game.main.deriveFont(42f), g) / 2,
					BOARD_HEIGHT
							- DrawUtils.getMessageHeight(
									"SCORE: " + this.score,
									Game.main.deriveFont(42f), g) - 50);
			g.drawString(
					"Final: " + this.finalGrade,
					Game.WIDTH
							/ 2
							- DrawUtils.getMessageWidth("Final: "
									+ this.finalGrade,
									Game.main.deriveFont(42f), g) / 2,
					BOARD_HEIGHT
							- DrawUtils.getMessageHeight("Final: "
									+ this.finalGrade,
									Game.main.deriveFont(42f), g) + 50);
		}

	}

	private void start() {
		for (int i = 0; i < startingTiles; i++) {
			spawnRandom();
		}

	}

	private void blockSpawn() {
		Random random = new Random();
		boolean notValid = true;

		while (notValid) {
			int location = random.nextInt(COLS * ROWS);
			int row = location / ROWS;
			int col = location % COLS;
			block = new Tile(-5, getTileX(col), getTileY(row));
			board[row][col] = block;

			notValid = false;
		}

	}

	private void spawnRandom() {
		Random random = new Random();
		boolean notValid = true;

		while (notValid) {
			int location = random.nextInt(COLS * ROWS);
			int row = location / ROWS;
			int col = location % COLS;
			Tile current = board[row][col];
			if (current == null) {
				int check = random.nextInt(301);
				if (check < 270) {
					int value = 2;
					Tile tile = new Tile(value, getTileX(col), getTileY(row));
					board[row][col] = tile;
					notValid = false;
				} else if (check < 300) {
					int value = 4;
					Tile tile = new Tile(value, getTileX(col), getTileY(row));
					board[row][col] = tile;
					notValid = false;
				} else {
					int value = 0;
					Tile tile = new Tile(value, getTileX(col), getTileY(row));
					board[row][col] = tile;
					notValid = false;
				}
			}
		}

	}

	void update() {

		if (!GameBoardTwo.isDead() && !GameBoardTwo.isWin()) {

			if (!win && !dead) {
				if (hasStarted) {

					elapsedMS = (System.nanoTime() - startTime) / 1000000;
					formattedTime = formateTime(elapsedMS);

				} else {
					startTime = System.nanoTime();
				}
			}
		} else
			formattedTime = GameBoardTwo.getOverTime();

		checkKeys();

		if (!GameBoardTwo.isDead() && !GameBoardTwo.isWin()) {

			if (!win && !dead) {
				if (pauseCount % 2 == 0) {

					elapsedMS = pauseTime / 1000000;

				} else if (restartTime != 0) {
					elapsedMS = (System.nanoTime() - (restartTime - pauseTime) - startTime) / 1000000;
				}

				formattedTime = formateTime(elapsedMS);
			}
		} else
			formattedTime = GameBoardTwo.getOverTime();

		if (score >= highScore) {
			highScore = score;
		}

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if (current == null)
					continue;
				current.update();
				resetPosition(current, row, col);
				if (current.getValue() == 2048) {
					win = true;
				}
			}
		}
	}

	private void resetPosition(Tile current, int row, int col) {
		if (current == null)
			return;

		int x = getTileX(col);
		int y = getTileY(row);

		int disX = current.getX() - x;
		int disY = current.getY() - y;

		if (Math.abs(disX) < Tile.SLIDE_SPEED) {
			current.setX(current.getX() - disX);
		}

		if (Math.abs(disY) < Tile.SLIDE_SPEED) {
			current.setY(current.getY() - disY);
		}

		if (disX < 0) {
			current.setX(current.getX() + Tile.SLIDE_SPEED);
		}

		if (disY < 0) {
			current.setY(current.getY() + Tile.SLIDE_SPEED);
		}
		if (disX > 0) {
			current.setX(current.getX() - Tile.SLIDE_SPEED);
		}
		if (disY > 0) {
			current.setY(current.getY() - Tile.SLIDE_SPEED);
		}

	}

	private void Marquee(String marquee, Graphics2D g, int marqueeStart) {

		g.setColor(new Color(0xBD211F));
		int marqueeLength = DrawUtils.getMessageWidth(marquee,
				Game.main.deriveFont(50f), g);
		int marqueeWidth = DrawUtils.getMessageHeight(marquee,
				Game.main.deriveFont(50f), g);

		if (elapsedMS - marqueeStart < marqueeTime) {
			if (elapsedMS - marqueeStart < marqueeTime * 0.3)
				g.drawString(marquee,
						(int) ((Game.WIDTH - marqueeLength)
								* (elapsedMS - marqueeStart)
								/ (marqueeTime * 0.3) - marqueeLength),
						BOARD_HEIGHT - marqueeWidth - 10);
			else if ((elapsedMS - marqueeStart) < marqueeTime * 0.7)
				g.drawString(marquee, (Game.WIDTH - marqueeLength) / 2,
						BOARD_HEIGHT - marqueeWidth - 10);
			else
				g.drawString(marquee, (int) ((Game.WIDTH - marqueeLength)
						* ((elapsedMS - marqueeStart) - marqueeTime * 0.4)
						/ (marqueeTime * 0.3) - marqueeLength), BOARD_HEIGHT
						- marqueeWidth - 10);
		}
	}

	private String formateTime(long mills) {
		String formattedTime;

		String hourFormat = "";
		int hours = (int) (mills / 3600000);
		if (hours >= 1) {
			mills -= hours * 3600000;
			if (hours >= 10) {
				hourFormat = "" + hours;
			} else {
				hourFormat = "0" + hours;
			}
			hourFormat += ":";
		}

		String minuteFormat = "";
		int minutes = (int) (mills / 60000);
		if (minutes >= 1) {
			mills -= minutes * 60000;
			if (minutes >= 10) {
				minuteFormat = "" + minutes;
			} else {
				minuteFormat = "0" + minutes;
			}
		} else {
			minuteFormat = "00";
		}

		String secondFormat = "";
		int seconds = (int) (mills / 1000);
		if (seconds >= 1) {
			mills -= seconds * 1000;
			if (seconds >= 10) {
				secondFormat = "" + seconds;
			} else {
				secondFormat = "0" + seconds;
			}
		} else {
			secondFormat = "00";
		}

		String millFormat = "";
		if (mills > 99) {
			millFormat = "" + mills;
		} else if (mills > 9) {
			millFormat = "0" + mills;
		} else {
			millFormat = "00" + mills;
		}

		formattedTime = hourFormat + minuteFormat + ":" + secondFormat + ":"
				+ millFormat;
		return formattedTime;

	}

	private void checkKeys() {

		if (KeyboardOne.typed(KeyEvent.VK_LEFT)) {
			if (pauseCount % 2 == 0)
				return;

			if (reverse)
				moveTiles(Direction.RIGHT);
			else
				moveTiles(Direction.LEFT);
		}
		if (KeyboardOne.typed(KeyEvent.VK_RIGHT)) {
			if (pauseCount % 2 == 0)
				return;
			if (reverse)
				moveTiles(Direction.LEFT);
			else
				moveTiles(Direction.RIGHT);
		}
		if (KeyboardOne.typed(KeyEvent.VK_UP)) {
			if (pauseCount % 2 == 0)
				return;
			if (reverse)
				moveTiles(Direction.DOWN);
			else
				moveTiles(Direction.UP);
		}
		if (KeyboardOne.typed(KeyEvent.VK_DOWN)) {
			if (pauseCount % 2 == 0)
				return;
			if (reverse)
				moveTiles(Direction.UP);
			else
				moveTiles(Direction.DOWN);
		}

		if (KeyboardOne.typed(KeyEvent.VK_SPACE)) {
			if (!hasStarted) {
				pauseCount = 0;
				hasStarted = true;
			}
			checkPause();
		}
	}

	private void moveTiles(Direction direction) {
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;

		switch (direction) {
		case LEFT:
			horizontalDirection = -1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
			break;

		case RIGHT:
			horizontalDirection = 1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = COLS - 1; col >= 0; col--) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
			break;

		// 2 2 4 8 ---> 0 4 4 8
		// 2 2 4 8 ---> 0 0 0 16
		// 16 0 0 0 <--- 2 2 4 8

		case UP:
			verticalDirection = -1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
			break;

		case DOWN:
			verticalDirection = 1;
			for (int row = ROWS - 1; row >= 0; row--) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection,
								verticalDirection, direction);
					} else
						move(row, col, horizontalDirection, verticalDirection,
								direction);
				}
			}
			break;

		default:
			System.out.println(direction + " is not a valid direction!");
			break;
		}

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if (current == null || current.getValue() < 0)
					continue;
				current.setCanCombine(true);
			}
		}

		if (canMove) {
			spawnRandom();
			checkDead();
		}
	}

	private void checkDead() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (board[row][col] == null)
					return;
				if (checkSurroundingTiles(row, col, board[row][col])) {
					return;
				}
			}
		}

		dead = true;
		overTime = GameBoardOne.getFormattedTime();

		if (win)
			timeBonus = 500;
		if (GameBoardTwo.isDead())
			timeBonus += 500;
		else
			BraveBonus = (score - GameBoardOne.getScore()) * 15 + 500;

		finalGrade = (long) (GameBoardOne.getElapsedMS() * 0.5) / 36000000
				+ timeBonus + BraveBonus + (long) (score * 1.5)
				+ (long) (blockCheck * 10);
		setHighScore();

	}

	void checkPause() {
		pauseCount++;

		if (pauseCount % 2 == 0) {

			pauseTime = (System.nanoTime() - (restartTime - pauseTime) - startTime);

		} else
			restartTime = (System.nanoTime() - startTime);

		if (restartTime != 0 && pauseCount % 2 == 1)
			System.out
					.println(formateTime((restartTime - pauseTime) / 1000000));

	}

	public static int getPauseCount() {
		return pauseCount;
	}

	private boolean checkSurroundingTiles(int row, int col, Tile current) {
		if (row > 0) {
			Tile check = board[row - 1][col];
			if (check == null)
				return true;
			else if ((current.getValue() == check.getValue()
					|| check.getValue() == 0 || current.getValue() == 0)
					&& check.getValue() >= 0)
				return true;
		}
		if (row < ROWS - 1) {
			Tile check = board[row + 1][col];
			if (check == null)
				return true;
			else if ((current.getValue() == check.getValue()
					|| check.getValue() == 0 || current.getValue() == 0)
					&& check.getValue() >= 0)
				return true;
		}
		if (col > 0) {
			Tile check = board[row][col - 1];
			if (check == null)
				return true;
			else if ((current.getValue() == check.getValue()
					|| check.getValue() == 0 || current.getValue() == 0)
					&& check.getValue() >= 0)
				return true;
		}
		if (col < COLS - 1) {
			Tile check = board[row][col + 1];
			if (check == null)
				return true;
			else if ((current.getValue() == check.getValue()
					|| check.getValue() == 0 || current.getValue() == 0)
					&& check.getValue() >= 0)
				return true;
		}
		return false;
	}

	private boolean move(int row, int col, int horizontalDir, int verticalDir,
			Direction dir) {

		boolean canMove = false;

		Tile current = board[row][col];
		if (current == null || current.getValue() < 0)
			return false;

		boolean move = true;
		int newCol = col;
		int newRow = row;

		while (move) {
			newCol += horizontalDir;
			newRow += verticalDir;
			if (checkOutOfBounds(dir, newRow, newCol))
				break;
			if ((board[newRow][newCol] != null && board[newRow][newCol]
					.getValue() < 0)
					|| (current != null && current.getValue() < 0))
				break;

			if (board[newRow][newCol] == null) {

				board[newRow][newCol] = current;
				board[newRow - verticalDir][newCol - horizontalDir] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				canMove = true;
			} else if ((board[newRow][newCol].getValue() == current.getValue()
					|| board[newRow][newCol].getValue() == 0 || current
					.getValue() == 0)
					&& board[newRow][newCol].canCombine()
					&& (current.getValue() >= 0 || board[newRow][newCol]
							.getValue() >= 0)) {

				board[newRow][newCol].setCanCombine(false);
				if (board[newRow][newCol].getValue() != 0) {
					board[newRow][newCol].setValue(board[newRow][newCol]
							.getValue() * 2);
					canMove = true;
					board[newRow - verticalDir][newCol - horizontalDir] = null;
					board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
					board[newRow][newCol].setCombineAnime(true);
					score += board[newRow][newCol].getValue();
				} else if (current.getValue() != 0) {
					board[newRow][newCol].setValue(current.getValue() * 2);
					canMove = true;
					board[newRow - verticalDir][newCol - horizontalDir] = null;
					board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
					board[newRow][newCol].setCombineAnime(true);
					score += board[newRow][newCol].getValue();
				} else if (current.getValue() == 0
						|| board[newRow][newCol].getValue() == 0) {
					board[newRow][newCol].setValue(64);
					canMove = true;
					board[newRow - verticalDir][newCol - horizontalDir] = null;
					board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
					board[newRow][newCol].setCombineAnime(true);
					score += board[newRow][newCol].getValue();
				}

			} else {
				move = false;
			}
		}
		return canMove;
	}

	private boolean checkOutOfBounds(Direction dir, int row, int col) {
		switch (dir) {
		case LEFT:
			return col < 0;

		case RIGHT:
			return col > COLS - 1;

		case UP:
			return row < 0;

		case DOWN:
			return row > ROWS - 1;

		default:
			return false;
		}

	}

	public static int getScore() {
		return score;
	}

	public static long getElapsedMS() {
		return elapsedMS;
	}

	public static String getFormattedTime() {
		return formattedTime;
	}

	public int getTileY(int row) {
		return SPACING * (1 + row) + row * Tile.HEIGHT;
	}

	public int getTileX(int col) {
		return SPACING * (1 + col) + col * Tile.WIDTH;
	}

	public static boolean isDead() {
		return dead;
	}

	public static boolean isWin() {
		return win;
	}

}

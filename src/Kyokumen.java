import java.util.*;
import java.io.*;

public class Kyokumen {
	static final int NONE = 0;
	static final int BLACK = 1;
	static final int WHITE = 2;
	static final int OUT = 3;
	static final int[] DIR = { -10, -9, 1, 11, 10, 9, -1, -11 };

	private int[] box = new int[100];
	private int nextTurn;

	private int rx, ry;
	private int pass;

	static Random random = new Random();

	public Kyokumen() {
		init();
	}

	public Kyokumen(Kyokumen k) {
		this();
		for (int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				box[y * 10 + x] = k.getState(x - 1, y - 1);
			}
		}
		nextTurn = k.which();
	}

	public void init() {
		for (int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				box[y * 10 + x] = NONE;
			}
		}
		for (int i = 0; i < 10; i++) {
			box[i * 10] = OUT;
			box[i * 10 + 9] = OUT;
			box[i] = OUT;
			box[i * 9 + i] = OUT;
		}
		box[44] = WHITE;
		box[54] = BLACK;
		box[45] = BLACK;
		box[55] = WHITE;
		nextTurn = BLACK;

		rx = 3;
		ry = 3;
	}

	int getState(int x, int y) {
		return box[y * 10 + x + 11];
	}

	// 矢印のX座標を返す
	public int getRx() {
		return rx;
	}

	// 矢印のY座標を返す
	public int getRy() {
		return ry;
	}

	int[][] getStone() {
		int[][] stone = new int[8][8];
		for (int i = 7; i >= 0; i--) {
			for (int j = 7; j >= 0; j--) {
				stone[i][j] = box[j * 10 + i + 11];
			}
		}
		return stone;
	}

	int getPass() {
		return pass;
	}

	void setPass(int pass) {
		if (pass == 0) {
			this.pass = 0;
		}
	}

	int which() {
		return nextTurn;
	}

	int prev() {
		return otherSide(nextTurn);
	}

	boolean isPosibleToPlace(int x, int y, int color) {
		int p = y * 10 + x + 11;
		if (box[p] == NONE) {
			for (int i = 7; i >= 0; i--) {
				int wp = p + DIR[i];
				if (box[wp] == otherSide(color)) {
					do {
						wp += DIR[i];
					} while (box[wp] == otherSide(color));
					if (box[wp] == color) {
						return true;
					}
				}
			}
		}
		return false;
	}

	boolean isPosibleToPlace(int x, int y) {
		return isPosibleToPlace(x, y, nextTurn);
	}

	boolean isPosibleToPlace(int color) {
		for (int x = 7; x >= 0; x--) {
			for (int y = 7; y >= 0; y--) {
				if (isPosibleToPlace(x, y, color)) {
					return true;
				}
			}
		}
		return false;
	}

	boolean isPosibleToPlace() {
		return isPosibleToPlace(nextTurn);
	}

	boolean isEnd() {
		return (!isPosibleToPlace(BLACK) && !isPosibleToPlace(WHITE));
	}

	static int otherSide(int color) {
		if (color == BLACK)
			return WHITE;
		else if (color == WHITE)
			return BLACK;
		return NONE;
	}

	boolean[][] getReversible(int x, int y, int color) {
		int p = y * 10 + x + 11;
		boolean[][] reversible = new boolean[8][8];
		for (int i = 7; i >= 0; i--) {
			for (int j = 7; j >= 0; j--) {
				reversible[i][j] = false;
			}
		}
		if (box[p] == NONE) {
			for (int i = 7; i >= 0; i--) {
				int wp = p + DIR[i];
				if (box[wp] == otherSide(color)) {
					do {
						wp += DIR[i];
					} while (box[wp] == otherSide(color));
					if (box[wp] == color) {
						wp -= DIR[i];
						while (box[wp] == otherSide(color)) {
							reversible[(wp - 11) % 10][(wp - 11) / 10] = true;
							wp -= DIR[i];
						}
					}
				}
			}
		}
		return reversible;
	}

	boolean[][] getReversible(int x, int y) {
		return getReversible(x, y, nextTurn);
	}

	void placeStone(int x, int y) {
		int p = y * 10 + x + 11;
		box[p] = nextTurn;
		for (int i = 7; i >= 0; i--) {
			int wp = p + DIR[i];
			if (box[wp] == otherSide(nextTurn)) {
				do {
					wp += DIR[i];
				} while (box[wp] == otherSide(nextTurn));
				if (box[wp] == nextTurn) {
					wp -= DIR[i];
					while (box[wp] == otherSide(nextTurn)) {
						box[wp] = nextTurn;
						wp -= DIR[i];
					}
				}
			}
		}
		if (isPosibleToPlace(otherSide(nextTurn))) {
			nextTurn = otherSide(nextTurn);
		} else {
			pass = nextTurn;
		}
	}

	void placeStone(int x, int y, WhiteBlackCanvas whiteblackcanvas) {
		int p = y * 10 + x + 11;
		rx = x;
		ry = y;
		box[p] = nextTurn;

		whiteblackcanvas.repaint();
		whiteblackcanvas.serviceRepaints();

		for (int i = 7; i >= 0; i--) {
			int wp = p + DIR[i];
			if (box[wp] == otherSide(nextTurn)) {
				do {
					wp += DIR[i];
				} while (box[wp] == otherSide(nextTurn));
				if (box[wp] == nextTurn) {
					wp -= DIR[i];
					while (box[wp] == otherSide(nextTurn)) {

						try {
							Thread.currentThread().sleep(600);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						box[wp] = nextTurn;
						wp -= DIR[i];

						whiteblackcanvas.repaint();
						whiteblackcanvas.serviceRepaints();
					}
				}
			}
		}
		if (isPosibleToPlace(otherSide(nextTurn))) {
			nextTurn = otherSide(nextTurn);
		} else {
			pass = nextTurn;
		}
	}

	void pass() {
		nextTurn = otherSide(nextTurn);
	}

	void copyKyokumen(Kyokumen k) {
		System.arraycopy(box, 0, k.box, 0, box.length);
		k.nextTurn = nextTurn;
	}

	int countStone(int color) {
		int n = 0;
		for (int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				if (box[y * 10 + x] == color)
					n++;
			}
		}
		return n;
	}

	int countStone() {
		int n = 0;
		for (int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				if (box[y * 10 + x] != NONE)
					n++;
			}
		}
		return n;
	}

	public String toString() {
		return box.toString();
	}

	/* aがtrueならx *//* bがtrueなら++ */
	/* aがfalseならy *//* bがfalseなら-- */
	public void ido(boolean a, boolean b) {
		if (a) {
			if (b) {
				rx++;
				if (rx > 7) {
					rx = 0;
				}
			} else {
				rx--;
				if (rx < 0) {
					rx = 7;
				}
			}
		} else {
			if (b) {
				ry++;
				if (ry > 7) {
					ry = 0;
				}
			} else {
				ry--;
				if (ry < 0) {
					ry = 7;
				}
			}
		}
	}

	public static int random(int a) {
		int rand = random.nextInt() % a;

		if (rand < 0) {
			rand = -rand;
		}
		return rand;
	}
}

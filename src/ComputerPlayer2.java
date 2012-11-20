import java.util.*;
import java.io.*;

public class ComputerPlayer2 extends ComputerPlayer1 {
	protected int FINALCNT = 57;
	private final int[][] AVAL = { { 8, 1, 8, 8, 8, 8, 1, 8 },
			{ 1, 1, 4, 4, 4, 4, 1, 1 }, { 8, 4, 4, 4, 4, 4, 4, 8 },
			{ 8, 4, 4, 4, 4, 4, 4, 8 }, { 8, 4, 4, 4, 4, 4, 4, 8 },
			{ 8, 4, 4, 4, 4, 4, 4, 8 }, { 1, 1, 4, 4, 4, 4, 1, 1 },
			{ 8, 1, 8, 8, 8, 8, 1, 8 } };
	private final int[][] BVAL = { { 100, 1, 10, 10, 10, 10, 1, 100 },
			{ 1, 1, 4, 4, 4, 4, 1, 1 }, { 10, 4, 4, 4, 4, 4, 4, 10 },
			{ 10, 4, 4, 4, 4, 4, 4, 10 }, { 10, 4, 4, 4, 4, 4, 4, 10 },
			{ 10, 4, 4, 4, 4, 4, 4, 10 }, { 1, 1, 4, 4, 4, 4, 1, 1 },
			{ 100, 1, 10, 10, 10, 10, 1, 100 } };
	private final int[] DX = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private final int[] DY = { -1, -1, 0, 1, 1, 1, 0, -1 };
	private Hashtable sideHash = new Hashtable();
	protected int depth;
	protected Kyokumen[] node = new Kyokumen[16];
	private Kyokumen next = new Kyokumen();

	public ComputerPlayer2(int myColor) {
		super(myColor);
		makeSideHashTable();
		for (int i = 0; i < node.length; i++) {
			node[i] = new Kyokumen();
		}
	}

	public Point where(Kyokumen kyokumen) {
		int alpha = Integer.MIN_VALUE;
		int mx = 0;
		int my = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				int beta = Integer.MAX_VALUE;
				if (kyokumen.isPosibleToPlace(x, y)) {
					kyokumen.copyKyokumen(node[0]);
					node[0].placeStone(x, y);
					int est = estimate(node[0], alpha, beta);
					if (est > alpha) {
						alpha = est;
						mx = x;
						my = y;
					}
				}
			}
		}
		return new Point(mx, my);
	}

	private void makeSideHashTable() {
		int c = myColor;
		int o = Kyokumen.otherSide(c);
		int n = Kyokumen.NONE;
		int[][] sideKey = { { n, c, c, c, c, c, c, n },
				{ n, c, c, c, c, c, n, n }, { n, n, c, c, c, c, c, n },
				{ n, n, c, n, n, c, n, n }, { n, n, c, c, c, c, n, n },
				{ n, c, n, c, n, n, n, n }, { n, n, n, n, c, n, c, n },
				{ n, c, n, c, c, n, n, n }, { n, n, n, c, c, n, c, n },
				{ n, c, n, c, c, c, n, n }, { n, n, c, c, c, n, c, n },
				{ n, c, n, c, n, c, n, n }, { n, n, c, n, c, n, c, n },
				{ n, c, n, c, c, c, c, n }, { n, c, c, c, c, n, c, n },
				{ c, n, c, c, c, c, c, n }, { n, c, c, c, c, c, n, c },
				{ n, c, c, c, n, c, n, n }, { n, n, c, n, c, c, c, n },
				{ c, c, c, c, c, c, c, n }, { n, c, c, c, c, c, c, c },
				{ c, n, c, c, c, c, n, c }, { c, o, c, c, c, c, c, n },
				{ n, c, c, c, c, c, o, c }, { o, c, c, c, c, c, n, n },
				{ n, n, c, c, c, c, c, o }, { o, c, c, c, c, c, n, o },
				{ o, n, c, c, c, c, c, o }, { n, o, o, o, o, c, n, n },
				{ n, n, c, o, o, o, o, n }, { n, n, o, n, n, o, n, n },
				{ n, c, n, n, o, n, n, n }, { n, n, n, o, n, n, c, n } };
		int[] sideVal = { 20, -10, -10, 20, 10, -20, -20, -20, -20, -20, -20,
				-20, -20, -20, -20, -20, -20, -30, -30, 20, 20, -10, -20, -20,
				-20, -20, -20, -20, -10, -10, -20, -20, -20 };
		for (int i = 0; i < sideKey.length; i++) {
			sideHash.put(new SidePattern(sideKey[i]), new Integer(sideVal[i]));
		}
	}

	private int estFunc2(Kyokumen k) {
		int col = k.prev();
		int sum = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (k.isPosibleToPlace(x, y, col)) {
					sum += AVAL[x][y];
				}
				if (k.isPosibleToPlace(x, y, k.otherSide(col))) {
					sum -= BVAL[x][y];
				}
			}
		}
		return sum;
	}

	private int estFunc3(Kyokumen k) {
		int col = k.prev();
		int sum = 0;
		int[][] s = new int[4][8];
		Integer est;
		if (col == myColor) {
			for (int i = 0; i < 8; i++) {
				s[0][i] = k.getState(0, i);
				s[1][i] = k.getState(7, i);
				s[2][i] = k.getState(i, 0);
				s[3][i] = k.getState(i, 7);
			}
		} else {
			for (int i = 0; i < 8; i++) {
				s[0][i] = k.otherSide(k.getState(0, i));
				s[1][i] = k.otherSide(k.getState(7, i));
				s[2][i] = k.otherSide(k.getState(i, 0));
				s[3][i] = k.otherSide(k.getState(i, 7));
			}
		}
		for (int i = 0; i < s.length; i++) {
			if ((est = (Integer) (sideHash.get(new SidePattern(s[i])))) != null) {
				sum += est.intValue();
			}
		}
		return sum;
	}

	private int estFunc4(Kyokumen k) {
		if (k.getState(0, 0) == k.NONE && k.getState(0, 7) == k.NONE
				&& k.getState(7, 0) == k.NONE && k.getState(7, 7) == k.NONE) {
			return 0;
		}
		boolean[][] fix = new boolean[8][8];
		int col = k.prev();
		int sum = 0;
		int c;
		int limit;
		boolean[] full = { true, true, true, true };
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				fix[x][y] = false;
			}
		}
		for (int i = 0; i < 8; i++) {
			if (k.getState(i, 0) == k.NONE)
				full[0] = false;
			if (k.getState(i, 7) == k.NONE)
				full[1] = false;
			if (k.getState(0, i) == k.NONE)
				full[2] = false;
			if (k.getState(7, i) == k.NONE)
				full[3] = false;
		}
		if (full[0]) {
			for (int i = 0; i < 8; i++) {
				fix[i][0] = true;
			}
		}
		if (full[1]) {
			for (int i = 0; i < 8; i++) {
				fix[i][7] = true;
			}
		}
		if (full[2]) {
			for (int i = 0; i < 8; i++) {
				fix[0][i] = true;
			}
		}
		if (full[3]) {
			for (int i = 0; i < 8; i++) {
				fix[7][i] = true;
			}
		}
		limit = 7;
		if ((c = k.getState(0, 0)) != k.NONE) {
			int x = 0;
			while (k.getState(x, 0) == c) {
				fix[x][0] = true;
				int y = 1;
				while ((k.getState(x, y) == c) && (y <= limit)) {
					fix[x][y] = true;
					y++;
				}
				limit = y - 1;
				x++;
			}
		}
		limit = 0;
		if ((c = k.getState(0, 7)) != k.NONE) {
			int x = 0;
			while (k.getState(x, 7) == c) {
				fix[x][7] = true;
				int y = 6;
				while ((k.getState(x, y) == c) && (y > limit)) {
					fix[x][y] = true;
					y--;
				}
				limit = y + 1;
				x++;
			}
		}
		limit = 7;
		if ((c = k.getState(7, 0)) != k.NONE) {
			int x = 7;
			while (k.getState(x, 0) == c) {
				fix[x][0] = true;
				int y = 1;
				while ((k.getState(x, y) == c) && (y <= limit)) {
					fix[x][y] = true;
					y++;
				}
				limit = y - 1;
				x--;
			}
		}
		limit = 0;
		if ((c = k.getState(7, 7)) != k.NONE) {
			int x = 7;
			while (k.getState(x, 7) == c) {
				fix[x][7] = true;
				int y = 6;
				while ((k.getState(x, y) == c) && (y > limit)) {
					fix[x][y] = true;
					y--;
				}
				limit = y + 1;
				x--;
			}
		}
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (fix[x][y]) {
					if (k.getState(x, y) == col)
						sum++;
					else
						sum--;
				}
			}
		}
		return sum;
	}

	private int estFunc5(Kyokumen k) {
		int sum = 50;
		int col = k.prev();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (k.isPosibleToPlace(x, y)) {
					k.copyKyokumen(next);
					next.placeStone(x, y);
					if (next.countStone(col) == 0) {
						return -1000;
					} else if (!next.isPosibleToPlace(0, 0)
							&& !next.isPosibleToPlace(0, 7)
							&& !next.isPosibleToPlace(7, 0)
							&& !next.isPosibleToPlace(7, 7)) {
						sum = 0;
					}
				}
			}
		}
		return sum;
	}

	private int estFunc6(Kyokumen k) {
		int col = k.prev();
		int sum = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (k.getState(x, y) == k.NONE) {
					for (int i = 0; i < 8; i++) {
						int s = k.getState(x + DX[i], y + DY[i]);
						if (s == col)
							sum--;
						else if (s == k.otherSide(col))
							sum++;
					}
				}
			}
		}
		return sum;
	}

	protected int finalEstimate(Kyokumen kyokumen, int alpha, int beta) {
		boolean flag = false;
		depth++;
		if (kyokumen.isEnd()) {
			depth--;
			return kyokumen.countStone(myColor);
		}
		if (kyokumen.which() == myColor) {
			loop: for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (kyokumen.isPosibleToPlace(x, y)) {
						flag = true;
						kyokumen.copyKyokumen(node[depth]);
						node[depth].placeStone(x, y);
						int e = finalEstimate(node[depth], alpha, beta);
						if (e > alpha)
							alpha = e;
						if (alpha >= beta)
							break loop;
					}
				}
			}
			if (!flag) { // if pass
				kyokumen.copyKyokumen(node[depth]);
				node[depth].pass();
				int e = finalEstimate(node[depth], alpha, beta);
				if (e > alpha)
					alpha = e;
			}
			depth--;
			return alpha;
		} else {
			loop: for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (kyokumen.isPosibleToPlace(x, y)) {
						flag = true;
						kyokumen.copyKyokumen(node[depth]);
						node[depth].placeStone(x, y);
						int e = finalEstimate(node[depth], alpha, beta);
						if (e < beta)
							beta = e;
						if (beta <= alpha)
							break loop;
					}
				}
			}
			if (!flag) { // if pass
				kyokumen.copyKyokumen(node[depth]);
				node[depth].pass();
				int e = finalEstimate(node[depth], alpha, beta);
				if (e < beta)
					beta = e;
			}
			depth--;
			return beta;
		}
	}

	protected int generalEstimate(Kyokumen kyokumen) {
		if (kyokumen.countStone(kyokumen.prev()) == 0) {
			return Integer.MIN_VALUE / 2;
		} else if (kyokumen.countStone(kyokumen.which()) == 0) {
			return Integer.MAX_VALUE / 2;
		}
		int e1 = estFunc1(kyokumen);
		int e2 = estFunc2(kyokumen);
		int e3 = estFunc3(kyokumen);
		int e4 = 10 * estFunc4(kyokumen);
		int e5 = estFunc5(kyokumen);
		int e6 = 2 * estFunc6(kyokumen);
		return e1 + e2 + e3 + e4 + e5 + e6 + (int) (Kyokumen.random(8));
	}

	protected int estimate(Kyokumen kyokumen, int alpha, int beta) {
		if (kyokumen.countStone() >= FINALCNT) {
			return finalEstimate(kyokumen, alpha, beta);
		} else {
			return generalEstimate(kyokumen);
		}
	}
}

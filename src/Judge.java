public class Judge implements Runnable {
	static final int NONE = 0;
	static final int BLACK = 1;
	static final int WHITE = 2;

	Kyokumen kyokumen;
	WhiteBlackCanvas whiteblackcanvas;

	private Player player1, player2;

	Thread thread;

	private boolean onGame = true;
	private boolean thinkingFlag = false;

	Point point;

	public Judge(WhiteBlackCanvas whiteblackcanvas, Kyokumen kyokumen) {
		this.whiteblackcanvas = whiteblackcanvas;
		this.kyokumen = kyokumen;
	}

	synchronized void newGame(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		onGame = true;

		thread = new Thread(this);
		thread.start();
	}

	boolean onGame() {
		return onGame;
	}

	Player getThinkingPlayer() {
		if (onGame) {
			if (kyokumen.which() == Kyokumen.BLACK) {
				return player1;
			} else if (kyokumen.which() == Kyokumen.WHITE) {
				return player2;
			}
		}
		return null;
	}

	public void run() {
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY - 3);

		while (!kyokumen.isEnd() && onGame) {

			System.gc();

			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thinkingFlag = true;
			whiteblackcanvas.repaint();
			whiteblackcanvas.serviceRepaints();

			Player player = getThinkingPlayer();
			Point p = player.where(kyokumen);
			thinkingFlag = false;
			kyokumen.placeStone(p.x, p.y, whiteblackcanvas);

			whiteblackcanvas.repaint();
			whiteblackcanvas.serviceRepaints();
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		onGame = false;
		if (kyokumen.isEnd())
			whiteblackcanvas.setScene(whiteblackcanvas.RESULT);
	}

	public boolean getComputerThinking() {
		if (whiteblackcanvas.sentaku == 0) {
			if (kyokumen.which() == kyokumen.WHITE) {
				return thinkingFlag;
			}
		} else if (whiteblackcanvas.sentaku == 1) {
			if (kyokumen.which() == kyokumen.BLACK) {
				return thinkingFlag;
			}
		} else if (whiteblackcanvas.sentaku == 3) {
			return thinkingFlag;
		}
		return false;
	}
}
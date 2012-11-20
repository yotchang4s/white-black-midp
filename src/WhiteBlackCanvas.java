import javax.microedition.lcdui.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class WhiteBlackCanvas extends Canvas {

	public final static int TITLE = 1, // タイトル
			LEVEL = 2, // レベル選択
			PLAY = 3, // プレイ中
			RESULT = 4, // 結果
			COM1 = 5, // COM
			COM2 = 6; // COM

	private int scene = TITLE;

	private Kyokumen kyokumen;
	private Judge judge;
	private Hyouji hyouji;
	private Player player1;
	private Player player2;
	private WhiteBlack WhiteBlack;

	private int hyoujiCount = 0;
	public int sentaku;
	private int level;
	private int com1Level;
	private int com2Level;
	private int whichWin;

	public WhiteBlackCanvas(WhiteBlack WhiteBlack) {
		this.WhiteBlack = WhiteBlack;
		hyouji = new Hyouji(getWidth(), getHeight());
		sentaku = 0;
	}

	// 描画処理
	public void paint(Graphics g) {
		if (scene == PLAY) {
			hyouji.update(g, kyokumen.getStone());
			hyouji.paintTurnMessage(g, kyokumen.which(), sentaku, level,
					com1Level, com2Level);
			hyouji.paintIshiCountMessage(g,
					kyokumen.countStone(kyokumen.BLACK),
					kyokumen.countStone(kyokumen.WHITE));

			if (!kyokumen.isEnd()) {
				if (hyouji.paintPass(g, kyokumen.getPass(), sentaku)) {
					hyoujiCount++;

					if (hyoujiCount > 1) {
						kyokumen.setPass(0);
						hyoujiCount = 0;
					}
				}
			} else {
				hyoujiCount = 0;
				kyokumen.setPass(0);
			}

			if (!judge.getComputerThinking()) {
				hyouji.paintYajirushi(g, kyokumen.getRx(), kyokumen.getRy());
			}

			hyouji.paintDialog(g, judge.getComputerThinking());
		} else if (scene == TITLE) {
			hyouji.paintTitle(g, sentaku);
		} else if (scene == LEVEL) {
			hyouji.paintLevelSentaku(g, level, sentaku);
		} else if (scene == COM1) {
			hyouji.paintCom(g, com1Level, 0);
		} else if (scene == COM2) {
			hyouji.paintCom(g, com2Level, 1);
		} else if (scene == RESULT) {
			hyouji.paintRESULT(g, whichWin, sentaku);
		}
	}

	public void setScene(int scene) {
		switch (scene) {
		case TITLE:
			judge = null;
			System.gc();
			break;
		case PLAY:
			kyokumen = new Kyokumen();
			judge = new Judge(this, kyokumen);
			judge.newGame(player1, player2);
			addCommand(WhiteBlack.reset); // ゲームリセットコマンドの登録
			removeCommand(WhiteBlack.help);
			break;
		case RESULT:
			if (sentaku == 0) {
				if (kyokumen.countStone(kyokumen.BLACK) > kyokumen
						.countStone(kyokumen.WHITE)) {
					whichWin = Kyokumen.BLACK;
				} else if (kyokumen.countStone(kyokumen.BLACK) < kyokumen
						.countStone(kyokumen.WHITE)) {
					whichWin = Kyokumen.WHITE;
				} else {
					whichWin = Kyokumen.NONE;
				}
			} else if (sentaku == 1) {
				if (kyokumen.countStone(kyokumen.BLACK) < kyokumen
						.countStone(kyokumen.WHITE)) {
					whichWin = Kyokumen.WHITE;
				} else if (kyokumen.countStone(kyokumen.BLACK) > kyokumen
						.countStone(kyokumen.WHITE)) {
					whichWin = Kyokumen.BLACK;
				} else {
					whichWin = Kyokumen.NONE;
				}
			} else {
				if (kyokumen.countStone(kyokumen.BLACK) < kyokumen
						.countStone(kyokumen.WHITE)) {
					whichWin = Kyokumen.WHITE;
				} else if (kyokumen.countStone(kyokumen.BLACK) > kyokumen
						.countStone(kyokumen.WHITE)) {
					whichWin = Kyokumen.BLACK;
				} else {
					whichWin = Kyokumen.NONE;
				}
			}
			break;
		}
		this.scene = scene;
		repaint();
	}

	protected synchronized void keyPressed(int keyCode) {
		int action = getGameAction(keyCode);

		if (scene == PLAY) {
			if (sentaku == 0) {
				if (kyokumen.which() == Kyokumen.WHITE) {
					return;
				}
			} else if (sentaku == 1) {
				if (kyokumen.which() == Kyokumen.BLACK) {
					return;
				}
			} else if (sentaku == 3) {
				return;
			}
			if (action == LEFT || keyCode == KEY_NUM4) {
				kyokumen.ido(true, false);
			} else if (action == RIGHT || keyCode == KEY_NUM6) {
				kyokumen.ido(true, true);

			} else if (action == UP || keyCode == KEY_NUM2) {
				kyokumen.ido(false, false);

			} else if (action == DOWN || keyCode == KEY_NUM8) {
				kyokumen.ido(false, true);

			} else if (action == FIRE || keyCode == KEY_NUM5) {
				Player player = judge.getThinkingPlayer();
				if (judge.onGame() && player instanceof HumanPlayer) {
					((HumanPlayer) player).decide(new Point(kyokumen.getRx(),
							kyokumen.getRy()));
				}
			}
			repaint();
			return;
		} else if (scene == TITLE) {
			if (action == UP || keyCode == KEY_NUM2) {
				sentaku--;
				if (sentaku < 0) {
					sentaku = 3;
				}
			} else if (action == DOWN || keyCode == KEY_NUM8) {
				sentaku++;
				if (sentaku > 3) {
					sentaku = 0;
				}
			} else if (action == FIRE || keyCode == KEY_NUM5) {
				if (sentaku == 2) {
					player1 = new HumanPlayer();
					player2 = new HumanPlayer();
					setScene(PLAY);
				} else if (sentaku == 3) {
					setScene(COM1);
				} else {
					setScene(LEVEL);
				}
			}
			repaint();
			return;
		} else if (scene == LEVEL) {
			if (keyCode == KEY_NUM1 || keyCode == KEY_NUM2
					|| keyCode == KEY_NUM3 || keyCode == KEY_NUM4
					|| keyCode == KEY_NUM5 || keyCode == KEY_NUM6) {

				if (sentaku == 0) {
					player1 = new HumanPlayer();
					switch (keyCode) {
					case KEY_NUM1:
						level = 0;
						player2 = new ComputerPlayer0(Kyokumen.WHITE);
						break;

					case KEY_NUM2:
						level = 1;
						player2 = new ComputerPlayer1(Kyokumen.WHITE);
						break;
					case KEY_NUM3:
						level = 2;
						player2 = new ComputerPlayer2(Kyokumen.WHITE);
						break;
					case KEY_NUM4:
						level = 3;
						player2 = new ComputerPlayer3(Kyokumen.WHITE);
						break;
					case KEY_NUM5:
						level = 4;
						player2 = new ComputerPlayer4(Kyokumen.WHITE);
						break;
					case KEY_NUM6:
						level = 5;
						player2 = new ComputerPlayer5(Kyokumen.WHITE);
						break;
					}
				} else if (sentaku == 1) {
					player2 = new HumanPlayer();
					switch (keyCode) {
					case KEY_NUM1:
						level = 0;
						player1 = new ComputerPlayer0(Kyokumen.BLACK);
						break;

					case KEY_NUM2:
						level = 1;
						player1 = new ComputerPlayer1(Kyokumen.BLACK);
						break;
					case KEY_NUM3:
						level = 2;
						player1 = new ComputerPlayer2(Kyokumen.BLACK);
						break;
					case KEY_NUM4:
						level = 3;
						player1 = new ComputerPlayer3(Kyokumen.BLACK);
						break;
					case KEY_NUM5:
						level = 4;
						player1 = new ComputerPlayer4(Kyokumen.BLACK);
						break;
					case KEY_NUM6:
						level = 5;
						player1 = new ComputerPlayer5(Kyokumen.BLACK);
						break;
					}
				}
				setScene(PLAY);
				return;
			}
			if (action == UP) {
				level--;
				if (level < 0) {
					level = 5;
				}
			} else if (action == DOWN) {
				level++;
				if (level > 5) {
					level = 0;
				}
			} else if (action == FIRE) {
				if (sentaku == 0) {
					player1 = new HumanPlayer();
					switch (level) {
					case 0:
						player2 = new ComputerPlayer0(Kyokumen.WHITE);
						break;
					case 1:
						player2 = new ComputerPlayer1(Kyokumen.WHITE);
						break;
					case 2:
						player2 = new ComputerPlayer2(Kyokumen.WHITE);
						break;
					case 3:
						player2 = new ComputerPlayer3(Kyokumen.WHITE);
						break;
					case 4:
						player2 = new ComputerPlayer4(Kyokumen.WHITE);
						break;
					case 5:
						player2 = new ComputerPlayer5(Kyokumen.WHITE);
						break;
					}
				} else if (sentaku == 1) {
					player2 = new HumanPlayer();
					switch (level) {
					case 0:
						player1 = new ComputerPlayer0(Kyokumen.BLACK);
						break;
					case 1:
						player1 = new ComputerPlayer1(Kyokumen.BLACK);
						break;
					case 2:
						player1 = new ComputerPlayer2(Kyokumen.BLACK);
						break;
					case 3:
						player1 = new ComputerPlayer3(Kyokumen.BLACK);
						break;
					case 4:
						player1 = new ComputerPlayer4(Kyokumen.BLACK);
						break;
					case 5:
						player1 = new ComputerPlayer5(Kyokumen.BLACK);
						break;
					}
				}
				setScene(PLAY);
			}
			repaint();
		} else if (scene == COM1) {
			if (keyCode == KEY_NUM1 || keyCode == KEY_NUM2
					|| keyCode == KEY_NUM3 || keyCode == KEY_NUM4
					|| keyCode == KEY_NUM5 || keyCode == KEY_NUM6) {

				switch (keyCode) {
				case KEY_NUM1:
					com1Level = 0;
					break;

				case KEY_NUM2:
					com1Level = 1;
					break;
				case KEY_NUM3:
					com1Level = 2;
					break;
				case KEY_NUM4:
					com1Level = 3;
					break;
				case KEY_NUM5:
					com1Level = 4;
					break;
				case KEY_NUM6:
					com1Level = 5;
					break;
				}
				setScene(COM2);
			}
			if (action == UP) {
				com1Level--;
				if (com1Level < 0) {
					com1Level = 5;
				}
			} else if (action == DOWN) {
				com1Level++;
				if (com1Level > 5) {
					com1Level = 0;
				}
			} else if (action == FIRE) {
				setScene(COM2);
			}
			repaint();
		} else if (scene == COM2) {
			if (keyCode == KEY_NUM1 || keyCode == KEY_NUM2
					|| keyCode == KEY_NUM3 || keyCode == KEY_NUM4
					|| keyCode == KEY_NUM5 || keyCode == KEY_NUM6) {

				switch (com1Level) {
				case 0:
					player1 = new ComputerPlayer0(Kyokumen.BLACK);
					break;

				case 1:
					player1 = new ComputerPlayer1(Kyokumen.BLACK);
					break;
				case 2:
					player1 = new ComputerPlayer2(Kyokumen.BLACK);
					break;
				case 3:
					player1 = new ComputerPlayer3(Kyokumen.BLACK);
					break;
				case 4:
					player1 = new ComputerPlayer4(Kyokumen.BLACK);
					break;
				case 5:
					player1 = new ComputerPlayer5(Kyokumen.BLACK);
					break;
				}

				switch (keyCode) {
				case KEY_NUM1:
					com2Level = 0;
					player2 = new ComputerPlayer0(Kyokumen.WHITE);
					break;
				case KEY_NUM2:
					com2Level = 1;
					player2 = new ComputerPlayer1(Kyokumen.WHITE);
					break;
				case KEY_NUM3:
					com2Level = 2;
					player2 = new ComputerPlayer2(Kyokumen.WHITE);
					break;
				case KEY_NUM4:
					com2Level = 3;
					player2 = new ComputerPlayer3(Kyokumen.WHITE);
					break;
				case KEY_NUM5:
					com2Level = 4;
					player2 = new ComputerPlayer4(Kyokumen.WHITE);
					break;
				case KEY_NUM6:
					com2Level = 5;
					player2 = new ComputerPlayer5(Kyokumen.WHITE);
					break;
				}
				setScene(PLAY);
				repaint();
				return;
			}
			if (action == UP) {
				com2Level--;
				if (com2Level < 0) {
					com2Level = 5;
				}
			} else if (action == DOWN) {
				com2Level++;
				if (com2Level > 5) {
					com2Level = 0;
				}
			} else if (action == FIRE) {
				switch (com1Level) {
				case 0:
					player1 = new ComputerPlayer0(Kyokumen.BLACK);
					break;
				case 1:
					player1 = new ComputerPlayer1(Kyokumen.BLACK);
					break;
				case 2:
					player1 = new ComputerPlayer2(Kyokumen.BLACK);
					break;
				case 3:
					player1 = new ComputerPlayer3(Kyokumen.BLACK);
					break;
				case 4:
					player1 = new ComputerPlayer4(Kyokumen.BLACK);
					break;
				case 5:
					player1 = new ComputerPlayer5(Kyokumen.BLACK);
					break;
				}

				switch (com2Level) {
				case 0:
					player2 = new ComputerPlayer0(Kyokumen.WHITE);
					break;
				case 1:
					player2 = new ComputerPlayer1(Kyokumen.WHITE);
					break;
				case 2:
					player2 = new ComputerPlayer2(Kyokumen.WHITE);
					break;
				case 3:
					player2 = new ComputerPlayer3(Kyokumen.WHITE);
					break;
				case 4:
					player2 = new ComputerPlayer4(Kyokumen.WHITE);
					break;
				case 5:
					player2 = new ComputerPlayer5(Kyokumen.WHITE);
					break;
				}
				setScene(PLAY);
			}
			repaint();
			return;
		}
	}
}

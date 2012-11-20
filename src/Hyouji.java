import javax.microedition.lcdui.*;
import java.lang.*;
import java.util.*;

public class Hyouji {
	private Image shiro = null;
	private Image kuro = null;

	private Image yajirushi = null;
	private Image title = null;
	private Image girl = null;
	private Image fukidashi = null;

	private Image ueShiro = null;
	private Image ueKuro = null;

	private int margin, tatemargin; // 縦横の空白
	private int square; // マス目の幅

	private int width, height;

	private Font def = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN,
			Font.SIZE_MEDIUM);
	private Font small = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN,
			Font.SIZE_SMALL);
	private Font kazu = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
			Font.SIZE_MEDIUM);
	private Font levelFont = Font.getFont(Font.FACE_MONOSPACE,
			Font.STYLE_PLAIN, Font.SIZE_SMALL);

	public Hyouji(int width, int height) {
		this.width = width;
		this.height = height;
		size(width, height);
		getResource();
	}

	public void paintTurnMessage(Graphics g, int turn, int sentaku, int level,
			int level1, int level2) {
		// 石との対応の表示
		int p1 = 0;
		int p2 = 0;

		Font oldFont;

		oldFont = g.getFont();

		g.setFont(def);

		if (turn == 1) {
			p1 = 0;
			p2 = 30;
		} else {
			p1 = 30;
			p2 = 0;
		}

		String player1;
		String player2;

		if (sentaku == 0) {
			player1 = "You";
			player2 = "Com";
		} else if (sentaku == 1) {
			player1 = "Com";
			player2 = "You";
		} else if (sentaku == 2) {
			player1 = "P1";
			player2 = "P2";
		} else {
			player1 = "L" + level1;
			player2 = "L" + level2;
		}

		g.drawImage(ueKuro, 5 + p1, tatemargin - 11, g.TOP | g.LEFT);

		g.setColor(0x000000);
		g.setFont(def);
		g.drawString(player1, 15 + p1, tatemargin - 13, g.TOP | g.LEFT);

		g.drawImage(ueShiro, 5 + p2, tatemargin - 11, g.TOP | g.LEFT);
		g.drawString(player2, 15 + p2, tatemargin - 13, g.TOP | g.LEFT);

		g.setFont(kazu);

		if (sentaku == 0 || sentaku == 1) {
			g.drawString("L" + level, width - 20, tatemargin - 13, g.TOP
					| g.LEFT);
		}

		g.setFont(oldFont);
	}

	public void paintIshiCountMessage(Graphics g, int blackCount, int whiteCount) {
		// 途中経過の表示
		g.setFont(kazu);
		g.setColor(0x000000);
		String bothCount = null;
		if (blackCount < 10) {
			bothCount = "0" + blackCount;
		} else {
			bothCount = "" + blackCount;
		}
		bothCount += "-";
		if (whiteCount < 10) {
			bothCount += "0" + whiteCount;
		} else {
			bothCount += whiteCount;
		}
		g.drawString(bothCount, width - square * 4, tatemargin - 13, g.TOP
				| g.LEFT);

	}

	public boolean paintPass(Graphics g, int pass, int sentaku) {
		if (pass != 1 && pass != 2)
			return false;
		String message = null;

		g.setFont(def);
		if (sentaku == 1) {
			if (pass == 1) {
				message = "YOUパス...";
			} else if (pass == 2) {
				message = "COMパス...";
			}
		} else if (sentaku == 0) {
			if (pass == 1) {
				message = "COMパス...";
			} else if (pass == 2) {
				message = "Youパス...";
			}
		} else {
			if (pass == 2) {
				message = "黒石パス...";
			} else if (pass == 1) {
				message = "白石パス...";
			}
		}

		g.setColor(0x000000);
		g.fillRect((width - 100) / 2, height / 2 - 10, 100, 20);
		g.setColor(0xffffff);
		g.fillRect((width - 100) / 2 + 1, height / 2 - 10 + 1, 100 - 2, 20 - 2);

		g.setColor(0x000000);
		g.setFont(def);
		g.drawString(message, width / 2, height / 2 + 8, Graphics.HCENTER
				| Graphics.BOTTOM);
		return true;
	}

	public void paintRESULT(Graphics g, int which, int sentaku) {
		// 結果の表示
		String message;

		if (sentaku == 0) {
			if (which == Kyokumen.BLACK) {
				message = "あなたの勝ち！";
			} else if (which == Kyokumen.WHITE) {
				message = "あなたの負け...";
			} else {
				message = "引き分け...";
			}
		} else if (sentaku == 1) {
			if (which == Kyokumen.WHITE) {
				message = "あなたの勝ち！";
			} else if (which == Kyokumen.BLACK) {
				message = "あなたの負け...";
			} else {
				message = "引き分け...";
			}
		} else {
			if (which == Kyokumen.BLACK) {
				message = "黒の勝ち！";
			} else if (which == Kyokumen.WHITE) {
				message = "白の勝ち！";
			} else {
				message = "引き分け...";
			}
		}

		g.setColor(0x000000);
		g.fillRect((width - 100) / 2, height / 2 - 10, 100, 20);
		g.setColor(0xffffff);
		g.fillRect((width - 100) / 2 + 1, height / 2 - 10 + 1, 100 - 2, 20 - 2);

		g.setColor(0x000000);
		g.setFont(def);
		g.drawString(message, width / 2, height / 2 + 8, Graphics.HCENTER
				| Graphics.BOTTOM);
	}

	public void paintYajirushi(Graphics g, int rx, int ry) {
		// 矢印の表示
		g.drawImage(yajirushi, margin + square * rx + square / 4, tatemargin
				+ square * ry + square / 2, g.TOP | g.LEFT);
	}

	// 描画処理の中身
	public void update(Graphics g, int[][] stone) {
		int k; // マスの番号
		int xx, yy; // マスの座標

		// バックを塗る
		g.setColor(0xffcc00);
		g.fillRect(0, 0, width, height);

		// 盤面の描画
		g.setColor(0x00bb00);
		g.fillRect(margin, tatemargin, 8 * square, 8 * square);

		g.setColor(0x000000);
		for (int i = 0; i < 8 + 1; i++) {
			g.drawLine(i * square + margin, tatemargin, i * square + margin, 8
					* square + tatemargin);
			g.drawLine(margin, i * square + tatemargin, 8 * square + margin, i
					* square + tatemargin);

		}
		g.fillRect(2 * square + margin - 1, 2 * square + tatemargin - 1, 3, 3);
		g.fillRect(2 * square + margin - 1, 6 * square + tatemargin - 1, 3, 3);
		g.fillRect(6 * square + margin - 1, 2 * square + tatemargin - 1, 3, 3);
		g.fillRect(6 * square + margin - 1, 6 * square + tatemargin - 1, 3, 3);

		// 盤面の影の描画
		g.setColor(0x5e5e5e);
		g.drawLine(margin + 1, 8 * square + tatemargin + 1, 8 * square + margin
				+ 1, 8 * square + tatemargin + 1);
		g.drawLine(8 * square + margin + 1, tatemargin + 1, 8 * square + margin
				+ 1, 8 * square + tatemargin + 1);

		// 石の描画
		for (int i = 0; i < 8; i++) {
			xx = i * square + margin;

			for (int j = 0; j < 8; j++) {
				yy = j * square + tatemargin;

				if (stone[i][j] == 1) {
					g.drawImage(kuro, xx + 2, yy + 2, g.TOP | g.LEFT);

				} else if (stone[i][j] == 2) {
					g.drawImage(shiro, xx + 2, yy + 2, g.TOP | g.LEFT);
				}
			}
		}
	}

	public void size(int width, int height) {
		if (width <= 120) {
			if (height >= 130) {
				square = 14;
			} else if (height >= 120) {
				square = 13;
			} else {
				square = 12;
			}
		} else if (width >= 135) {
			if (height >= 145) {
				square = 16;
				tatemargin = (height - 8 * square) - 8;
			} else {
				square = 15;
				tatemargin = (height - 8 * square) - 8;
			}
		} else if (width >= 130) {
			if (height >= 135) {
				square = 15;
			} else {
				square = 14;
			}
		} else {
			square = 14;
		}
		margin = (width - 8 * square) / 2 - 1;
		tatemargin = (height - 8 * square + 10) / 2;
	}

	public void paintTitle(Graphics g, int sentaku) {
		g.setColor(0x000000);
		g.fillRect(0, 0, width, height);

		g.drawImage(title, width / 2, title.getHeight() / 2, Graphics.HCENTER
				| Graphics.VCENTER);

		if (sentaku == 0) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.setFont(def);
		g.drawString("先手", width / 2,
				(height - title.getHeight()) / 2 + title.getHeight() - 13,
				Graphics.HCENTER | Graphics.BOTTOM);
		if (sentaku == 1) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("後手", width / 2,
				(height - title.getHeight()) / 2 + title.getHeight(),
				Graphics.HCENTER | Graphics.BOTTOM);
		if (sentaku == 2) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("対人", width / 2,
				(height - title.getHeight()) / 2 + title.getHeight() + 13,
				Graphics.HCENTER | Graphics.BOTTOM);
		if (sentaku == 3) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("COM対COM", width / 2, (height - title.getHeight()) / 2
				+ title.getHeight() + 26, Graphics.HCENTER | Graphics.BOTTOM);
	}

	public void paintCom(Graphics g, int level, int sentaku) {
		g.setColor(0x000000);
		g.fillRect(0, 0, width, height);

		String s;

		g.drawImage(fukidashi, width / 2 + 15, height / 2 - 40,
				Graphics.HCENTER | Graphics.VCENTER);

		if (sentaku == 0) {
			s = "Com1のﾚﾍﾞﾙは?";
		} else {
			s = "Com2のﾚﾍﾞﾙは?";
		}

		g.setColor(0x000000);
		g.drawString(s, width / 2 + 15, height / 2 - 34, Graphics.HCENTER
				| Graphics.BOTTOM);

		g.setColor(0xffffff);
		g.drawImage(girl, width / 2 - 40, height / 2, Graphics.HCENTER
				| Graphics.VCENTER);

		g.setFont(levelFont);

		if (level == 0) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.setFont(def);
		g.drawString("\ue522" + "ｽﾄﾚｽ解消ｯ", width / 2 - 15, height / 2 - 27,
				g.TOP | g.LEFT);
		if (level == 1) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue523" + "ﾁｮﾛｲぜっ!", width / 2 - 15, height / 2 - 14,
				g.TOP | g.LEFT);
		if (level == 2) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue524" + "ﾏﾀﾞﾏﾀﾞｯ!", width / 2 - 15, height / 2 - 1,
				g.TOP | g.LEFT);
		if (level == 3) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue525" + "おやっ？", width / 2 - 15, height / 2 + 12, g.TOP
				| g.LEFT);
		if (level == 4) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue526" + "ﾑｯ...  遅", width / 2 - 15, height / 2 + 25,
				g.TOP | g.LEFT);
		if (level == 5) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue527" + "ﾑﾑﾑｯ...遅", width / 2 - 15, height / 2 + 38,
				g.TOP | g.LEFT);
	}

	public void paintDialog(Graphics g, boolean flag) {
		if (flag) {
			g.setColor(0x000000);
			g.fillRect((width - 100) / 2, height / 2 - 10, 100, 20);
			g.setColor(0xffffff);
			g.fillRect((width - 100) / 2 + 1, height / 2 - 10 + 1, 100 - 2,
					20 - 2);

			g.setColor(0x000000);
			g.setFont(def);
			g.drawString("\ue489" + "考え中...", width / 2, height / 2 + 8,
					Graphics.HCENTER | Graphics.BOTTOM);
		}
	}

	public void paintLevelSentaku(Graphics g, int level, int sentaku) {

		g.setColor(0x000000);
		g.fillRect(0, 0, width, height);

		String s;

		g.drawImage(fukidashi, width / 2 + 15, height / 2 - 40,
				Graphics.HCENTER | Graphics.VCENTER);

		if (sentaku == 0) {
			s = "ｱﾅﾀが先手" + "\ue513";
		} else {
			s = "\ue513" + "ｱﾅﾀが後手";
		}

		g.setColor(0x000000);
		g.drawString(s, width / 2 + 15, height / 2 - 34, Graphics.HCENTER
				| Graphics.BOTTOM);

		g.setColor(0xffffff);
		g.drawImage(girl, width / 2 - 40, height / 2, Graphics.HCENTER
				| Graphics.VCENTER);

		g.setFont(levelFont);

		if (level == 0) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.setFont(def);
		g.drawString("\ue522" + "ｽﾄﾚｽ解消ｯ", width / 2 - 15, height / 2 - 27,
				g.TOP | g.LEFT);
		if (level == 1) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue523" + "ﾁｮﾛｲぜっ!", width / 2 - 15, height / 2 - 14,
				g.TOP | g.LEFT);
		if (level == 2) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue524" + "ﾏﾀﾞﾏﾀﾞｯ!", width / 2 - 15, height / 2 - 1,
				g.TOP | g.LEFT);
		if (level == 3) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue525" + "おやっ？", width / 2 - 15, height / 2 + 12, g.TOP
				| g.LEFT);
		if (level == 4) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue526" + "ﾑｯ...  遅", width / 2 - 15, height / 2 + 25,
				g.TOP | g.LEFT);
		if (level == 5) {
			g.setColor(0xffffff);
		} else {
			g.setColor(0x5e5e5e);
		}
		g.drawString("\ue527" + "ﾑﾑﾑｯ...遅", width / 2 - 15, height / 2 + 38,
				g.TOP | g.LEFT);
	}

	private void getResource() {
		try {
			yajirushi = Image.createImage("/yajirushi.png"); // スラッシュ必須
			title = Image.createImage("/title.png");
			girl = Image.createImage("/girl.png");
			fukidashi = Image.createImage("/fukidashi.png");

			ueShiro = Image.createImage("/shiro12.png");
			ueKuro = Image.createImage("/kuro12.png");

			kuro = Image.createImage("/kuro" + square + ".png"); // スラッシュ必須
			shiro = Image.createImage("/shiro" + square + ".png"); // スラッシュ必須
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
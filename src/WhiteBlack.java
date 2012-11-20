import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.*;

public class WhiteBlack extends MIDlet implements CommandListener {
	public Command exit, reset, help; // コマンド
	private WhiteBlackCanvas canvas; // キャンバス
	RecordStore rs = null;

	private static String setsumei = "この度はDLして頂き、誠にありがとうございました。\n\n"
			+ "注意！\n"
			+ "Comが強ければ強いほど、Comの思考時間は長く長くなります。一番強いモードだと何(十?)分もかかる場合もありますので、予め御了承下さい。"
			+ "\n\nこのゲームを利用したことによって起きた如何なるトラブルも私(鉄太郎)は一切責任を負いません。\n"
			+ "また、このゲームの著作権は私(鉄太郎)にあります。無断配布は禁止しております。\n\n"
			+ "上記の事に同意できない方は今すぐにこのゲームをデータフォルダから削除してください。\n"
			+ "今すぐに削除しない場合は上記の事を納得した事とします。\n\n" + "(C)2001　鉄太郎";

	// コンストラクタ
	public WhiteBlack() {
		exit = new Command("Exit", Command.EXIT, 1); // ゲーム終了コマンド
		reset = new Command("Reset", Command.SCREEN, 3);// ゲームリセットコマンド
		help = new Command("Help", Command.SCREEN, 3);// ゲームリセットコマンド
		canvas = new WhiteBlackCanvas(this); // ゲームキャンバス
		canvas.addCommand(exit); // ゲーム終了コマンドの登録
		canvas.addCommand(help); // ゲーム終了コマンドの登録

		// コマンドリスナーをセット
		canvas.setCommandListener(this);

		// 画面をキャンバスにする
		Display.getDisplay(this).setCurrent(canvas);

		try {
			rs = RecordStore.openRecordStore("set", false);
			rs.closeRecordStore();
		} catch (RecordStoreException e) {
			try {
				rs = RecordStore.openRecordStore("set", true);

				String s = "0";
				byte[] data = s.getBytes();
				rs.addRecord(data, 0, data.length);

				rs.closeRecordStore();

				Alert alert = new Alert("アプリについて");
				alert.setTimeout(Alert.FOREVER);
				alert.setString(setsumei);

				Display.getDisplay(this).setCurrent(alert);
			} catch (RecordStoreException f) {
				f.printStackTrace();
			}
		}
	}

	// 開始、終了
	public void startApp() {
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	// ラベル部分の処理

	public void commandAction(Command c, Displayable s) {
		// 終了処理
		if (c == exit) {
			destroyApp(false);
			notifyDestroyed();
		}
		// リセット処理
		else if (c == reset) {
			canvas.setScene(canvas.TITLE);
			canvas.removeCommand(reset);
			canvas.addCommand(help);
			canvas.repaint();
		}
		// ヘルプ表示
		else if (c == help) {
			Help.showHelp(Display.getDisplay(this));
		}
	}
}

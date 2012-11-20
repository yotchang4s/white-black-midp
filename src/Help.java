import javax.microedition.lcdui.*;
import java.io.*;

public class Help {

	private static String setsumei = "遊び方\n\n"
			+ "まず最初の画面で先手●か後手○か決めます。これらは対COMです。\n" + "対人は友達等との対戦です。"
			+ "対人の場合はそのままゲーム開始になりますが、それ以外は次の画面でレベルを選択します。\n"
			+ "レベルを選択したらゲーム開始です！\n\n" + "ルール\n"
			+ "いたって簡単！交互に石を置いていきます。その時に相手の石を自分の石で挟むようにするのです。挟まれた石は裏返します\n"
			+ "しか〜し、簡単だからといって侮るなかれ！とても奥が深いゲームなのです。\n\n"
			+ "ちょっと暇なときや、息抜きしたいときに是非どうぞ＾＾\n" + "(C)2001　鉄太郎";

	private Displayable previous;

	private Help() {
	};

	public static void showHelp(Display display) {
		Alert alert = new Alert("遊び方");
		alert.setTimeout(Alert.FOREVER);

		alert.setString(setsumei);
		display.setCurrent(alert);
	}

}

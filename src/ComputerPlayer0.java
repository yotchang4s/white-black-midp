public class ComputerPlayer0 implements Player {
	private boolean thinkingFlag = false;

	public ComputerPlayer0(int myColor) {
	}

	public Point where(Kyokumen kyokumen) {
		thinkingFlag = true;
		int random1;
		int random2;

		while (true) {
			random1 = Kyokumen.random(8);
			random2 = Kyokumen.random(8);
			if (kyokumen.isPosibleToPlace(random1, random2)) {
				break;
			}
		}
		thinkingFlag = false;
		return new Point(random1, random2);
	}

	public boolean getThinking() {
		return thinkingFlag;
	}
}

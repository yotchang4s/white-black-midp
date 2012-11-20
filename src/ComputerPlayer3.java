public class ComputerPlayer3 extends ComputerPlayer2 {
	protected int MAXDEP = 1;

	public ComputerPlayer3(int myColor) {
		super(myColor);
		FINALCNT = 56;
	}

	protected int generalEstimate(Kyokumen kyokumen, int alpha, int beta) {
		boolean flag = false;
		depth++;
		if (kyokumen.which() == myColor) {
			loop: for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (kyokumen.isPosibleToPlace(x, y)) {
						flag = true;
						kyokumen.copyKyokumen(node[depth]);
						node[depth].placeStone(x, y);
						if (depth >= MAXDEP) {
							int e = super.generalEstimate(node[depth]);
							if (e > alpha)
								alpha = e;
						} else {
							int e = generalEstimate(node[depth], alpha, beta);
							if (e > alpha)
								alpha = e;
						}
						if (alpha > beta)
							break loop;
					}
				}
			}
			if (!flag) {
				kyokumen.copyKyokumen(node[depth]);
				node[depth].pass();
				if (depth >= MAXDEP) {
					int e = super.generalEstimate(node[depth]);
					if (e > alpha)
						alpha = e;
				} else {
					int e = generalEstimate(node[depth], alpha, beta);
					if (e > alpha)
						alpha = e;
				}
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
						if (depth >= MAXDEP) {
							int e = -super.generalEstimate(node[depth]);
							if (e < beta)
								beta = e;
						} else {
							int e = generalEstimate(node[depth], alpha, beta);
							if (e < beta)
								beta = e;
						}
						if (beta < alpha) {
							break loop;
						}
					}
				}
			}
			if (!flag) {
				kyokumen.copyKyokumen(node[depth]);
				node[depth].pass();
				if (depth >= MAXDEP) {
					int e = -super.generalEstimate(node[depth]);
					if (e < beta)
						beta = e;
				} else {
					int e = generalEstimate(node[depth], alpha, beta);
					if (e < beta)
						beta = e;
				}
			}
			depth--;
			return beta;
		}
	}

	protected int estimate(Kyokumen kyokumen, int alpha, int beta) {
		if (kyokumen.countStone() >= FINALCNT) {
			return finalEstimate(kyokumen, alpha, beta);
		} else {
			return generalEstimate(kyokumen, alpha, beta);
		}
	}
}

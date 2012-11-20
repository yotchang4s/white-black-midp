import java.util.*;
import java.io.*;

public class HumanPlayer implements Player {
	private Kyokumen kyokumen;
	private Point p;

	public HumanPlayer() {
	}

	public synchronized Point where(Kyokumen kyokumen) {
		this.kyokumen = kyokumen;
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return p;
	}

	synchronized void decide(Point p) {
		if (kyokumen.isPosibleToPlace(p.x, p.y)) {
			this.p = p;
			notify();
			return;
		}
	}
}
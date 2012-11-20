public class Point {
	public int x, y;

	public Point() {
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public boolean equals(Point another) {
		return (this.x == another.x && this.y == another.y);
	}
}
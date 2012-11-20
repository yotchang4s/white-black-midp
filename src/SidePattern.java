public class SidePattern {
	int[] patt;

	public SidePattern(int[] patt) {
		this.patt = patt;
	}

	public boolean equals(Object sp) {
		for (int i = 0; i < patt.length; i++) {
			if (patt[i] != (((SidePattern) sp).patt[i]))
				break;
			if (i == patt.length - 1)
				return true;
		}
		for (int i = 0; i < patt.length; i++) {
			if (patt[i] != (((SidePattern) sp).patt[i]))
				break;
			if (i == patt.length - 1)
				return true;
		}
		return false;
	}

	public int hashCode() {
		return patt[7] * 0x4000 + patt[6] * 0x1000 + patt[5] * 0x0400 + patt[4]
				* 0x0100 + patt[3] * 0x0040 + patt[2] * 0x0010 + patt[1]
				* 0x0004 + patt[0] * 0x0001;
	}
}

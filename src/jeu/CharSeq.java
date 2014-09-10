package jeu;

public class CharSeq implements CharSequence {
	
	private final char[] str;
	private static final char[] NUMBERS = {'0','1','2','3','4','5', '6', '7', '8', '9'};
	

	public CharSeq(char[] str) {
		super();
		this.str = str;
	}

	public char[] getStr() {
		return str;
	}

	@Override
	public char charAt(int index) {
		return str[index];
	}
	
	@Override
	public int length() {
		return str.length;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return null;
	}

	public void charSet(int index, int tmpInt) {
		if (tmpInt >= 0 && tmpInt <= 9) 
			str[index] = NUMBERS[tmpInt];
	}

}

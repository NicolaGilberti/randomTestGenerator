

import java.util.ArrayList;
import java.util.Timer;


public class Class0Instr {
	public static ArrayList<Integer> checker;

	boolean flag = true;

	String word = "test";

	double value = 0.5;

	public Class0Instr() {
		checker = new ArrayList();
	}

	public void testMe(String s, double i, boolean b, Timer l) {
		if (i <= (value)) {
			checker.add(0);
			System.out.println("Int OK");
		}
		if (s.equals(this.word)) {
			checker.add(1);
			System.out.println("Stringa!");
		}
		if ((i == (value)) && (b == (flag))) {
			checker.add(2);
			System.out.println("Int OK & Flag true p2");
		}else {
			checker.add(3);
			System.out.println("Antani anche per lei");
		}
	}

	public void testMe_Pt2(String s, int i, boolean b) {
		if (!(s.equals(i))) {
			checker.add(5);
			if (!b) {
				checker.add(4);
			}
		}
	}

	public ArrayList getChecker() {
		return checker;
	}

	public int setChecker() {
		checker.clear();
		return 0;
	}
}


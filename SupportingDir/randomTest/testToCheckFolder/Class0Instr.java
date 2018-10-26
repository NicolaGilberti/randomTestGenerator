

import java.util.ArrayList;
import java.util.Timer;


public class Class0Instr {
	public static ArrayList<Integer> checker;

	boolean flag = true;

	String word = "test";

	double value = 0.5;

	Class0Instr.Day day = Class0Instr.Day.SUNDAY;

	public Class0Instr() {
		checker = new ArrayList();
	}

	public void testMe(String s, double i, boolean b, Timer l, Class0Instr.Day d) {
		if (i <= (value)) {
			checker.add(0);
			System.out.println("Int OK");
		}
		checker.add(5);
		if (s.equals(this.word)) {
			checker.add(1);
			System.out.println("Stringa!");
		}
		checker.add(6);
		if ((i == (value)) && (b == (flag))) {
			checker.add(2);
			System.out.println("Int OK & Flag true p2");
		}else {
			checker.add(3);
			System.out.println("Antani anche per lei");
		}
		if (d == (day)) {
			checker.add(4);
			System.out.println("A good day to die!");
		}
		checker.add(7);
	}

	public void testMe_Pt2(String s, int i, boolean b) {
		if (!(s.equals(i))) {
			checker.add(10);
			System.out.println("Son diversi!");
			if (!b) {
				checker.add(8);
				System.out.println("falzo!");
			}
			checker.add(9);
		}
		checker.add(11);
	}

	public enum Day {
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;}

	public ArrayList getChecker() {
		return checker;
	}

	public int setChecker() {
		checker.clear();
		return 0;
	}
}


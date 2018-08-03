import org.junit.Test;
import java.util.ArrayList;
import java.util.Timer;

public class Tester{

	@Test
	//test case 0 coverage: 0.7
	//branch covered: [3, 4, 5, 6, 7, 8, 9]
	public void test0(){
	Class0Instr obj = new Class0Instr();

		String var110 = "test";
		int var111 = 1832633683;
		boolean var112 = false;
	obj.testMe_Pt2(var110,var111,var112);

		String var210 = "OTMGYew9xdl";
		double var211 = 0.740038377209773;
		boolean var212 = var112;
		Timer var213 = new Timer();
	obj.testMe(var210,var211,var212,var213);

	}
	@Test
	//test case 6 coverage: 0.3
	//branch covered: [0, 1, 2]
	public void test1(){
	Class0Instr obj = new Class0Instr();

		String var110 = "test";
		double var111 = 0.5;
		boolean var112 = true;
			String var120 = "ZvEPC3";
			boolean var121 = true;
		Timer var113 = new Timer(var120,var121);
	obj.testMe(var110,var111,var112,var113);

		String var210 = "x2IaJxXPM";
		double var211 = 0.39308724253336746;
		boolean var212 = var121;
			String var220 = "hMf7q1ZaKkB3ZyotB1h";
		Timer var213 = new Timer(var220);
	obj.testMe(var210,var211,var212,var213);

	}
}
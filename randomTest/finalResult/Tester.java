import org.junit.Test;
import java.util.ArrayList;
import java.util.Timer;

public class Tester{

	@Test
	//test case 0 coverage: 0.8333333333333334
	//branch covered: [0, 1, 3, 4, 5]
	public void test0(){
	Class0Instr obj = new Class0Instr();

		String var110 = "test";
		int var111 = 1445628440;
		boolean var112 = true;
	obj.testMe_Pt2(var110,var111,var112);

		String var210 = var110;
		double var211 = 0.5;
		boolean var212 = false;
			String var220 = "nCdkU2Ciy3";
			boolean var221 = var212;
		Timer var213 = new Timer(var220,var221);
	obj.testMe(var210,var211,var212,var213);

		String var310 = var110;
		int var311 = var111;
		boolean var312 = true;
	obj.testMe_Pt2(var310,var311,var312);

		String var410 = var220;
		int var411 = var111;
		boolean var412 = false;
	obj.testMe_Pt2(var410,var411,var412);

	}
	@Test
	//test case 1 coverage: 0.16666666666666666
	//branch covered: [2]
	public void test1(){
	Class0Instr obj = new Class0Instr();

		String var110 = "Wpc";
		double var111 = 0.5;
		boolean var112 = true;
			String var120 = var110;
		Timer var113 = new Timer(var120);
	obj.testMe(var110,var111,var112,var113);

		String var210 = var110;
		int var211 = 2126536601;
		boolean var212 = true;
	obj.testMe_Pt2(var210,var211,var212);

		String var310 = "";
		int var311 = 329060016;
		boolean var312 = var212;
	obj.testMe_Pt2(var310,var311,var312);

	}
}
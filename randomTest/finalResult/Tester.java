import org.junit.Test;
import java.util.ArrayList;
import java.util.Timer;

public class Tester{

	@Test
	//test case 0 coverage: 0.6666666666666666
	//branch covered: [0, 1, 2, 5]
	public void test0(){
	Class0Instr obj = new Class0Instr();

		String var110 = "test";
		int var111 = 1602437499;
		boolean var112 = true;
	obj.testMe_Pt2(var110,var111,var112);

		String var210 = var110;
		double var211 = 0.5;
		boolean var212 = var112;
			boolean var220 = var112;
		Timer var213 = new Timer(var220);
	obj.testMe(var210,var211,var212,var213);

		String var310 = "Y5s5SLGpVJ0uVT";
		int var311 = var111;
		boolean var312 = var112;
	obj.testMe_Pt2(var310,var311,var312);

		String var410 = "1Onal8QKCUN";
		int var411 = var111;
		boolean var412 = true;
	obj.testMe_Pt2(var410,var411,var412);

		String var510 = var310;
		int var511 = 628587349;
		boolean var512 = true;
	obj.testMe_Pt2(var510,var511,var512);

		String var610 = var310;
		int var611 = var111;
		boolean var612 = true;
	obj.testMe_Pt2(var610,var611,var612);

	}
	@Test
	//test case 1 coverage: 0.3333333333333333
	//branch covered: [3, 4]
	public void test1(){
	Class0Instr obj = new Class0Instr();

		String var110 = "5xEDCB";
		double var111 = 0.2821930250452097;
		boolean var112 = true;
			boolean var120 = var112;
		Timer var113 = new Timer(var120);
	obj.testMe(var110,var111,var112,var113);

		String var210 = var110;
		int var211 = 946119194;
		boolean var212 = false;
	obj.testMe_Pt2(var210,var211,var212);

		String var310 = var110;
		int var311 = 390931433;
		boolean var312 = true;
	obj.testMe_Pt2(var310,var311,var312);

		String var410 = "R6k2mXLSZ5";
		double var411 = 0.5;
		boolean var412 = var312;
			String var420 = "test";
		Timer var413 = new Timer(var420);
	obj.testMe(var410,var411,var412,var413);

	}
}
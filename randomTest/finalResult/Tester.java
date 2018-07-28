import org.junit.Test;
import java.util.ArrayList;
import java.util.Timer;

public class Tester{

	@Test
	//test case 1 coverage: 1.0
	//branch covered: [0, 1, 2, 3, 4, 5]
	public void test0(){
	Class0Instr obj = new Class0Instr();

		String var110 = "test";
		double var111 = 0.5;
		boolean var112 = true;
			boolean var120 = false;
		Timer var113 = new Timer(var120);
	obj.testMe(var110,var111,var112,var113);

		String var210 = var110;
		double var211 = 0.5426149651908859;
		boolean var212 = true;
			boolean var220 = true;
		Timer var213 = new Timer(var220);
	obj.testMe(var210,var211,var212,var213);

		String var310 = "LmLHhW";
		int var311 = 1270237995;
		boolean var312 = true;
	obj.testMe_Pt2(var310,var311,var312);

		String var410 = "xkj";
		int var411 = var311;
		boolean var412 = false;
	obj.testMe_Pt2(var410,var411,var412);

		String var510 = var110;
		double var511 = var211;
		boolean var512 = false;
		Timer var513 = new Timer();
	obj.testMe(var510,var511,var512,var513);

	}
}
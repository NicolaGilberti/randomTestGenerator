
import java.util.*;

public class Class0{
	// attributes
	boolean flag = true;
	String word = "test";
	double value = 0.5;
	Day day = Day.SUNDAY;
	public Class0() {
		
	}
	
//function X
	public void testMe(String s,double i,boolean b, Timer l, Day d){
		/*String s="a";
		int i=0;
		boolean b=false;*/
		if( i <= value){
			System.out.println("Int OK");
		}
		if(s.equals(this.word) ){
			System.out.println("Stringa!");
		}

		if(i == value && b == flag){
			System.out.println("Int OK & Flag true p2");
		}
		else {
			System.out.println("Antani anche per lei");
		}
		if(d == /*Day.SUNDAY*/day) {
			System.out.println("A good day to die!");
		}
	}
	
	public void testMe_Pt2(String s, int i, boolean b){
		if(!s.equals(i)) {
			System.out.println("Son diversi!");
			if(!b) {
				System.out.println("falzo!");
				
			}
		}
	}
	public enum Day {
	    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	    THURSDAY, FRIDAY, SATURDAY 
	}
}

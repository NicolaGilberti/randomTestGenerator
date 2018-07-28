
import java.util.*;

public class Class0{
	// attributes
	boolean flag = true;
	String word = "test";
	double value = 0.5;
	
	public Class0() {
		
	}
	
//function X
	public void testMe(String s,double i,boolean b, Timer sb){
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
	}
	
	public void testMe_Pt2(String s, int i, boolean b){
		if(!s.equals(i)) {
			if(!b) {
				
			}
		}
	}
}

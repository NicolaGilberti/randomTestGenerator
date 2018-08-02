package support.test;

import java.util.ArrayList;
import java.util.HashSet;

import support.generator.*;

public class TestCase {
	//attributes
	private int id;
	private ArrayList<MethodTest> methodList;	
	private HashSet<Integer> branchCovered;
	private int nBranch;
	private Instantiator inst;
	
	/**
	 * Constructor 
	 * 
	 * @param id of the testcase
	 * @param n used for debug/analysis purposes -> it's the number of branch that a perfect test has to cover
	 */
	public TestCase(int id, int n) {
		this.id = id;
		this.nBranch = n;
		this.methodList = new ArrayList<MethodTest>();
		this.branchCovered = new HashSet<Integer>();
		this.inst = new Instantiator();
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the methodList
	 */
	public ArrayList<MethodTest> getMethodList() {
		return methodList;
	}

	/**
	 * @param methodList the methodList to set
	 */
	public void setMethodList(ArrayList<MethodTest> methodList) {
		this.methodList = methodList;
	}
	
	/**
	 * @param Method to add to the list of the test
	 */
	public void addMethod(MethodTest m) {
		this.methodList.add(m);
	}

	/**
	 * @return the branchCovered
	 */
	public HashSet<Integer> getBranchCovered() {
		return branchCovered;
	}

	/**
	 * @param branchCovered the branchCovered to set
	 */
	public void setBranchCovered(HashSet<Integer> branchCovered) {
		this.branchCovered = branchCovered;
	}
	
	/**
	 * @param i the branch number that we want to insert into the set
	 */
	public void addBranch(int i) {
		this.branchCovered.add(i);
	}

	/**
	 * @return the nBranch
	 */
	public int getnBranch() {
		return nBranch;
	}

	/**
	 * @param nBranch the nBranch to set
	 */
	public void setnBranch(int nBranch) {
		this.nBranch = nBranch;
	}

	/**
	 * 
	 * @param num that define the method number 
	 * @return the string formatted for a JUnit test
	 */
	public String TestFunction(int num) {
		String s = "";

		s+= "\t@Test\n";
		s+= "\t//test case " + this.getId() + " coverage: " + this.branchCovered.size()*1.0 / this.nBranch*1.0  + "\n";
		s+= "\t//branch covered: " + this.branchCovered + "\n";
		s+= "\tpublic void test"+num+"(){\n";

		//to do
		for(int u=0; u<this.methodList.size(); u++) {
			s+= this.methodList.get(u).toString(u);
			s+= "\n";
		}
		
		s+= "\t}";
		
		return s;
	}

	/**
	 * @return the inst
	 */
	public Instantiator getInst() {
		return inst;
	}

	/**
	 * @param inst the inst to set
	 */
	public void setInst(Instantiator inst) {
		this.inst = inst;
	}
}

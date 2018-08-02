package primary;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.URL;
import java.net.URLClassLoader;

import support.test.TestCase;
import support.test.TestCaseGenerator;

public class MainProgram {
	private static ArrayList<TestCase> finalTests;
	private static long testTimer;//millisecond
	private static String fileName;
	private static String filePath;
	private static String destDirectory = "target\\classes";
	private static String classpath;
	private static String outputDir;
	private static int maxNum;
	private static String appConfigPath = "program.properties";
	
	protected static boolean threadAlive = true;


	public static void main2(String[] args) {
		ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

    	System.out.println("boiacan del dio porco!\n");
        for(URL url: urls){
        	System.out.println(url.getFile()+"\n");
        }
    	System.out.println("la dea puttana!\n");
	}
	
	public static void main(String[] args){
		finalTests = new ArrayList<TestCase>();

		Properties appProps = new Properties();
		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error reading the " + appConfigPath + " file: " + e);
		}
		testTimer = Long.parseLong(appProps.getProperty("ExecutionTestTimer"));
		fileName = appProps.getProperty("FileName");
		filePath = appProps.getProperty("FilePath");
		maxNum = Integer.parseInt(appProps.getProperty("MaxNumberOfMethodXTest"));
		classpath = appProps.getProperty("Classpath","");
		outputDir = appProps.getProperty("OutputDir","finalResult");

		LinkedBlockingQueue<TestCase> cLQ = new LinkedBlockingQueue<TestCase>();

		SecThread st = new SecThread("Thread", cLQ, testTimer, fileName, filePath, maxNum, classpath, destDirectory);
		st.start();
		boolean flag=true;
		boolean fluffa = true;//used to understand first cycle
		String importString = "";
		while(flag) {
			try {
				TestCase o;
				if(!threadAlive && cLQ.isEmpty()){
					flag=false;
				}
				if(cLQ.size()>0) {
					o = cLQ.take();
					if(fluffa) {
						importString = st.getImportString();
						fluffa=false;
					}
					AddToFinalTestCase(o);

					if(isCoverageAlreadyComplete()) {
						st.stop();
						threadAlive = false;
						cLQ.clear();
					}
				}
			} catch (Exception e) {
				System.err.println("main interrupt " + e);
				flag=false;
			}
		}

		checkTestValues();
		
		TestCaseGenerator tCG = new TestCaseGenerator();
		tCG.setOutputDir(outputDir);
		tCG.setImportList(importString);
		tCG.setTestList(finalTests);
		tCG.GenerateTestCaseFile();

		System.exit(0);
	}

	/**
	 * 
	 * This function is used to analyze tests, to assess if they are covering all the branches (T) or not (F)
	 * 
	 * @return a boolean value defining if the test suite cover all the branches
	 */
	private static boolean isCoverageAlreadyComplete() {
		HashSet<Integer> hs = new HashSet<Integer>();
		int n = -1;
		checkTestValues();
		for(TestCase tc : finalTests) {
			hs.addAll(tc.getBranchCovered());
			n = tc.getnBranch();
		}
		int qw=0;
		boolean whileFlag = true;
		while(whileFlag && qw<n) {
			if(!hs.contains(qw)) {
				whileFlag=false;
			}
			qw++;
		}
		return whileFlag;
	}

	/**
	 * This function analyze the test in input and it verify if it can be inserted or not into the final test list
	 * 
	 * @param newTest The Test that can be inserted into the list 
	 */
	private static void AddToFinalTestCase(TestCase newTest) {
		HashSet<Integer> tmp1 = newTest.getBranchCovered();
		boolean flag=true;
		if(finalTests.isEmpty()) {
			finalTests.add(newTest);
		}else {
			for(int i=0;i<finalTests.size();i++) {
				TestCase oldTest = finalTests.get(i);
				if(sameValues(oldTest.getBranchCovered(), newTest.getBranchCovered())) {
					flag=false;
					if(oldTest.getMethodList().size()>newTest.getMethodList().size()) {/////sto prendendo i piu grandi per test usando il <
						finalTests.set(i, newTest);
					}else if(oldTest.getMethodList().size() == newTest.getMethodList().size() && (new Random()).nextInt(10)%2==0) {
						finalTests.set(i, newTest);
					}
					break;
				}else {
					if(newTest.getBranchCovered().containsAll(oldTest.getBranchCovered())) {
						finalTests.set(i, newTest);
						flag=false;
						break;
					}else if(oldTest.getBranchCovered().containsAll(newTest.getBranchCovered())){
						flag=false;
						break;
					}else {
						tmp1.removeAll(oldTest.getBranchCovered());
					}
				}
			}
			if(!tmp1.isEmpty() && flag) {
				finalTests.add(newTest);
			}
		}

	}

	/**
	 * This function is used to analyze the test list
	 * it is used to remove possible duplicates
	 */
	private static void checkTestValues() {
		for(int i=0;i<finalTests.size();i++) {//for each element
			TestCase oldTest = finalTests.get(i);
			for(int j=0;j<finalTests.size();j++) {//on all the other test
				TestCase newTest = finalTests.get(j);
				if(i!=j) {
					if(sameValues(oldTest.getBranchCovered(), newTest.getBranchCovered())) {
						if(oldTest.getMethodList().size()>newTest.getMethodList().size()) {
							finalTests.remove(i);
						}else{
							finalTests.remove(j);
						}
					}else {
						if(newTest.getBranchCovered().containsAll(oldTest.getBranchCovered())) {
							finalTests.remove(i);
						}else if(oldTest.getBranchCovered().containsAll(newTest.getBranchCovered())){
							finalTests.remove(j);
						}
					}
				}
			}
		}
	}

	/**
	 * this method is a 'equal' like function optimized for this program
	 * it take two HashSet has input and it return if they are equal or not.
	 * 
	 * @param hashSet 
	 * @param hashSet2 
	 * @return a boolean value that define the equality (T) or not (F) of the input
	 */
	public static boolean sameValues(HashSet<Integer> hashSet, HashSet<Integer> hashSet2) {
		//null checking
		if(hashSet==null && hashSet2==null) {
			return true;
		}
		if((hashSet == null && hashSet2 != null) || (hashSet != null && hashSet2 == null)) {
			return false;
		}
		if(hashSet.size()!=hashSet2.size()) {
			return false;   
		}
		for(Integer itemList1: hashSet){
			if(!hashSet2.contains(itemList1)) {
				return false;
			}
		}
		return true;
	}
}
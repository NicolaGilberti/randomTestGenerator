package primary;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.lang.Math;

import org.apache.commons.io.FileUtils;
import org.jgrapht.graph.DirectedPseudograph;

import graph.GraphImporter;

import java.io.File;
import java.io.StringReader;

import support.test.TestCase;
import support.test.TestCaseGenerator;
import support.generator.ClassPathManager;

public class MainProgram {

	private static String fileSeparator = System.getProperty("file.separator"); // in Unix '/', in Windows '\'
	private static String pathSeparator = System.getProperty("path.separator"); // in Unix ':', in Windows ';'

	private static ArrayList<TestCase> finalTests;
	private static long testTimer;//millisecond
	private static String fileName;
	private static String projectName;
	private static String pathToProjectDir; 
	private static String destDirectory = "target" + fileSeparator + "classes";
	private static String mavenDirPath = "src" + fileSeparator + "main" + fileSeparator + "java";
	private static String fileJavaPath;
	private static String fileClassPath;
	private static String outputDir;
	private static int maxNum;
	private static String appConfigPath = "program.properties";
	private static String pathToGraphFile;
	private static DirectedPseudograph<String, String> navigationGraph;
	private static String startNodeGraph;
	private static String requiredPath;
	private static String lineToCover;
	private static String separator;

	protected static boolean threadAlive = true;

	public static void main(String[] args){

		finalTests = new ArrayList<TestCase>();

		Properties appProps = new Properties();
		try {
			File propFile = new File("program.properties");
			List<String> propertyFileContentsList = Files.readAllLines(Paths.get(propFile.getAbsolutePath()));
			String propertyFileContents = "";
			for(String pfcle: propertyFileContentsList) {
				propertyFileContents += pfcle + "\n";
			}
			appProps.load(new StringReader(propertyFileContents.replace("\\","\\\\")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error reading the " + appConfigPath + " file: " + e);
		}

		fileName = appProps.getProperty("FileName");
		projectName = appProps.getProperty("ProjectName");
		pathToProjectDir = appProps.getProperty("PathToProjectDir");
		String fileNameDirMode =  "";
		String[] fndotList = fileName.split("\\.");
		for(String fndotElem : fndotList) {
			fileNameDirMode += fndotElem + fileSeparator;
		}

		fileJavaPath = pathToProjectDir + fileSeparator + projectName + fileSeparator + mavenDirPath;
		fileClassPath = pathToProjectDir + fileSeparator + projectName + fileSeparator + destDirectory;

		requiredPath =  "." + pathSeparator + pathToProjectDir + fileSeparator + projectName + fileSeparator + destDirectory + pathSeparator;
		requiredPath += appProps.getProperty("RequiredPath");
		//System.out.println(requiredPath.replaceAll(pathSeparator, "\n"));

		/**
		 * 
		 * ANOTHER WAY TO ADD CLASSPATHS DINAMICALLY (SETPROPERTY DOESN'T WPRK)
		 * System.setProperty("java.class.path",requiredPath);
		 * 
		 */
		String[] paths = requiredPath.split(pathSeparator);
		for(String pth : paths) {
			try {
				ClassPathManager.addFile(pth);
			}catch(IOException e) {
				System.err.println("MainProgram: error with the ClassPathManager " + e);
			}
		}

		testTimer = Long.parseLong(appProps.getProperty("ExecutionTestTimer"));
		maxNum = Integer.parseInt(appProps.getProperty("MaxNumberOfMethodXTest"));
		lineToCover = appProps.getProperty("LineToCover");
		separator = appProps.getProperty("Separator",":");

		outputDir = appProps.getProperty("OutputDir","webrandomgenerator-report");
		// Import navigation graph
		pathToGraphFile = appProps.getProperty("GraphDirPath") + fileSeparator + appProps.getProperty("GraphName") + ".txt";
		GraphImporter graphImporter = new GraphImporter(pathToGraphFile);
		Optional<DirectedPseudograph<String, String>> optionalNavigationGraph = graphImporter.importGraphDotFile();
		if(optionalNavigationGraph.isPresent()){
			navigationGraph = optionalNavigationGraph.get();
		}else{
			throw new IllegalStateException("");
		}
		startNodeGraph = appProps.getProperty("StartNodeGraph");

		LinkedBlockingQueue<TestCase> cLQ = new LinkedBlockingQueue<TestCase>();

		SecThread st = new SecThread("Thread", cLQ, testTimer, fileName, fileJavaPath, fileClassPath, maxNum, requiredPath, destDirectory, navigationGraph, startNodeGraph, lineToCover, separator);
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

		showTestCoverage(finalTests);

		try {
			String className = fileName;
			String fileJavaPathPack = fileJavaPath;
			String fileClassPathPack = fileClassPath;
			if(fileName.contains(".")) {
				className = fileName.substring(fileName.lastIndexOf(".")+1);
				String packagePath = fileName.substring(0, fileName.lastIndexOf("."));
				packagePath.replaceAll(Pattern.quote("\\."), fileSeparator);
				fileJavaPathPack += fileSeparator + packagePath;
				fileClassPathPack += fileSeparator + packagePath;
			}
			FileUtils.deleteDirectory(new File("spoon"));
			/*File jf = new File(fileJavaPathPack + fileSeparator + className + "Instr.java");
			File cf = new File(fileClassPathPack + fileSeparator + className + "Instr.class");
			FileUtils.deleteQuietly(jf);
			FileUtils.deleteQuietly(cf);*/
		}catch(Exception e) {
			System.err.println("error removing tempor spoon dir " + e);
		}
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

	/**
	 * Print in console (System.out "standard") 
	 * the coverage performed by the list of TestCase
	 * 
	 * @param ft ArrayList of the tests 
	 */
	public static void showTestCoverage(ArrayList<TestCase> ft) {
		int den = ft.get(0).getnBranch();
		HashSet<Integer> num = new HashSet<Integer>();
		for(TestCase t : ft) {
			num.addAll(t.getBranchCovered());
		}
		System.out.println("Total coverage of the tests is => " + (int)Math.round(num.size()*1.0/den*1.0*100));
	}
}

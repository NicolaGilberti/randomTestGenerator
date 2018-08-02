package support.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class TestCaseGenerator {

	private String className = "Tester";
	private String importList;
	private String outputDir;
	private ArrayList<TestCase> testList;
	
	/**
	 * @return the outputDir
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * @param outputDir the outputDir to set
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	/**
	 * @return the testList
	 */
	public ArrayList<TestCase> getTestList() {
		return testList;
	}

	/**
	 * @param testList the testList to set
	 */
	public void setTestList(ArrayList<TestCase> testList) {
		this.testList = testList;
	}

	/**
	 * This method write a string formatted for a JUnit test  
	 * 
	 * @return the string of the JUnit class
	 */
	public String generateTestCaseString() {
		String s= "import org.junit.Test;\n" + importList + "\n";

		s += "public class " + className + "{\n\n";

		for (int i = 0; i < testList.size(); i++) {
			TestCase t = testList.get(i);
			s+= t.TestFunction(i) + "\n";
		}

		s += "}";

		return s;
	}

	/**
	 * This method create an output file for the JUnit test
	 */
	public void GenerateTestCaseFile() {
		String s = generateTestCaseString();
		File root = new File(outputDir);
		root.mkdirs();
		File sourceFile = new File(root, className + ".java");
		try {
			Files.write(sourceFile.toPath(), s.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: file In/Out failed:" + e);
		}
	}

	/**
	 * To set the importList 
	 * @param importString
	 */
	public void setImportList(String importString) {
		// TODO Auto-generated method stub
		importList = importString;
	}
}

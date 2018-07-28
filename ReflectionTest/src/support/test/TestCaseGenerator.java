package support.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class TestCaseGenerator {
	private static ArrayList<TestCase> testList;
	private static String className = "Tester";
	private static String importList;

	public static ArrayList<TestCase> getTestList() {
		return testList;
	}

	public static void setTestList(ArrayList<TestCase> tList) {
		testList = tList;
	}
	
	public static String generateTestCaseString() {
		String s=importList+"\n";
		
		s += "public class " + className + "{\n\n";
		
		for (int i = 0; i < testList.size(); i++) {
			TestCase t = testList.get(i);
			s+= t.TestFunction(i) + "\n";
		}
		
		s += "}";
		
		return s;
	}
	
	public static void GenerateTestCaseFile() {
		String s = generateTestCaseString();
		File root = new File("./finalResult");
		root.mkdirs();
		File sourceFile = new File(root, className + ".java");
		try {
			Files.write(sourceFile.toPath(), s.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: file In/Out failed:");
			e.printStackTrace();
		}
	}

	public static void setImportList(String importString) {
		// TODO Auto-generated method stub
		importList = importString;
	}
}

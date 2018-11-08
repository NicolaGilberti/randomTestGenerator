package primary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import java.io.FileWriter;

import spoon.reflect.declaration.CtClass;

import org.jgrapht.graph.DirectedPseudograph;

import spoon.reflect.factory.Factory;
import support.generator.Instantiator;
import support.generator.ParamSaver;
import support.spoon.ClassModifier;
import support.spoon.SpoonMod;
import support.test.MethodTest;
import support.test.TestCase;
import utils.Randomness;
import graph.GraphImporter;
import graph.GraphParser;
import graph.PathGenerator;

public class SecThread extends Thread {

	String fileSeparator = System.getProperty("file.separator"); // in Unix '/', in Windows '\'
	String pathSeparator = System.getProperty("path.separator"); // in Unix ':', in Windows ';'

	private String fileName;
	private String fileJavaPath;
	private String fileClassPath;
	private LinkedBlockingQueue<TestCase> clq;
	private long execTime;
	private int id=0;
	private int nBranch;
	private Object instance;
	private Class<?> modifiedClass;
	private Random random = new Random();
	private String destDirectory;
	private String classpath;
	private int maxNumMethods;
	private ClassModifier loader;
	private String importString;
	private SpoonMod sm;
    private static DirectedPseudograph<String, String> navigationGraph;
	private static String startNodeGraph;
	private static String className;
	private static String fileJavaPathPack;
	private static String fileClassPathPack;
	private static String packagePath;
	private static String modelSrcDirPack;
	private static String modelBinDirPack;
	private static String lineToCover;
	private static String separator;
	private static long endTime;
	private static HashSet<Integer> covDurTim = new HashSet<Integer>();


	public SecThread(String name, LinkedBlockingQueue<TestCase> c, long time, String fn, String fjp, String fcp, int mnm, String cp, String dd, DirectedPseudograph<String, String> ng, String sng, String ltc, String s) {
		clq = c;
		execTime = time;
		fileName = fn;
		fileJavaPath = fjp;
		fileClassPath = fcp;
		maxNumMethods = mnm;
		loader = new ClassModifier();
		loader.setModelSrcDir("spoon");
		loader.setModelBinDir(dd);
		classpath = cp;
		destDirectory = dd;
		sm = new SpoonMod();
		navigationGraph = ng;
		startNodeGraph = sng;
		className = fileName;
		fileJavaPathPack = fileJavaPath;
		fileClassPathPack = fileClassPath;
		packagePath = "";
		modelSrcDirPack = loader.getModelSrcDir();
		modelBinDirPack = loader.getModelBinDir();
		if(fileName.contains(".")) {
			className = fileName.substring(fileName.lastIndexOf(".")+1);
			packagePath = fileName.substring(0, fileName.lastIndexOf("."));
			packagePath.replaceAll(Pattern.quote("\\."), fileSeparator);
			fileJavaPathPack += fileSeparator + packagePath;
			fileClassPathPack += fileSeparator + packagePath;
			modelSrcDirPack += fileSeparator + packagePath;
			modelBinDirPack += fileSeparator + packagePath;
		}
		lineToCover = ltc;
		separator = s;
	}

	@Override
	public void run() {
		super.run();
		
		instantiateInstrumentedClass();
		//loader.setTheClassLoader(fileClassPath);
		//modifiedClass = loader.load(fileName+"Instr");
		try{
			modifiedClass = Class.forName(fileName+"Instr");
		}catch(ClassNotFoundException e){
			System.err.println("SecThread.run: class not found error " + e);
		}
		
		/**
		 * find all the import
		 */
		File root = new File(modelSrcDirPack);
		File sourceFile = new File(root, className+".java");
		List<String> ls=null;
		try {
			ls = Files.readAllLines(sourceFile.toPath(), Charset.defaultCharset());
		} catch (IOException e) {
			System.err.println("Error reading instrumented class for import purposes ");
		}
		boolean isImport = false;
		importString = "";

		for(String strTmp : ls) {
			if(strTmp.contains("import")) {
				importString += strTmp + "\n";
				isImport = true;
			}
			else if(isImport){
				break;
			}
		}
		ls.clear();
		
		endTime = System.currentTimeMillis() + this.execTime;
		while (System.currentTimeMillis() < endTime) {
			testMethods();
		}
		MainProgram.threadAlive=false;
		this.interrupt();
	}

	public void testMethods() {
		TestCase test = new TestCase(id++,nBranch);
		Instantiator inst = test.getInst();
		sm.findVar(inst);
		Method method=null;
		Method[] list=modifiedClass.getMethods();
		HashMap<String, Method> listM = new HashMap<String, Method>();
		Method methodC=null;
		Method methodS=null;
		int y=0;
		while(y<list.length) {
			if(list[y].getName().equals("getChecker")) {
				methodC = list[y];
			}else if(list[y].getName().equals("setChecker")) {
				methodS = list[y];
			}else {
				listM.put(list[y].getName(), list[y]);
			}
			y++;
		}
		
		Object[] tmpp;
		boolean shish = true;
		int maxNumMethodsXTest = maxNumMethods+2;
		try {

			int constrNum = modifiedClass.getConstructors().length;
			Constructor<?> methodc = modifiedClass.getConstructors()[random.nextInt(constrNum)];
			MethodTest mtc = new MethodTest(methodc);

			Class<?>[] parametersc = methodc.getParameterTypes();
			Object[] tmpc = new Object[parametersc.length];
			ParamSaver kc = inst.istantiatedArray(	tmpc, parametersc, 1, 0);	
			mtc.addParameter(kc.getParamString());
			mtc.setParamUsed(kc.getParamArray());
			test.addMethod(mtc);
			/*
			 * when the project was talking about random method calls.
			 * 
			 * instance = methodc.newInstance( (Object[])kc[0]);
			int l=0,methodCounter=1;//from 1 because constructor is  0 (line 132)
			while(shish) {
				int i = l%listM.length;
				if(!random.nextBoolean()) {

					method=listM[i];
					MethodTest mtm = new MethodTest(method);
					Class<?>[] parameters = method.getParameterTypes();
					tmpp = new Object[parameters.length];

					ParamSaver km = inst.istantiatedArray(tmpp, parameters, 1, methodCounter);
					methodCounter++;

					mtm.addParameter(km.getParamString());
					mtm.setParamUsed(km.getParamArray());
					test.addMethod(mtm);

				}
				l++;
				if(l == maxNumMethodsXTest-1) {
					shish = false;
				}
			}*/
			Set<String> edgeSet = navigationGraph.edgeSet();
	        String randomNavigationMethod = Randomness.getInstance().choice(edgeSet);
	        PathGenerator pathGenerator = new PathGenerator();
	        List<String> listOfNavigationMethods = pathGenerator.generateRandomPathWithTarget(navigationGraph, startNodeGraph,randomNavigationMethod, maxNumMethods-1);

			int methodCounter=1;//from 1 because constructor is  0 (line 157)
			for(String methodString : listOfNavigationMethods) {
				String[] methodData = methodString.split("-");
				String methodName = methodData[0];
				String startingNode = methodData[1];
				String OuterNode = methodData[2];
				
				method = listM.get(methodName);
				
				MethodTest mtm = new MethodTest(method);
				Class<?>[] parameters = method.getParameterTypes();
				tmpp = new Object[parameters.length];
				ParamSaver km = inst.istantiatedArray(tmpp, parameters, 1, methodCounter);
				methodCounter++;

				mtm.addParameter(km.getParamString());
				mtm.setParamUsed(km.getParamArray());
				test.addMethod(mtm);
			}
			
			/**
			 * check test and if it is acceptable run it
			 * 
			 */
			if(isReliable(test)) {
				ArrayList<MethodTest> mListTemp = test.getMethodList();
				for(int im = 0; im < mListTemp.size(); im++) {
					MethodTest x = mListTemp.get(im);
					try {
						Executable exec = x.getMethod();
						if(exec instanceof Constructor) {
							Constructor<?> c = (Constructor<?>) exec;
								instance = c.newInstance(x.getParamUsed());
						}else if(exec instanceof Method) {
							Method m = (Method)exec;
								//System.out.println(instance.getClass().getName());
							m.invoke(instance, x.getParamUsed());
						}
					}catch(InvocationTargetException  e) {
						//do stuff or do nothing?
						x.setException(e.getCause().getClass().getName());
						mListTemp.remove(im);
						mListTemp.add(im, x);
						//System.err.println("error generated by the method registered!");
					} catch (IllegalAccessException | IllegalArgumentException | InstantiationException e ) {
						// TODO Auto-generated catch block
						System.err.println("error invoke method/constructor secThread " + e);
						System.err.println("Param:" +  x.getParameterUsed());
					}
				}
				test.setMethodList(mListTemp);
				//get the arrayList value for the branch coverage
				Class<?>[] parametersC = methodC.getParameterTypes();
				Object[] tmpC = new Object[parametersC.length];
				ParamSaver kC = inst.istantiatedArray(tmpC, parametersC, 1, 0);
				ArrayList<?> a= new ArrayList<>();
				try {
					a = (ArrayList<?>) methodC.invoke(instance, kC.getParamArray());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					System.err.println("error invoke getChecker secThread" + e);
				}
				for(int j=0;j<a.size();j++) {
					test.addBranch((Integer)a.get(j));
				}
				//reset the arraylist to clear the memory
				Class<?>[] parametersS = methodS.getParameterTypes();
				Object[] tmpS = new Object[parametersS.length];
				ParamSaver kS = inst.istantiatedArray(tmpS, parametersS, 1, 0);
				try {
					methodS.invoke(instance, kS.getParamArray());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					System.err.println("error invoke setChecker secThread" + e);
				}
			}
			//reset the app with the default class&method
			try {
				Class<?> reset = Class.forName("po_utils.ResetAppState");
				Method resetM = reset.getMethod("reset",null);
				resetM.invoke(reset, null);
			}catch(Exception e) {
				System.out.println("Something bad happened during the reset!");
			}
			//add to the blocking queue the new test, to send it to the primary thread
			/**
			 * percentuale di coverage di ogni test creato e percentuale sul tempo
			 * 
			 * TODO: adjust the files
			 */
			long startTime = this.endTime - this.execTime;
			double lapse = (double)(System.currentTimeMillis() - startTime) / (double)this.execTime;
			double timePerc = lapse * 100;
			this.covDurTim.addAll(test.getBranchCovered());
			double covPerc = (double)this.covDurTim.size() / (double)this.nBranch;
			String percentage = "time passed: " + timePerc + "% actual coverage: " + covPerc + "\n";
			System.err.println(percentage);
			try {
			if(this.id==1) {
				FileWriter p = new FileWriter("covFile.txt",false);
				p.write("");
				p.close();
			}
			FileWriter p = new FileWriter("covFile.txt",true);
			p.write(percentage);
			p.close();
			}catch(Exception e){
				System.err.println("Error inn the file");
			}
			
			
			clq.add(test);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.err.println("Errore al test numero "+(id-1)+"\n");
			test = null;
		}
	}

	/**
 	 * This method modify the input class, instrumenting the class and recompiling it
 	 * 
	 */
	public void instantiateInstrumentedClass() {
		try {
			File rootz = new File(fileJavaPathPack);
			File sourceFilez = new File(rootz, className+".java");
			loader.addSourcePath(fileJavaPath);

			Class<?> classToLoad = null;
			try{
				classToLoad = Class.forName(fileName);
			}catch(ClassNotFoundException e){
				System.err.println("SecThread.instantiateInstrumentedClass: class not found error " + e);
			}
			Factory f = loader.getFactory();
			sm.setFactory(f);
			/**
			 * Branch coverage
			 * nBranch = sm.modifyForBranchCoverage(classToLoad);
			 */
			String[] lineToCoverList = lineToCover.split(separator);
			nBranch = lineToCoverList.length;
			sm.modifyForLineCoverage(classToLoad, lineToCoverList);
			loader.compile();

			File root = new File(modelSrcDirPack);
			File sourceFile = new File(root, className+".java");
			String k="";
			List<String> ls = Files.readAllLines(sourceFile.toPath(), Charset.defaultCharset());
			for(int i=0;i<ls.size();i++) {
				k = k.concat(ls.get(i));
				k = k.concat("\n");
			}

			k = k.replaceAll("\\Q"+className+" \\E",className+"Instr ");
			k = k.replaceAll("\\Q"+className+"<\\E",className+"Instr<");
			k = k.replaceAll("\\Q"+className+"(\\E",className+"Instr(");
			k = k.replaceAll("\\Q"+className+"{\\E",className+"Instr{");
			k = k.replaceAll("\\Q"+className+".\\E",className+"Instr.");

			File root2 = new File(fileJavaPathPack);
			File sourceFile2 = new File(root2, className+"Instr.java");
			Files.write(sourceFile2.toPath(), k.getBytes(StandardCharsets.UTF_8));

			String [] source = { "-d", fileClassPath,"-cp", classpath, new String(fileJavaPathPack + fileSeparator + className + "Instr.java")};//filePath + "/" + 
			ByteArrayOutputStream baos= new ByteArrayOutputStream();
			com.sun.tools.javac.Main.compile(source);

			while(!(baos.toString().indexOf("error")==-1)) {}

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: Security problem! compiling part secThread " + e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: Argument inserted not correct! compiling part secThread " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: file In/Out failed! compiling part secThread " + e);
			//e.printStackTrace();
		}
	}

	/**
	 * @return the importString
	 */
	public String getImportString() {
		return importString;
	}

	/**
	 * @param importString the importString to set
	 */
	public void setImportString(String importString) {
		this.importString = importString;
	}

	/**
	 * Implementing Fisher Yates shuffle
	 * 
	 * @param ar array to shuffle
	 */
	static void shuffleArray(Method[] ar){
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--){
			int index = rnd.nextInt(i + 1);
			Method a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	/**
	 * to check the testcase before invoking it
	 * @param test
	 * @return
	 */
	private static boolean isReliable(TestCase test) {
		// TODO 

		return true;
	}
}

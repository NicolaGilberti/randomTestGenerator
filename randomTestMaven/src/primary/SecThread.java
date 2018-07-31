package primary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import spoon.reflect.factory.Factory;
import support.generator.Instantiator;
import support.generator.ParamSaver;
import support.spoon.ClassModifier;
import support.spoon.SpoonMod;
import support.test.MethodTest;
import support.test.TestCase;

public class SecThread extends Thread {

	private String fileName;
	private String filePath;
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


	public SecThread(String name, LinkedBlockingQueue<TestCase> c, long time, String fn, String fp, int mnm, String cp, String dd) {
		clq = c;
		execTime = time;
		fileName = fn;
		filePath = fp;
		maxNumMethods = mnm-2;
		loader = new ClassModifier();
		classpath = cp;
		destDirectory = dd;
		sm = new SpoonMod();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("Starting thread: "+this.name);
		super.run();
		instantiateInstrumentedClass();
		modifiedClass = loader.load(fileName+"Instr");

		/**
		 * find all the import
		 */
		File root = new File(ClassModifier.DEFAULT_MODEL_SRC_DIR);
		File sourceFile = new File(root, fileName+"Instr.java");
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
		
		long endTime = System.currentTimeMillis() + this.execTime;
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
		Method[] list=modifiedClass.getDeclaredMethods();
		Method[] listM = new Method[list.length-2];
		Method methodC=null;
		Method methodS=null;

		boolean removed=false;
		int y=0,w=0;
		while(!removed && y<list.length) {
			if(list[y].getName().equals("getChecker")) {
				methodC = list[y];
			}else if(list[y].getName().equals("setChecker")) {
				methodS = list[y];
			}else {
				listM[w] = list[y];
				w++;
			}
			y++;
		}
		shuffleArray(listM);
		
		Object[] tmpp;
		boolean shish = true;
		int maxNumMethodsXTest = maxNumMethods+2;
		try {

			int constrNum = modifiedClass.getConstructors().length;
			Constructor<?> methodc = modifiedClass.getConstructors()[random.nextInt(constrNum)];

			MethodTest mtc = new MethodTest(methodc);
			//Object instancec = modifiedClass.newInstance();
			Class<?>[] parametersc = methodc.getParameterTypes();
			Object[] tmpc = new Object[parametersc.length];

			ParamSaver kc = inst.istantiatedArray(tmpc, parametersc, 1, 0);	
			mtc.addParameter(kc.getParamString());
			mtc.setParamUsed(kc.getParamArray());
			test.addMethod(mtc);
			//instance = methodc.newInstance( (Object[])kc[0]);
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
							m.invoke(instance, x.getParamUsed());
						}
					}catch(InvocationTargetException  e) {
						//do stuff or do nothing?
						x.setException(e.getCause().toString());
						mListTemp.remove(im);
						mListTemp.add(im, x);
						System.err.println("error generated by the method registered!");
					} catch (IllegalAccessException | InstantiationException e ) {
						// TODO Auto-generated catch block
						System.err.println("error invoke method/constructor secThread" + e);
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
				//add to the blocking queue the new test, to send it to the primary thread
				clq.add(test);
			}
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
			loader.addSourcePath(filePath);
			Class<?> classToLoad = loader.load(fileName);

			Factory f = loader.getFactory();
			nBranch = sm.modify(classToLoad, f);
			loader.compile();

			File root = new File("./spoon/src");
			File sourceFile = new File(root, fileName+".java");
			String k="";
			List<String> ls = Files.readAllLines(sourceFile.toPath(), Charset.defaultCharset());
			for(int i=0;i<ls.size();i++) {
				k = k.concat(ls.get(i));
				k = k.concat("\n");
			}

			k = k.replaceAll("\\Q"+fileName+" \\E",fileName+"Instr ");
			k = k.replaceAll("\\Q"+fileName+"<\\E",fileName+"Instr<");
			k = k.replaceAll("\\Q"+fileName+"(\\E",fileName+"Instr(");
			k = k.replaceAll("\\Q"+fileName+"{\\E",fileName+"Instr{");

			File root2 = new File(filePath);
			File sourceFile2 = new File(root2, fileName+"Instr.java");
			Files.write(sourceFile2.toPath(), k.getBytes(StandardCharsets.UTF_8));

			String [] source = { "-d",destDirectory,"-cp",".;"+classpath,new String(filePath + "/" + fileName + "Instr.java")};
			ByteArrayOutputStream baos= new ByteArrayOutputStream();
			com.sun.tools.javac.Main.compile(source);

			while(!(baos.toString().indexOf("error")==-1)) {}

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: Security problem! compiling part secThread" + e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: Argument inserted not correct! compiling part secThread" + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: file In/Out failed! compiling part secThread" + e);
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

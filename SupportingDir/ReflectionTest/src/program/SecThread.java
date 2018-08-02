package program;

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
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import spoon.reflect.factory.Factory;
import support.Instantiator;
import support.ParamSaver;
import support.spoon.ClassModifier;
import support.spoon.SpoonMod;
import support.test.MethodTest;
import support.test.TestCase;

public class SecThread extends Thread {

	private static String fileName;
	private static String filePath;
	private static LinkedBlockingQueue<TestCase> clq;
	private static long execTime;
	private static int id=0;
	private static int nBranch;
	private static Object instance;
	private static Class<?> modifiedClass;
	private static Random random = new Random();
	private static String classpath = "";
	private static int maxNumMethods;
	private static ClassModifier loader;
	private static String importString;


	public SecThread(String name, LinkedBlockingQueue<TestCase> c, long time, String fn, String fp, int mnm) {
		clq = c;
		execTime = time;
		fileName = fn;
		filePath = fp;
		maxNumMethods = mnm-2;
		loader = new ClassModifier();
	}

	@SuppressWarnings("deprecation")
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
		File root = new File("./spoon/src");
		File sourceFile = new File(root, fileName+"Instr.java");
		List<String> ls=null;
		try {
			ls = Files.readAllLines(sourceFile.toPath(), Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
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
				/******************/
				/** System.gc(); **/
				/******************/
		
		/*while(scan.hasNext() && whileFlag) {
			String strTmp = scan.next();
			if(strTmp.contains("import")) {
				isImport=true;
			}else if(isImport) {
				importString += "import " + strTmp + "\n";
				isImport = false;
			}else {
				whileFlag=false;
			}
		}*/
		//System.out.println(importString);
		
		SpoonMod.findVar();
		long endTime = System.currentTimeMillis() + SecThread.execTime;
		while (System.currentTimeMillis() < endTime) {
			testMethods();
		}
	}

	public static void testMethods() {
		TestCase test = new TestCase(id++,nBranch);
		Method method=null;
		Method[] listM=modifiedClass.getDeclaredMethods();
		Object[] tmpp;
		boolean shish = true;
		int maxNumMethodsXTest = random.nextInt(maxNumMethods)+2;
		try {
			
			int constrNum = modifiedClass.getConstructors().length;
			Constructor<?> methodc = modifiedClass.getConstructors()[random.nextInt(constrNum)];

			MethodTest mtc = new MethodTest(methodc);
			//Object instancec = modifiedClass.newInstance();
			Class<?>[] parametersc = methodc.getParameterTypes();
			Object[] tmpc = new Object[parametersc.length];

			//			System.out.println(parametersc.length);
			//			System.out.println(parametersc[0].getName());

			ParamSaver kc = Instantiator.istantiatedArray(tmpc, parametersc, 1);	
			mtc.addParameter(kc.getParamString());
			mtc.setParamUsed(kc.getParamArray());
			test.addMethod(mtc);
			//instance = methodc.newInstance( (Object[])kc[0]);
			int l=0;
			while(shish) {//loop per testare tutti i metodi sostituire con loop di metodi random
				//System.out.println(listM[i].getName());
				int i = l%listM.length;
				if(!listM[i].getName().equals("getChecker") && !listM[i].getName().equals("setChecker") && random.nextInt(50)%2==0) {//li ho inseriti io, conosco i loro nomi

					//System.out.println(listM[i].getName());
					method=listM[i];
					MethodTest mtm = new MethodTest(method);
					/**
					 * instanziazione di una class
					 */
					//  Object instance = modifiedClass.newInstance();
					/**
					 * come conoscere quali parametri necessita il metodo ricavato precedentemente
					 * trovo numero di parametri e classe per parametro
					 */
					Class<?>[] parameters = method.getParameterTypes();
					tmpp = new Object[parameters.length];

					//if(parameters != parameters_old) { //non funziona REP

					//System.out.println("***************");

					/**
					 * Object strin = parameters[0].newInstance();
					 * c'è un problema nel caso i costruttori richiedano parametri (Es. boolean) -> non funge....
					 */
					ParamSaver km = Instantiator.istantiatedArray(tmpp, parameters, 1);
					
					mtm.addParameter(km.getParamString());
					mtm.setParamUsed(km.getParamArray());
					test.addMethod(mtm);
					//}
					/**
					 * esecuzione del metodo ricercato 
					 * inserimento dei parametri richiesto
					 */
					/**
					 *  
		         for(int i=0;i<parameters.length;i++) {
		        	System.out.println(parameters[i].getName());
		        	System.out.println(tmp[i].getClass().getName());
		        	System.out.println(tmp[i]);
		         }
					 */

					//method.invoke(instance,"test", 1, true);

					//method.invoke(instance,  (Object[])km[0]); to invoke the method
				}
				l++;
				/*if(j==listM.length) {
					shish = false;
				}*/
				if(l == maxNumMethodsXTest-1) {
					shish = false;
				}
			}
			/**
			 * check test and if it is acceptable run it
			 */
			if(isReliable(test)) {
				for(MethodTest x : test.getMethodList()) {
					try {
						Executable exec = x.getMethod();
						if(exec instanceof Constructor) {
							Constructor c = (Constructor) exec;
							instance = c.newInstance(x.getParamUsed());
						}else if(exec instanceof Method) {
							Method m = (Method)exec;
							m.invoke(instance, x.getParamUsed());
						}
					}catch(Exception e) {
						//do stuff or do nothing?
						System.err.println("error invoke constructor/method secThread " + e);
						/**
						 * doing nothing on the exception permit to find also branch that throw exceptions
						 * or at least, it seems that it's doing that
						 */
					}
				}

				//get
				Method methodC=null;
				Method[] listMC=modifiedClass.getDeclaredMethods();
				//ottimizzabile
				for(int j=0;j<listMC.length;j++) {
					if(listMC[j].getName().equals("getChecker")) {
						methodC=listMC[j];
						break;
					}
				}
				Class<?>[] parametersC = methodC.getParameterTypes();
				Object[] tmpC = new Object[parametersC.length];
				ParamSaver kC = Instantiator.istantiatedArray(tmpC, parametersC, 1);
				ArrayList a= new ArrayList();
				try {
					a = (ArrayList) methodC.invoke(instance, kC.getParamArray());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.err.println("error invoke getChecker secThread" + e);
				}
				for(int j=0;j<a.size();j++) {
					//System.out.println(a.get(i));
					//clq.add(a.get(i));
					test.addBranch((Integer)a.get(j));
				}
				//clq.add(-2);//fine singolo test 
				//set
				Method methodS=null;
				Method[] listMS=modifiedClass.getDeclaredMethods();
				//ottimizzabile
				for(int j=0;j<listMS.length;j++) {
					if(listMS[j].getName().equals("setChecker")) {
						methodS=listMS[j];
						break;
					}
				}
				Class<?>[] parametersS = methodS.getParameterTypes();
				Object[] tmpS = new Object[parametersS.length];
				ParamSaver kS = Instantiator.istantiatedArray(tmpS, parametersS, 1);
				try {
					methodS.invoke(instance, kS.getParamArray());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.err.println("error invoke setChecker secThread" + e);
				}

				clq.add(test);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("Errore al test numero "+(id-1)+"\n");
			test = null;
		}
	}

	/**
	 * to check the testcase before invoking it
	 * @param test
	 * @return
	 */
	private static boolean isReliable(TestCase test) {
		// TODO Auto-generated method stub
		
		return true;
	}

	/**
	 * 
	 */
	public static void instantiateInstrumentedClass() {
		try {
			/**
			 * modifica iniziale della classe in input			
			 */
			loader.addSourcePath(filePath);
			Class<?> classToLoad = loader.load(fileName);

			Factory f = loader.getFactory();
			nBranch = SpoonMod.modify(classToLoad, f);
			loader.compile();

			File root = new File("./spoon/src");
			File sourceFile = new File(root, fileName+".java");
			String k="";
			List<String> ls = Files.readAllLines(sourceFile.toPath(), Charset.defaultCharset());
			for(int i=0;i<ls.size();i++) {
				//System.out.println(ls.get(i));
				k = k.concat(ls.get(i));
				k = k.concat("\n");
			}

			//System.out.println(k);
			//k = k.replaceAll(Pattern.quote(fileName),fileName+"Instr");
			k = k.replaceAll("\\Q"+fileName+" \\E",fileName+"Instr ");
			k = k.replaceAll("\\Q"+fileName+"<\\E",fileName+"Instr<");
			k = k.replaceAll("\\Q"+fileName+"(\\E",fileName+"Instr(");
			k = k.replaceAll("\\Q"+fileName+"{\\E",fileName+"Instr{");
			//System.out.println(k);

			File root2 = new File(filePath);
			File sourceFile2 = new File(root2, fileName+"Instr.java");
			//sourceFile.getParentFile().mkdirs();
			Files.write(sourceFile2.toPath(), k.getBytes(StandardCharsets.UTF_8));
			
			//System.out.println(sourceFile2.getAbsolutePath());/**/

			/**
			 * maniera differente per scrivere il file Instr
			 * 
			 * PrintWriter writer = new PrintWriter("class/Class0Instr.java", "UTF-8");
			 * writer.println(k);
			 * writer.close();
			 */


			/*JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			compiler.run(null, null, null, sourceFile2.getPath());			
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { 
					root2.toURI().toURL()
					});*/
			//			Class<?> modifiedClass = Class.forName("spoon.src.Class0", true, classLoader);

			//loader.addSourcePath("spoon/src");
			//Class<?> modifiedClass = loader.load("Class0");
			//			supportFunction.printCode(loader.mirror(classToLoad));


			//			CtType ctt = loader.mirror(classToLoad);
			//			System.out.println(ctt);



			String [] source = { "-d","bin","-cp",".;"+classpath,new String(filePath + "/" + fileName + "Instr.java")};
			ByteArrayOutputStream baos= new ByteArrayOutputStream();
			//new sun.tools.javac.Main(baos,source[0]).compile(source);
			com.sun.tools.javac.Main.compile(source);
			
			while(!(baos.toString().indexOf("error")==-1)) {}


			//			if( name.equals("Secondary pt.2")) {
			//			loader.addSourcePath("class");
			//			Class<?> modifiedClass = loader.load("Class0Instr");
			//			
			//			
			//			
			//			/**
			//			 * non mi vede le funzioni create con spoon....
			//			 */
			//			
			//			//loader.addSourcePath("spoon");
			//			//modifiedClass = loader.load("src.Class0");
			//			//modifiedClass = Class.forName("src.Class0");
			//			
			

			/**
			 * Prelevare il metodo da testare tramite nome
			 * richiede anche la lista di parametri come classi (possibile problema)
			 * funziona così:
			 * Method method = classToLoad.getDeclaredMethod ("testMe", String.class, int.class, boolean.class);
			 * se si conosce che c'è solo una funzione (qua sotto) non necessita della conoscenza dei parametri
			 */
			//System.out.println(modifiedClass.getDeclaredMethods()[1].getName());
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: Security problem! compiling part secThread" + e);
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: Argument inserted not correct! compiling part secThread" + e);
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: file In/Out failed! compiling part secThread" + e);
			//e.printStackTrace();
			/*} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();*/
		}
	}

	/**
	 * @return the importString
	 */
	public static String getImportString() {
		return importString;
	}

	/**
	 * @param importString the importString to set
	 */
	public static void setImportString(String importString) {
		SecThread.importString = importString;
	}
}

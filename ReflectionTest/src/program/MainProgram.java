package program;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import support.Instantiator;
import support.RandomGenerator;
import support.test.TestCase;
import support.test.TestCaseGenerator;

public class MainProgram {
	private static ArrayList<TestCase> finalTests;
	private static long testTimer;//millisecond
	private static String fileName;
	private static String filePath;
	private static int maxNum;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		//creo la mappa di supporto (da incementare su tutti i primitivi)
		Instantiator.setMap();
		finalTests = new ArrayList<TestCase>();
		
		String appConfigPath = "program.properties";
		 
		Properties appProps = new Properties();
		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		testTimer = Long.parseLong(appProps.getProperty("ExecutionTestTimer"));
		fileName = appProps.getProperty("FileName");
		filePath = appProps.getProperty("FilePath");
		maxNum = Integer.parseInt(appProps.getProperty("MaxNumberOfMethodXTest"));
		
		//main
		/**
		 * 
		 * File chooser da una interfaccia grafica per scegliere il file
		 * Non so come gli viene dato il file in input realmente
		 * 
		 */
		/*JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(".\\src"));
		chooser.setDialogTitle("Scegliere il file .java che si desidera");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			String fileName = chooser.getSelectedFile().getName();*/
		/**
		 * Stampe di supporto
		 * System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
		 * System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		 * System.out.println("Possible path : " + System.getProperty("user.dir")+"\\template\\"+chooser.getSelectedFile().getName());
		 */
		/**
		 * 
		 * DA CHIEDERE
		 * il file adesso io lo prelevo e lo inserisco nelal cartella del default package 
		 * perche cosi lo posso ripescare senza problemi e senza necessita di altre operazioni
		 * serve un altro modo?
		 * 
		 */
		/*try {
				Path in = chooser.getSelectedFile().toPath();
				File f = new File(System.getProperty("user.dir")+"\\src\\"+fileName);
				URI u = f.toURI();
				Path out = Paths.get(u);
				Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING); 	*/
		/*^ non va anche se copia correttamente
												non vede subito il nuovo file e quindi 
												chiamando testing con Class.forName(nomefilesenzaext)
												non va nulla*/
		/*} catch (FileNotFoundException e) {
				  System.out.println("File not found!");
			} catch (IOException e) {
				  System.out.println("File not copied!\n"+e);
			}
				String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$","");
				File f = new File(System.getProperty("user.dir")+"\\src\\"+fileName);//<- il file esiste, ma non va D:
				while(!f.isFile()) {} //<- tecnicamente questo loop mostra che il file esiste D: D: D: D:
		 */
		/**
		 * 
		 * devo trovare un qualcosa qua in mezzo che mi permetta di 'refreshare' tutte le directory
		 * per far comparire magicamente il nuovo file -> perche in realta f lo contiene gia.....
		 * 
		 */
		/*
				try {
					testing(Class.forName(fileNameWithOutExt));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					  System.out.println("Class not Found!\n"+e);
				}*/
		/**
		 * prelevo il file scelto ed i dati necessari per il prelievo della classe
		 * 1.nome senza estensione
		 * 2.URL
		 * e carico direttamente la classe
		 */
		/**
		 * 
		 *
			 	try {
					URL[] cp = {f.toURI().toURL()};
					URLClassLoader urlcl = new URLClassLoader(cp);
					Class targetClass = urlcl.loadClass(fileNameWithOutExt);//<- problema o si trova nel default package od 
																			//in un package conosciuto o non so come prenderla... bad
					testing(targetClass);


				} catch (MalformedURLException e){
					// TODO Auto-generated catch block
					  System.out.println("URL inserted malformed!");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					  System.out.println("Class not Found!\n"+e);
				}*/

		/*} else {
			  System.out.println("File .java not selected!");
		}*/

		LinkedBlockingQueue<TestCase> cLQ = new LinkedBlockingQueue();

		SecThread st = new SecThread("Thread", cLQ, testTimer, fileName, filePath, maxNum);//millisecond value
		st.start();
		boolean flag=true;
		boolean fluffa = true;
		boolean coverFlag = false;
		String importString = "";
		//ArrayList<TestCase> testCases = new ArrayList();
		while(flag && (!cLQ.isEmpty() || st.isAlive())) {
			//System.out.println("whilee");
			/*try {
				Object o = cLQ.take();
					ArrayList a = (ArrayList)o;
					System.out.println(a.size());
					for(int i=0;i<a.size();i++) {
						System.out.println(a.get(i));
					}
					//System.out.println("-----------------");

			}catch(NoSuchElementException e) {
				System.out.println("no element");
			}catch(java.lang.ClassCastException e) {
				System.out.println("cast error");
				flag=false;
			} catch (InterruptedException e) {
				System.out.println("Interrotto");
			}*/
			/**
			 * gestione dei risultati dei test
			 * devo prendere il branch coverage, che mi arriva diviso da -2 
			 * il recupero dei dati finisce allo scadere dell'ipotetico timer con -1
			 * oltre al branch coverage devo recuperare gli attributi input delle funzioni
			 * devo fare l'analisi dei coverage migliori
			 */
			try {
				TestCase o = cLQ.take();
				if(fluffa) {
					importString = SecThread.getImportString();
					fluffa=false;
				}
				//System.out.println(o.toString());
				//System.out.println("Nuovo test!");
				AddToFinalTestCase(o);
				//testCases.add(o);
				//Thread.sleep(1);//se non gli do un attimo di pausa, si blocca per conto suo.......
				/*if(!st.isAlive()) {
					//System.out.println("sta per mori!");			
					if(cLQ.isEmpty()) {
						flag=false;
						//System.out.println("Raccolto tutti  i dati");
					}
				}*/
				if(isCoverageAlreadyComplete(o)) {
					importString = SecThread.getImportString();
					st.stop();
					while(st.isAlive()) {}
					System.err.println(cLQ.size());
					cLQ.clear();
					coverFlag=true;
				}
			} catch (Exception e) {
				System.err.println("main interrupt " + e);
				flag=false;
			}
			

		}
		if(!coverFlag) {
			st.stop();
			while(st.isAlive()) {}
		}
		
		System.err.println("Test raccolti");
		
		checkTestValues();
		TestCaseGenerator.setImportList(importString);
		System.err.println("Import raccolti");
		TestCaseGenerator.setTestList(finalTests);
		TestCaseGenerator.GenerateTestCaseFile();
		System.err.println("test generati");
		/*for(TestCase tc : finalTests) {
			System.out.println(tc.toString());
		}*/
		//RandomGenerator.showMap();
		System.exit(0);
	}

	private static boolean isCoverageAlreadyComplete(TestCase o) {
		// TODO Auto-generated method stub
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
					if(oldTest.getMethodList().size()>newTest.getMethodList().size()) {
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
	
	private static void checkTestValues() {
		for(int i=0;i<finalTests.size();i++) {//for each element
			TestCase oldTest = finalTests.get(i);
			for(int j=0;j<finalTests.size();j++) {//on all the other test
				TestCase newTest = finalTests.get(j);
				HashSet<Integer> tmp1 = newTest.getBranchCovered();
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
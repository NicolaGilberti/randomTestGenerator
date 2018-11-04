package support.spoon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import spoon.Launcher;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.JavaOutputProcessor;
import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;

public class ClassModifier {

	String fileSeparator = System.getProperty("file.separator"); // in Unix '/', in Windows '\'
	String pathSeparator = System.getProperty("path.separator"); // in Unix ':', in Windows ';'

	private String DEFAULT_MODEL_BIN_DIR = "spoon/bin";
	private String DEFAULT_MODEL_SRC_DIR = "spoon/src";
	private Factory factory;
	private String modelBinDir = DEFAULT_MODEL_BIN_DIR;
	private String modelSrcDir = DEFAULT_MODEL_SRC_DIR;
	private Launcher launcher;
	JDTBasedSpoonCompiler builder;

	/**
	 * Constructor
	 */
	public ClassModifier() {
		factory = createFactory();
	}

	/**
	 * @return the {@link Factory} used to create mirrors
	 */
	public Factory getFactory() {
		return factory;
	}

	/**
	 * Set the temporary directory in which this class outputs source files
	 */
	public void setModelSrcDir(String modelSrcDir) {
		this.modelSrcDir = modelSrcDir;
	}

	/**
	 * Get the temporary directory in which this class outputs source files
	 */
	public String getModelSrcDir() {
		return modelSrcDir;
	}

	/**
	 * Set the temporary directory in which this class outputs compiled class files
	 */
	public void setModelBinDir(String modelBinDir) {
		this.modelBinDir = modelBinDir;
	}

	/**
	 * Get the temporary directory in which this class outputs compiled class files
	 */
	public String getModelBinDir() {
		return modelBinDir;
	}

	/**
	 * @return a new Spoon {@link Factory}
	 */
	protected Factory createFactory() {
		launcher = new Launcher();
		factory = launcher.getFactory();
		factory.getEnvironment().setAutoImports(true);
		factory.getEnvironment().setTabulationSize(5);
		factory.getEnvironment().setNoClasspath(true);
		factory.getEnvironment().setComplianceLevel(6);
		factory.getEnvironment().useTabulations(true);
		return factory;
	}

	/**
	 * Add directories or JAR files in which to search for input source code
	 */
	public void addSourcePath(String sourcePath) {
		addSourcePath(new File(sourcePath));
	}

	/**
	 * Add directories or JAR files in which to search for input source code
	 */
	public void addSourcePath(File sourcePath) {
		try {
			buildModel(sourcePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		compile();
	}

	/**
	 * Build the internal model (i.e. abstract syntax tree) of the source code
	 */
	protected void buildModel(File sourcePath) throws IOException {
		builder = new JDTBasedSpoonCompiler(factory);		
		try {
			builder.addInputSource(sourcePath);
			builder.build();
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}

	/**
	 * a spoon compiler
	 */
	public void compile() {
		JavaOutputProcessor fileOutput = new JavaOutputProcessor(new DefaultJavaPrettyPrinter(getFactory().getEnvironment()));

		fileOutput.getPrinter();
		launcher.setBinaryOutputDirectory(new File(modelBinDir));
		launcher.setSourceOutputDirectory(new File(modelSrcDir));
		fileOutput.setFactory(getFactory());
		fileOutput.init();
		for (CtType<?> type : getFactory().Class().getAll()) {
			fileOutput.process(type);
		}
		fileOutput.processingDone();
	}

	/**
	 * the first step is used to remove the 'first' dir that is not part of the dot notation of the classes
	 * @param sourceDir
	 * @return
	 */
	public String[] findQualifiedNames(String sourceDir) {
		ArrayList<String>  qn = findQualifiedNamesPt2(sourceDir);
		String[] ss = new String[qn.size()];
		for(int i=0; i<qn.size(); i++) {
			String s = qn.get(i);
			ss[i] = s.substring(s.indexOf(".")+1);
		}
		return ss;
	}

	private ArrayList<String> findQualifiedNamesPt2(String sourceDir) {
		ArrayList<String>  qn = new ArrayList<String>();
		File f = new File(sourceDir);
		if(f.isDirectory()) {
			for(String ff : f.list()) {
				ArrayList<String> tmp = findQualifiedNamesPt2(sourceDir + fileSeparator +ff);
				for(String s : tmp) {
					qn.add(f.getName() + "." + s);
				}
			}
		}
		else {
			String[] s = f.getName().split("\\.");
			qn.add(s[0]);
		}
		return qn;
	}
}

package support.spoon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import spoon.Launcher;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.JavaOutputProcessor;
import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;

public class ClassModifier {

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
	 * @return the class with the given qualified name. 
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> load(String qualifiedName) {
		try {
			URL url = new File(modelBinDir).toURI().toURL();
			URLClassLoader cl = new URLClassLoader(new URL[] {url});
			Thread.currentThread().setContextClassLoader(cl);
			return (Class<T>)(cl.loadClass(qualifiedName));
		} 
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} 
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the reflective instance of the class with the given qualified name
	 */
	public <T> CtType<T> mirror(String qualifiedName) {
		Class<T> clazz = load(qualifiedName);
		return mirror(clazz);
	}

	/**
	 * @return the reflective instance of the given class
	 */
	public <T> CtType<T> mirror(Class<T> clazz) {
		return getFactory().Type().get(clazz);
	}
}

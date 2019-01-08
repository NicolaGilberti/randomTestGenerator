package support.spoon;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import spoon.reflect.declaration.*;
import spoon.reflect.code.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.chain.CtQuery;
import spoon.reflect.visitor.filter.AnnotationFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.cu.SourcePosition;
import support.generator.Instantiator;
import support.generator.MapValue;

public class SpoonMod {
	static int counter = 0;
	private static Factory factory;
	private static CtClass<?> cc;
	private static ArrayList<Integer> lineToCoverList;
	private HashMap<Integer,String> instrToMethodMap;

	public void setFactory(Factory factory) {
		this.factory = factory;
	}
	
	/**
	 * 
	 * add field used for instrumentation
	 *
	 * @param cc
	 * @param factory
	 */
	private void addField(CtClass cc) {
		final CtTypeReference<Integer> dateRef = factory.Code().createCtTypeReference(Integer.class);
		final CtTypeReference<ArrayList<Integer>> listRef = factory.Code().createCtTypeReference(ArrayList.class);
		listRef.addActualTypeArgument(dateRef);
		final CtField<ArrayList<Integer>> listOfDates = factory.Core().<ArrayList<Integer>>createField();
		listOfDates.setSimpleName("checker");
		listOfDates.setType(listRef);
		listOfDates.addModifier(ModifierKind.STATIC);
		listOfDates.addModifier(ModifierKind.PUBLIC);
		final CtParameter<ArrayList<Integer>> parameter = factory.Core().<ArrayList<Integer>>createParameter();
		parameter.setType(listRef);
		parameter.setSimpleName("checker");
		cc.addFieldAtTop(listOfDates);
	}
	
	/**
	 * 
	 * getter method for the new Field
	 *
	 * @param cc
	 * @param factory
	 */
	private void addGetField(CtClass cc) {
		Set<ModifierKind> modifiersG = new HashSet<>(1);
		modifiersG = new HashSet<>(1);
		modifiersG.add(ModifierKind.PUBLIC);
		final CtTypeReference<ArrayList<Integer>> listRef2G = factory.Code().createCtTypeReference(ArrayList.class);
		CtBlock getterBlock = factory.Core().createBlock();
		getterBlock.addStatement(factory.Code().createCodeSnippetStatement("return checker"));
		factory.Method().create(cc,modifiersG,listRef2G,"getChecker",
				new ArrayList<CtParameter<?>>(), 
				new HashSet<CtTypeReference<? extends Throwable>>(),getterBlock); 	
	}
	
	/**
	 * 
	 * setter method for the new Field
	 *
	 * @param cc
	 * @param factory
	 */
	private void addSetField(CtClass cc) {
		Set<ModifierKind> modifiersS = new HashSet<>(1);
		modifiersS = new HashSet<>(1);
		modifiersS.add(ModifierKind.PUBLIC);
		final CtTypeReference<?> listRef2S = factory.Code().createCtTypeReference(int.class);
		CtBlock setterBlock = factory.Core().createBlock();
		setterBlock.addStatement(factory.Code().createCodeSnippetStatement("checker.clear()"));
		setterBlock.addStatement(factory.Code().createCodeSnippetStatement("return 0"));
		factory.Method().create(cc,modifiersS,listRef2S,"setChecker",
				new ArrayList<CtParameter<?>>(), 
				new HashSet<CtTypeReference<? extends Throwable>>(),setterBlock); 	
	}
	
	/**
	 * 
	 * modify constructor for Instrumentation
	 *
	 * @param cc
	 * @param factory
	 */
	private void modConstructors(CtClass cc) {
		List<?> tmp;
		CtConstructor<?> constructor;
		Set<?> constructors = cc.getConstructors();
		Iterator<?> itc = constructors.iterator();
		while(itc.hasNext()) {
			constructor = (CtConstructor<?>) itc.next();
			CtBlock<?> ctb = constructor.getBody();
			tmp = ctb.getStatements();
			List<CtStatement> newStatements = new ArrayList<CtStatement>();
			CtCodeSnippetStatement newStatement = factory.Code().createCodeSnippetStatement("checker = new ArrayList()");
			newStatements.add(newStatement);
			for(int j = 0; j < tmp.size(); j++){
				newStatements.add((CtStatement) tmp.get(j));
			}
			ctb.setStatements(newStatements);
		}
	}
	
	/**
	 * This method add all the instrumentation to the input class
	 * 
	 * @param inputClass the class to modify
	 * @param factory spoon element required to modify class
	 * @return the number of branch founded during the instrumentation
	 */
	public int modifyForBranchCoverage(Class<?> inputClass) {
		// TODO Auto-generated method stub
		cc = (CtClass<?>) factory.Class().get(inputClass);
		//System.out.println("class: " + cc.getQualifiedName());
		CtMethod<?> method;
		Set<?> methods = cc.getMethods();
		Iterator<?> itm = methods.iterator();
		while(itm.hasNext()) {
			method = (CtMethod<?>) itm.next();
			CtExecutable<?> methodDecl = factory.Method().createReference(method).getDeclaration();				
			List<CtStatement> statements = methodDecl.getBody().getStatements();
			visitIfTree(statements);
		}

		return counter;
	}
	
	public void modifyForLineCoverage(Class<?> inputClass, String[] lineToCover) {
		cc = (CtClass<?>) factory.Class().get(inputClass);
		CtMethod<?> method;
		instrToMethodMap = new HashMap<Integer,String>();

		lineToCoverList = new ArrayList<Integer>();
		for(int i=lineToCover.length-1; i>=0; i--) {
			lineToCoverList.add(new Integer(lineToCover[i]));
		}

		Comparator<CtMethod<?>> comp = (m1,m2)->(m1.getPosition().getLine()<=m2.getPosition().getLine()?1:-1); 
		Set<CtMethod<?>> methods = cc.getMethods();
		List<CtMethod<?>> underTest = new ArrayList<CtMethod<?>>(methods);
		underTest.sort(comp);
		

		int tempCounter = this.counter;
		Iterator<?> itm = underTest.iterator();
		while(itm.hasNext()) {
			method = (CtMethod<?>) itm.next();
			CtExecutable<?> methodDecl = factory.Method().createReference(method).getDeclaration();	
			List<CtStatement> statements = methodDecl.getBody().getStatements();
			visitBlock(statements);
			if(this.counter!=tempCounter){
				for (int x=tempCounter; x<this.counter; x++) {
					instrToMethodMap.put(x, method.getSimpleName());
				}
			}
			tempCounter = this.counter;

		}
		modConstructors(cc);
		addField(cc);
		addGetField(cc);
		addSetField(cc);
	}
	/**
	 * 
	 * @param statements
	 * @param factory
	 */
	private void visitBlock(List<CtStatement> statements) {
		if(lineToCoverList.size()==0){return;} 
		for(int pos=statements.size()-1; pos>=0; pos--) {
			CtStatement cts = statements.get(pos);
			if(cts instanceof CtBodyHolder){
				CtStatement body = ((CtBodyHolder) cts).getBody();
				this.visitLines(body);
			}else if(cts instanceof CtIf) {
				CtIf ctIf = (CtIf) cts;
				//System.out.println(ctIf.getPosition().getLine() + " " + ctIf);
				CtStatement ctsIf = ctIf.getThenStatement();
				CtStatement ctsElse = ctIf.getElseStatement();
				this.visitLines(ctsElse);//else first becase we want to analye the code from the end of the code to the 'first' line
				//System.out.println(ctsElse.getPosition().getLine() + " " + ctsElse);
				this.visitLines(ctsIf);
				//System.out.println(ctsIf.getPosition().getLine() + " " + ctsIf);
			}
			this.checkLine(cts);
		}
		
		/*for(CtStatement cts : statements) {
			if(cts instanceof CtBodyHolder){
				CtStatement body = ((CtBodyHolder) cts).getBody();
				this.visitLines(body);
			}else if(cts instanceof CtIf) {
				CtIf ctIf = (CtIf) cts;
				System.out.println(ctIf.getPosition().getLine() + " " + ctIf);
				CtStatement ctsIf = ctIf.getThenStatement();
				CtStatement ctsElse = ctIf.getElseStatement();
				boolean flag1 = this.visitLines(ctsElse);//else first becase we want to analye the code from the end of the code to the 'first' line
				//System.out.println(ctsElse.getPosition().getLine() + " " + ctsElse);
				boolean flag2 = this.visitLines(ctsIf);
				//System.out.println(ctsIf.getPosition().getLine() + " " + ctsIf);
			}
		}*/
		/*Iterator<CtStatement> x = statements.iterator();
		while(x.hasNext()){
			CtStatement cts = x.next();
			if(cts instanceof CtBodyHolder){
				CtStatement body = ((CtBodyHolder) cts).getBody();
				this.visitLines(body);
			}else if(cts instanceof CtIf) {
				CtIf ctIf = (CtIf) cts;
				//System.out.println(ctIf.getPosition().getLine() + " " + ctIf);
				CtStatement ctsIf = ctIf.getThenStatement();
				CtStatement ctsElse = ctIf.getElseStatement();
				this.visitLines(ctsElse);//else first becase we want to analye the code from the end of the code to the 'first' line
				this.visitLines(ctsIf);
			}
		}*/
	}

	private void visitLines(Object o) {
		if(lineToCoverList.size()==0){return;} 
		/**
		 * the System.out.println tell in which area you have to work and on what. so the visit is from the lower line to the upper.
		 */
		List<?> tmp;
		if(o instanceof CtBlock) {
			visitBlock(((CtBlock<?>)o).getStatements());
			tmp = ((CtBlock<?>)o).getStatements();
			for(int j = tmp.size()-1; j >= 0 ; j--){
				 CtStatement lineStatement = ((CtStatement)tmp.get(j));
				//System.out.println("Position: " + ((CtStatement)tmp.get(j)).getPosition().getLine());
				 this.checkLine(lineStatement);
			}
			//System.out.println("If Position: " + ((CtStatement)o).getPosition().getLine());
			//this.checkLine(((CtStatement)o));
		}
	}
	
	private void checkLine(CtStatement lineStatement) {
		boolean flag = false;
		if(lineToCoverList.size() > 0){
			if(lineStatement.getPosition().getLine() == lineToCoverList.get(0)) {
				lineStatement.insertBefore(this.getInstrSnippet());
				lineToCoverList.remove(0);
			}
		}
	}
	
	
	/**
	 * recursive function that analyze all the block searching for if and else areas.
	 * 
	 * @param statements the list of element to analyze
	 * @param factory spoon element required to modify class
	 */
	private void visitIfTree(List<CtStatement> statements) {
		List<?> tmp;
		List<?> tmp2;
		Iterator<CtStatement> x = statements.iterator();
		ArrayList<CtIf> noElseBlock = new ArrayList<CtIf>();
		while(x.hasNext()) {
			CtStatement o = x.next();
			if(o instanceof CtIf) {
				CtIf ctIf = (CtIf) o;
				CtStatement cts1 = ctIf.getThenStatement();
				CtStatement cts2 = ctIf.getElseStatement();
				if(cts1 instanceof CtBlock) {
					visitIfTree(((CtBlock<?>)cts1).getStatements());
					tmp = ((CtBlock<?>)cts1).getStatements();
					List<CtStatement> newStatements = new ArrayList<CtStatement>();
					newStatements.add(this.getInstrSnippet());
					for(int j = 0; j < tmp.size(); j++){
						newStatements.add((CtStatement) tmp.get(j));
					}
					((CtBlock<?>) cts1).setStatements(newStatements);

				}
				if(cts2 instanceof CtBlock) {
					visitIfTree(((CtBlock<?>)cts2).getStatements());
					tmp2 = ((CtBlock<?>)cts2).getStatements();
					List<CtStatement> newStatements = new ArrayList<CtStatement>();
					newStatements.add(this.getInstrSnippet());
					for(int j = 0; j < tmp2.size(); j++){
						newStatements.add((CtStatement) tmp2.get(j));
					}
					((CtBlock<?>)cts2).setStatements(newStatements);
				}else{
					noElseBlock.add(ctIf);
				}
			}
		}

		for(CtIf ctIf : noElseBlock) {
			ctIf.insertAfter(this.getInstrSnippet());
		}
	}

	/**
	 * this method find all the fields inside the class and set the map in support.generator.RandomGenerator with those values
	 */
	public void findVar(Instantiator inst) {
		TypeFilter typeFilter = new TypeFilter(CtField.class);
		List elements1 = cc.getElements(typeFilter);
		ArrayList values = new ArrayList();
		String valClass;
		Object value; 

		for(int i=0;i<elements1.size();i++) {
			CtExpression tmp = ((CtField)elements1.get(i)).getAssignment();
			if(tmp != null) {
				values.add(tmp);
			}
		}
		for(int i=0;i<values.size();i++) {
			if(values.get(i) instanceof CtLiteral) {
				CtLiteral temp = (CtLiteral) values.get(i);
				value = temp.getValue();
				if(value != null) {
					if(temp.getType().getPackage()==null) {
						//System.out.println(temp.getType());
						valClass = inst.getType(temp.getType().toString());
					}else {
						String s = temp.getType().getPackage() + "." + temp.getType();
						//System.out.println(s);
						valClass = inst.getType(s);
					}
					//System.out.println("\t" + value);
					inst.getrG().addToMap(new MapValue(value,-1,-1,-1,-1));
				}
			}else {
				//manage enum
			}
		}
	}
	
	public CtCodeSnippetStatement getInstrSnippet(){
		return factory.Code().createCodeSnippetStatement("checker.add("+ counter++ +")");
	}

	public HashMap<Integer, String> getInstrToMethodMap(){
		return this.instrToMethodMap;
	}

	public void printInstrToMethodMap(){
		for (Integer i : this.instrToMethodMap.keySet()) {
			System.err.println("Instrumentation value: "+i+" mapped to method: "+this.instrToMethodMap.get(i));
		}
	}
}

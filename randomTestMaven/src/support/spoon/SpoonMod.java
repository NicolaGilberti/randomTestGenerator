package support.spoon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtEnum;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import support.generator.Instantiator;
import support.generator.MapValue;

public class SpoonMod {
	static int counter = 0;
	private static List<?> tmp;
	private static List<?> tmp2;
	private static CtClass<?> cc;

	/**
	 * This method add all the instrumentation to the input class
	 * 
	 * @param inputClass the class to modify
	 * @param factory spoon element required to modify class
	 * @return the number of branch founded during the instrumentation
	 */
	public <T> int modify(Class<?> inputClass, Factory factory) {
		// TODO Auto-generated method stub
		cc = (CtClass<?>) factory.Class().get(inputClass);
		CtMethod<?> method;
		Set<?> methods = cc.getMethods();
		Iterator<?> itm = methods.iterator();
		while(itm.hasNext()) {
			method = (CtMethod<?>) itm.next();
			CtExecutable<?> methodDecl = factory.Method().createReference(method).getDeclaration();				
			List<CtStatement> statements = methodDecl.getBody().getStatements();
			visitIfTree(statements, factory);
		}

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

		Set<ModifierKind> modifiersG = new HashSet<>(1);
		modifiersG = new HashSet<>(1);
		modifiersG.add(ModifierKind.PUBLIC);
		final CtTypeReference<ArrayList<Integer>> listRef2G = factory.Code().createCtTypeReference(ArrayList.class);
		CtBlock getterBlock = factory.Core().createBlock();
		getterBlock.addStatement(factory.Code().createCodeSnippetStatement("return checker"));
		factory.Method().create(cc,modifiersG,listRef2G,"getChecker",
				new ArrayList<CtParameter<?>>(), 
				new HashSet<CtTypeReference<? extends Throwable>>(),getterBlock); 	

		/**
		 * metodo setter
		 */
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
		return counter;
	}

	/**
	 * recursive function that analyze all the block searching for if and else areas.
	 * 
	 * @param statements the list of element to analyze
	 * @param factory spoon element required to modify class
	 */
	public void visitIfTree(List<CtStatement> statements, Factory factory) {
		Iterator<CtStatement> x = statements.iterator();
		ArrayList<CtIf> noElseBlock = new ArrayList<CtIf>();
		while(x.hasNext()) {
			CtStatement o = x.next();
			if(o instanceof CtIf) {
				CtIf ctIf = (CtIf) o;
				CtStatement cts1 = ctIf.getThenStatement();
				CtStatement cts2 = ctIf.getElseStatement();
				if(cts1 instanceof CtBlock) {
					visitIfTree(((CtBlock<?>)cts1).getStatements(), factory);
					tmp = ((CtBlock<?>)cts1).getStatements();
					List<CtStatement> newStatements = new ArrayList<CtStatement>();
					CtCodeSnippetStatement newStatement = factory.Code().createCodeSnippetStatement("checker.add("+counter++ +")");
					newStatements.add(newStatement);
					for(int j = 0; j < tmp.size(); j++){
						newStatements.add((CtStatement) tmp.get(j));
					}
					((CtBlock<?>) cts1).setStatements(newStatements);

				}
				if(cts2 instanceof CtBlock) {
					visitIfTree(((CtBlock<?>)cts2).getStatements(), factory);
					tmp2 = ((CtBlock<?>)cts2).getStatements();
					List<CtStatement> newStatements = new ArrayList<CtStatement>();
					CtCodeSnippetStatement newStatement = factory.Code().createCodeSnippetStatement("checker.add("+counter++ +")");
					newStatements.add(newStatement);
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
			CtCodeSnippetStatement newStatement = factory.Code().createCodeSnippetStatement("checker.add("+counter++ +")");
			ctIf.insertAfter(newStatement);
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
				if(temp.getType().getPackage()==null) {
					//System.out.println(temp.getType());
					valClass = inst.getType(temp.getType().toString());
				}else {
					String s = temp.getType().getPackage() + "." + temp.getType();
					//System.out.println(s);
					valClass = inst.getType(s);
				}
				value = ((CtLiteral) values.get(i)).getValue();
				//System.out.println("\t" + value);
				inst.getrG().addToMap(new MapValue(value,-1,-1,-1));
			}else {
				//manage enum
			}
			
			
		}
	}
}

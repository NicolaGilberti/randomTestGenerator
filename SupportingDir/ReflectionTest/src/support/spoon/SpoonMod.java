package support.spoon;
import java.util.*;

import org.eclipse.jdt.core.dom.ThisExpression;

import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import support.Instantiator;
import support.RandomGenerator;

public final class SpoonMod {
	static int counter = 0;
	private static List<?> tmp;
	private static List<?> tmp2;
	private static CtClass<?> cc;

	public static <T> int modify(Class<?> inputClass, Factory factory) {
		// TODO Auto-generated method stub
		cc = (CtClass<?>) factory.Class().get(inputClass);
		CtMethod<?> method;
		Set<?> methods = cc.getMethods();
		Iterator<?> itm = methods.iterator();
		while(itm.hasNext()) {
			method = (CtMethod<?>) itm.next();
			//System.out.println(method.getSimpleName());
			CtExecutable<?> methodDecl = factory.Method().createReference(method).getDeclaration();				
			List<CtStatement> statements = methodDecl.getBody().getStatements();
			visitIfTree(statements, factory);
		}
		/*for(int q=0;q<methods.length-9;q++) {
			method = methods[q];
			//System.out.println(method.getName());
			CtExecutable<?> methodDecl = factory.Method().createReference(method).getDeclaration();			
			List<CtStatement> statements = methodDecl.getBody().getStatements();
			visitIfTree(statements, factory);
		}*/

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
		/*
		 * devo gestire anche queste funzioni nel loop..... douch -> per questo loop con -9
		 * wait, wait, wait, equals, toString, hashCode, getClass, notify, notifyAll
		 */

		//   Sta cosa funziona, ma vale solo per un methodo, il primo nella lista		
		//		Method method = inputClass.getMethods()[0];
		//		CtExecutable<?> methodDecl = factory.Method().createReference(method).getDeclaration();			
		//		List<CtStatement> statements = methodDecl.getBody().getStatements();
		//		visitIfTree(statements, factory);

		/*CtCodeSnippetStatement newStmt = factory.Code().createCodeSnippetStatement("boolean[] checker = new boolean["+counter+"]");
		statements.add(0,newStmt);*/ //rimane locale alla funzione -> bad non si vede dopo -> se salvato su file -> persiste (hope) <- no import -> fail

		/*	CtTypeReference<Boolean> dateRef = factory.Code().createCtTypeReference(Boolean[].class);
		//CtType ct = new ClassModifier().mirror(inputClass.getName());
		CtField<Boolean> cf = factory.Core().<Boolean>createField();
		cf.setType(dateRef);
		cf.setSimpleName("checker");
		CtClass cc = new CtClassImpl<>();
		cc = factory.Class().get(inputClass);
		cc.addField(cf);*/
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
		/*
		 * costruttore
		 *
		 	final CtCodeSnippetStatement statementInConstructor = factory.Code().createCodeSnippetStatement("this.checker = checker");
	        final CtBlock<?> ctBlockOfConstructor = factory.Code().createCtBlock(statementInConstructor);
	        final CtParameter<ArrayList<Integer>> parameter1 = factory.Core().<ArrayList<Integer>>createParameter();
	        parameter1.<CtParameter>setType(listRef);
	        parameter1.setSimpleName("checker");
	        final CtConstructor constructor = factory.Core().createConstructor();
	        constructor.setBody(ctBlockOfConstructor);
	        constructor.setParameters(Collections.<CtParameter<?>>singletonList(parameter1));
	        constructor.addModifier(ModifierKind.PUBLIC);
	        cc.addConstructor(constructor);
		 */

		/*probabilmente non funziona anche perchè non credo sia possibile istanziare in questo modo
		 */
		/*String s= "PrintWriter out;"
				+ "File f = new File(\"./filename.txt\");"
				+ "try{"
				+ "out = new PrintWriter(f);"
				+ "out.println(checker.toString());"
				+ "}catch(Exception e){" + 
				"e.printStackTrace();"
				+ "}";
		String s ="checker = new ArrayList()";
		CtCodeSnippetStatement newStmt2 = factory.Code().createCodeSnippetStatement(s);
		statements.add(0,newStmt2);*/
		/*String s ="System.out.println(\"aaaaaa\")";
		CtCodeSnippetStatement newStmt3 = factory.Code().createCodeSnippetStatement(s);
		statements.add(newStmt3);*/

		/**
		 * crea funzione get... non riesco a vederla
		 * il metodo sotto crea la stessa cosa...
		 * 
        final CtMethod ctmethodm = factory.Core().createMethod();
        final CtCodeSnippetStatement ctstmtm = factory.Code().createCodeSnippetStatement("return checker");
        final CtBlock<?> ctblockm = factory.Code().createCtBlock(ctstmtm);
        ctmethodm.setSimpleName("getChecker");
		final CtTypeReference<ArrayList<Integer>> listRef2 = factory.Code().createCtTypeReference(ArrayList.class);
        ctmethodm.setType(listRef2);
        ctmethodm.setBody(ctblockm);
        ctmethodm.addModifier(ModifierKind.PUBLIC);
        cc.addMethod(ctmethodm);*/
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
		final CtTypeReference listRef2S = factory.Code().createCtTypeReference(int.class);
		CtBlock setterBlock = factory.Core().createBlock();
		setterBlock.addStatement(factory.Code().createCodeSnippetStatement("checker.clear()"));
		setterBlock.addStatement(factory.Code().createCodeSnippetStatement("return 0"));
		factory.Method().create(cc,modifiersS,listRef2S,"setChecker",
				new ArrayList<CtParameter<?>>(), 
				new HashSet<CtTypeReference<? extends Throwable>>(),setterBlock); 	
		return counter;
	}

	public static void visitIfTree(List<CtStatement> statements, Factory factory) {
		Iterator<CtStatement> x = statements.iterator();
		while(x.hasNext()) {
			CtStatement o = x.next();
			//sSystem.out.println(o.getClass());
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
				}
				/**
				 * rendere ricorsiva la funzione fino a trovare il blocco interno non contenente un if.
				 * riscrivere il blocco con lo statement aggiuntivo
				 * instrumentazione comunque in qualunque livello -> capire più facilmente fino a che livello si è arrivati
				 * 
				 */
			}
		}
	}

	public static void findVar() {
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
			CtLiteral temp = (CtLiteral) values.get(i);
			if(temp.getType().getPackage()==null) {
				//System.out.println(temp.getType());
				valClass = Instantiator.getType(temp.getType().toString());
			}else {
				String s = temp.getType().getPackage() + "." + temp.getType();
				//System.out.println(s);
				valClass = Instantiator.getType(s);
			}
			value = ((CtLiteral) values.get(i)).getValue();
			//System.out.println("\t" + value);
			RandomGenerator.addToMap(valClass, value);
		}

	}
	
	public static void printCode(CtType<?> ctSimpleType) {
		System.out.println(ctSimpleType);
	}
}

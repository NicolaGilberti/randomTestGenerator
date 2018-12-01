package support.test;

import java.lang.reflect.*;

public class MethodTest {
	//attributes
	private Executable method;//superclass of Constructor and Method (Reflection)
	private String parameterUsed;
	private Object[] paramUsed;
	private String exception;

	/**
	 * Constructor
	 * 
	 * @param m the method/constructor we are analyzing
	 */
	public MethodTest(Executable m) {
		this.method = m;
		this.parameterUsed = "";
		this.setException(null);
	}


	/**
	 * @return the method
	 */
	public String getParameterUsed() {
		return parameterUsed;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String parameterUsed) {
		this.parameterUsed = parameterUsed;
	}
	
	/**
	 * @return the method
	 */
	public Executable getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Executable method) {
		this.method = method;
	}

	/**
	 * @return the method name
	 */
	public String getMethodName() {
		return this.method.getName();
	}

	/**
	 * @return the list of the parameter class required for this method
	 */
	public Class<?>[] getParamTypeList() {
		return this.method.getParameterTypes();
	}

	/**
	 * @param s, the data to append to the String to recover all the values required to execute the method
	 */
	public void addParameter(String s) {
		this.parameterUsed += s;//to check for the format
	}

	/**
	 * @return the paramUsed
	 */
	public Object[] getParamUsed() {
		return paramUsed;
	}

	/**
	 * @param paramUsed the paramUsed to set
	 */
	public void setParamUsed(Object[] paramUsed) {
		this.paramUsed = paramUsed;
	}

	/**
	 * this function analyze the param string recovering all the data required for the toString method
	 * It is a recursive function
	 * 
	 * @param param string to be analyzed
	 * @param lvl the depth level
	 * @param u an id value used to return a string composed properly without any inconsistencies in the variables
	 * @return a String formated for a JUnit test plus a counter for the toSting method
	 */
	private Object[] levelVisitor(String param, int lvl, int u){
		int counter = 0;
		int cont = 0;
		String s = "";
		String tab = "";
		int exactMatch=0;
		for(int q=0; q<=lvl; q++) {
			tab += "\t";
			exactMatch++;
		}
		String[] res = param.trim().split("#@{"+(exactMatch)+"}#");
		for(int qwe=1; qwe<res.length; qwe++) {
			String token = res[qwe];
			if(token.contains("#")) {
				Object[] obj = this.levelVisitor(token, lvl+1, u);
				s += obj[0];
				int max = token.indexOf(':');
				String variables="";
				String claz = token.substring(0, max);
				
				String[] clazGen = claz.split("[.]");
				String clazz = clazGen[clazGen.length-1];
				
				int counter2 = (int)obj[1];
				for(int x=0; x<counter2 ; x++) {
					if(x != counter2-1) {
						variables+= "var" + u + (lvl+1) + x + ",";
					}else {
						variables+= "var" + u + (lvl+1) + x;
					}
				}
				s+= tab + clazz + " var" + u + lvl + counter++ + " = new " + clazz + "(" + variables + ");\n";
			}else {
				s+=tab;
				String[] miniTokens = token.trim().split(":");
				if(miniTokens.length!=1) {
					String claz = miniTokens[0];
					String value = miniTokens[1];
					
					String[] clazGen = claz.split("[.]");
					String clazz = clazGen[clazGen.length-1];
					
					int mtN = u;
					int mtL = lvl;
					int mtC = counter;
					if(miniTokens.length>2) {
						mtN = Integer.parseInt(miniTokens[2]);
						mtL = Integer.parseInt(miniTokens[3]);
						mtC = Integer.parseInt(miniTokens[4]);
					}
					if(mtN != u || mtL != lvl || mtC != counter) {
						s+= clazz + " var" + u + lvl + counter++ + " = var" + mtN + mtL + mtC + ";\n";
					}else {
						if(claz.contains("String") && !value.equals("null")) {
							value = "\"" + value + "\"";
						}
						if(claz.toLowerCase().contains("char")) {
							value = "'" + value + "'";
						}
						if(claz.toLowerCase().contains("float")) {
							value += "f";
						}
						if(claz.toLowerCase().contains("long")) {
							value += "L";
						}
						if(value.contains(claz) && !value.contains("("+claz+")")) {	
							s+= clazz + " var" + u + lvl + counter++ + " = new " + clazz + "();\n";
						}else if(!value.equals("")){
							if(clazGen[0].equals("enum")) {
								String clazzz = "";
								for(int qwer=1; qwer<clazGen.length-1; qwer++) {
									clazzz += clazGen[qwer] + ".";
								}
								s+= clazzz + clazz + " var" + u + lvl + counter++ + " = " + value + ";\n";
								
							}
							else {
								s+= clazz + " var" + u + lvl + counter++ + " = " + value + ";\n";
							}
						}
					}
				}
			}
			cont++;
		}
		return new Object[] {s,cont};
	}


	/**
	 * @return a formatted string for a JUnit test
	 */
	public String toString(int u) {
		String s = "";
		
		Object[] k = this.levelVisitor(this.parameterUsed,1,u);
		s = (String)k[0];
		String something = "";
		for(int w=0; w<(int)k[1]; w++) {
			if(w != (int)k[1]-1) {
				something += "var" + u + "1" + w + ",";
			}else {
				something += "var" + u + "1" + w;
			}
		}
		
		if(this.getException()!=null) {
			s += "\ttry{\n"
					+ "\t";
		}
		s += "\t";
		
		if(this.method instanceof Method) {
			s += "obj." + this.method.getName(); 
		}
		else if(this.method instanceof Constructor) {
			String name = this.method.getName();
			name = name.substring(0,name.length()-5);//to remove Instr
			s += name + " obj = new " + name; 
		}
		s += "(" + something + ");\n"; 
		
		if(this.getException()!=null) {
			s += "\t}catch(" + this.getException() + " e){}\n";
		}

		return s;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}
}

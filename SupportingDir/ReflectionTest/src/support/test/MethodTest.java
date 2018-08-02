package support.test;

import java.lang.reflect.*;

public class MethodTest {
//attributes
	private Executable method;//superclass of Constructor and Method (Reflection)
	private String parameterUsed;
	private Object[] paramUsed;
	
	public MethodTest(Executable m) {
		this.method = m;
		this.parameterUsed = "";
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
	public Class[] getParamTypeList() {
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
	
//	private synchronized Object[] levelVisitor(String param, int lvl, int u){
//		int cont = 0;
//		String s = "";
//		String k="";
//		String tab = "";
//		for(int q=0; q<=lvl; q++) {
//			k += "*";
//			tab += "\t";
//		}
//		StringTokenizer st =  new StringTokenizer(param, k);
//		while(st.hasMoreTokens()) {
//			s+=tab;
//			String tmp = st.nextToken();
//			//s+= "\n\n" + tmp + "\n\n";
//			if(tmp.matches(".*:.*:")) {
//				Object[] temp = this.levelVisitor(tmp,lvl+1,u);
//				s += temp[0];
//				int max = tmp.indexOf(':');
//				String variables="";
//
//				String claz = tmp.substring(0, max);
//				for(int x=counter - (int)temp[1]; x< counter; x++) {
//					if(x != counter-1) {
//						variables+= "var" + u + lvl + x + ",";
//					}else {
//						variables+= "var" + u + lvl + x;
//					}
//				}
//				s+= claz + " var" + u + lvl + counter++ + " = new " + claz + "(" + variables + ");\n";
//			}else {
//				int max = tmp.indexOf(":");
//				if(max!=-1) {
//					String claz = tmp.substring(0, max);
//					String value = tmp.substring(max+1);
//					if(claz.contains("String")) {
//						value = "\"" + value + "\"";
//					}
//					if(value.contains(claz)) {						
//						s+= claz + " var" + u + lvl + counter++ + " = new " + claz + "();\n";
//					}else {
//						s+= claz + " var" + u + lvl + counter++ + " = " + value + ";\n";
//					}
//				}
//			}
//			cont++;
//		}
//		return new Object[] {s,cont};
//	}
	private synchronized Object[] levelVisitor(String param, int lvl, int u){
		int counter = 0;
		int cont = 0;
		String s = "";
		String tab = "";
		int exactMatch=0;
		for(int q=0; q<=lvl; q++) {
			tab += "\t";
			exactMatch++;
		}
		String[] res = param.trim().split("§@{"+(exactMatch)+"}§");
		/*for(String kdsj : res) {
			System.out.println(kdsj + "\n");
		}*/
		for(int qwe=1; qwe<res.length; qwe++) {
			String token = res[qwe];
			if(token.contains("§")) {
				Object[] obj = this.levelVisitor(token, lvl+1, u);
				s += obj[0];
				int max = token.indexOf(':');
				String variables="";
				String claz = token.substring(0, max);
				int counter2 = (int)obj[1];
				for(int x=0; x<counter2 ; x++) {
					if(x != counter2-1) {
						variables+= "var" + u + (lvl+1) + x + ",";
					}else {
						variables+= "var" + u + (lvl+1) + x;
					}
				}
				s+= tab + claz + " var" + u + lvl + counter++ + " = new " + claz + "(" + variables + ");\n";
			}else {
				s+=tab;
				int max = token.indexOf(":");
				if(max!=-1) {
					String claz = token.substring(0, max);
					String value = token.substring(max+1);
					if(claz.contains("String")) {
						value = "\"" + value + "\"";
					}
					if(value.contains(claz)) {						
						s+= claz + " var" + u + lvl + counter++ + " = new " + claz + "();\n";
					}else {
						s+= claz + " var" + u + lvl + counter++ + " = " + value + ";\n";
					}
				}
			}
			cont++;
		}
		return new Object[] {s,cont};
	}
	
	
	/**
	 * @return a formatted string
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
		s += "\t";
		if(this.method instanceof Method) {
			s += "obj." + this.method.getName(); 
		}
		else if(this.method instanceof Constructor) {
			String name = this.method.getName();
			s += name + " obj = new " + name; 
		}
		s += "(" + something + ");\n"; 
		
		return s;
	}
}

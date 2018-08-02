package support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Instantiator {

	private static Map<String, String> map = new HashMap<String, String>();

	public static void setMap() {
		map.put("int","java.lang.Integer");
		map.put("boolean", "java.lang.Boolean");
		map.put("java.lang.String", "java.lang.String");
		map.put("java.util.ArrayList", "java.util.ArrayList");
		map.put("long", "java.lang.Long");
		map.put("short", "java.lang.Short");
		map.put("float", "java.lang.Float");
		map.put("double", "java.lang.Double");
	}

	public static String getType(String key) {
		if(map.containsKey(key)) {
			return map.get(key);
		}
		else {
			return key;
		}
	}

	public static ParamSaver istantiatedArray(Object[] obj, Class<?>[] parameters, int lvl) {
		String s="";
		Random random;
		String temp;
		Class<?> target;
		int maxVal;
		int parVal;
		Constructor<?> targetC;
		Object[] req = null;
		for(int i=0;i<obj.length;i++){
			//to define different levels (to find token during the string elaboration)
			s+="§";
			for(int q=0 ; q<=lvl; q++) {
				s += "@";
			}
			s+="§";
			
			try {
				temp = getType(parameters[i].getName()); 
				random = new Random();
				target = Class.forName(temp);
				s += parameters[i].getName() + ":";
				switch (parameters[i].getName()){
				case "boolean":
					obj[i] = Class.forName(temp).getConstructor(boolean.class).newInstance(true);
					obj[i] = RandomGenerator.setValue(obj[i]);
					s += obj[i];
					break;
				case "int":
					obj[i] = Class.forName(temp).getConstructor(int.class).newInstance(1);
					obj[i] = RandomGenerator.setValue(obj[i]);
					s += obj[i];
					break;
				case "long":
					obj[i] = Class.forName(temp).getConstructor(long.class).newInstance(1L);
					obj[i] = RandomGenerator.setValue(obj[i]);
					s += obj[i];
					break;
				case "short":
					obj[i] = Class.forName(temp).getConstructor(short.class).newInstance(1);
					obj[i] = RandomGenerator.setValue(obj[i]);
					s += obj[i];
					break;
				case "float":
					obj[i] = Class.forName(temp).getConstructor(float.class).newInstance(0.0f);
					obj[i] = RandomGenerator.setValue(obj[i]);
					s += obj[i];
					break;
				case "double":
					obj[i] = Class.forName(temp).getConstructor(double.class).newInstance(0.0d);
					obj[i] = RandomGenerator.setValue(obj[i]);
					s += obj[i];
					break;
				case "java.lang.String":
					obj[i] = Class.forName(temp).newInstance();
					obj[i] = RandomGenerator.setValue(obj[i]);
					s += obj[i];
					break;
				default:
					boolean flag=true;
					do{
						flag=false;
						maxVal = target.getConstructors().length;
						int ran = random.nextInt(maxVal);
						targetC = target.getConstructors()[ran];
						parVal = targetC.getParameterTypes().length;
						if(parVal!=0) {
							try {
								req = new Object[parVal];
								Class<?>[] paramType = targetC.getParameterTypes();
								ParamSaver k = istantiatedArray(req, paramType, lvl+1);
								Object[] pippo = k.getParamArray();
								obj[i] = Class.forName(temp).getConstructor(paramType).newInstance(pippo);
								s += k.getParamString();
							}catch(NoSuchMethodException  | IllegalArgumentException | InvocationTargetException e) {
								
								System.err.println("Instantiator error, it wil ltry again " + e);
								//System.err.println("\ntest: "+Class.forName(temp).getName());
								flag=true;
							}
						}else {
								obj[i] = Class.forName(temp).newInstance();
								s+=temp;
						}
					}while(flag);
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Instantiator error ClassNotFoundException " + e);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Instantiator error InstantiationException " + e);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Instantiator error IllegalAccessException " + e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Instantiator error InvocationTargetException " + e);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Instantiator error NoSuchMethodException " + e);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println("Instantiator error SecurityException " + e);
			}

			/************************************************/
			/**	obj[i] = RandomGenerator.setValue(obj[i]); **/
			/***************** s += obj[i]; *****************/ 
			/************************************************/
			
			
		}
		return new ParamSaver(obj,s);
	}

	//old version
	/*public static Object[] istantiatedArrayOld(Object[] obj, Class<?>[] parameters) {
		for(int i=0;i<obj.length;i++) {
			String temp = supportFunction.getType(parameters[i].getName()); 
			try {

	 * questo primo try mi serve per analizzare se la creazione della nuova istanza necessita di costruttori con 0 o più parametri
	 * se 0 va a buon fine, se maggiore viene eseguito il catch

				if(parameters[i].getName().equals("java.util.ArrayList")){
    				obj[i] = new ArrayList<>();
    			}
	        		obj[i] = Class.forName(temp).newInstance();
        	}catch(InstantiationException e) {
        		try {

	 * si necessita di un altro try catch per gestire la possibilità di costruttore sconosciuto, bisogna quindi
	 * trovare un modo per classificare tutti i tipi e definirne che costruttori richiedono

        			if(parameters[i].getName().equals("boolean")) {
        				obj[i] = Class.forName(temp).getConstructor(boolean.class).newInstance(true);
        			}else if(parameters[i].getName().equals("int")){
        				obj[i] = Class.forName(temp).getConstructor(int.class).newInstance(1);
        			}else {
        				Random random = new Random();
        				Object[] req = null;
        				Class<?> target = Class.forName(temp);
        				int maxVal = target.getConstructors().length;
        				Constructor<?> targetC = target.getConstructors()[random.nextInt(maxVal)];
        				int parVal = targetC.getParameterTypes().length;
        				req = new Object[parVal];
        				req = istantiatedArray(req, targetC.getParameterTypes());
        				obj[i] = Class.forName(temp).getConstructor(Class.forName(temp)).newInstance(req);
        			}

				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					System.out.println("Error: Constructor unknown!");
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			obj[i] = setValue(obj[i]);

		}

		return obj;
	}*/
}

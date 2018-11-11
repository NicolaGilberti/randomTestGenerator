package support.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;

import support.spoon.*;

public class Instantiator {

	private Map<String, String> map = new HashMap<String, String>();
	private RandomGenerator rG;
	private ClassModifier loader;
	private String classPath;

	/**
	 * Constructor  that create a map with 'primitive' types and class name
	 */
	public Instantiator() {
		rG = new RandomGenerator();
		map.put("int","java.lang.Integer");
		map.put("boolean", "java.lang.Boolean");
		map.put("java.lang.String", "java.lang.String");
		map.put("long", "java.lang.Long");
		map.put("short", "java.lang.Short");
		map.put("float", "java.lang.Float");
		map.put("double", "java.lang.Double");
		map.put("char", "java.lang.Character");
	}

	/**
	 * to get the key from the value. 
	 * if not into the map return the input
	 * 
	 * @param value the value to search
	 * @return the key
	 */
	public String getKey(String value) {
		for (Entry<String, String> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return value;
	}

	/**
	 * to get a value from the map
	 * in case of not presence, the input is returned
	 * @param key to find inside the map
	 * @return the value if the input is contained into the map, the input in the other cases
	 */
	public String getType(String key) {
		if(map.containsKey(key)) {
			return map.get(key);
		}
		else {
			return key;
		}
	}

	/**
	 * This method is recursive.
	 * 
	 * @param obj an array that will contain teh parameter instantiated.
	 * @param parameters an array that define all the classes that the method have to instantiate
	 * @param lvl the depth level of the function parameter
	 * @return a ParamSaver containing all the value required for future works
	 */
	public ParamSaver istantiatedArray(Object[] obj, Class<?>[] parameters, int lvl, int u) {
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
			s+="#";
			for(int q=0 ; q<=lvl; q++) {
				s += "@";
			}
			s+="#";

			try {
				MapValue mV;
				temp = getType(parameters[i].getName()); 
				String clazNameTmp = parameters[i].getName();
				if(parameters[i].isEnum()) {
					clazNameTmp="enum";
				}
				else {
					//On the enum we cannot put this value like in the other cases because in the name there is a not accepted symbol
					s += parameters[i].getName() + ":";
				}
				random = new Random();
				random.setSeed(System.currentTimeMillis());
				switch (clazNameTmp){
				case "boolean":
					obj[i] = Class.forName(temp).getConstructor(boolean.class).newInstance(true);
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "int":
					obj[i] = Class.forName(temp).getConstructor(int.class).newInstance(1);
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "long":
					obj[i] = Class.forName(temp).getConstructor(long.class).newInstance(1L);
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "short":
					obj[i] = Class.forName(temp).getConstructor(short.class).newInstance(1);
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "float":
					obj[i] = Class.forName(temp).getConstructor(float.class).newInstance(0.0f);
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "double":
					obj[i] = Class.forName(temp).getConstructor(double.class).newInstance(0.0d);
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "char":
					obj[i] = Class.forName(temp).getConstructor(char.class).newInstance('a');
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "java.lang.String":
					obj[i] = Class.forName(temp).newInstance();
					mV = rG.setValue(this, new MapValue(obj[i],u,lvl,i));
					obj[i] = mV.getValue();
					s += mV;
					break;
				case "enum":
					String[] x = temp.split("\\.");
					String[] enumRootClass;
					String enumClassName;

					enumClassName = x[x.length-1];
					enumRootClass = Arrays.copyOf(x, x.length-1);
					
					String enumRootClassStr = "";
					for(int qwer=0; qwer<enumRootClass.length; qwer++) {
						enumRootClassStr += enumRootClass[qwer] + ".";
					}
					Object[] enumOptions = parameters[i].getEnumConstants();
					int enumOptionsLenght = enumOptions.length;
					Object valuezz = enumOptions[random.nextInt(enumOptionsLenght)];
					mV = new MapValue(valuezz,u,lvl,i);
					if(rG.map.containsKey(temp)){
						rG.addToMap(mV);
					}else {
						ArrayList<MapValue> valuez = new ArrayList<MapValue>();
						valuez.add(mV);
						rG.map.put(temp, valuez);
					}
					obj[i] = mV.getValue();
					mV.setValue(enumClassName + "." + mV.getValue().toString());
					s += "enum." + enumRootClassStr + enumClassName + ":" + mV;
					/*} 
					else{
						System.err.println("Enum creation error, unknown name collected: " + x[0] ); 
					}*/
					break;
				default:
					//try {
					target = Class.forName(temp);
					/*}catch(ClassNotFoundException c) {
						c.printStackTrace();
						target = Class.forName("Id");
						temp= "Id";
					}
					System.out.println("class: " + temp);*/
					
					/**
					 * it seems a bit long, but there are only 2 branch after this comment:
					 * 1. RandomGenerator map DOESN'T KNOW this object class OR
					 * 		the RandomGenerator KNOW this object class, but the randomicity want a new instantiation
					 * 2. RandomGenerator KNOW this object class and randomicity let to choose an old instance
					 * 
					 * first branch has two option but they differ only on the last part, where the first update the map with the new value,
					 * while the second insert the new class into the RandomGenerator map
					 * 
					 */
					if(!rG.map.containsKey(temp) || random.nextBoolean()) {
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
									ParamSaver k = istantiatedArray(req, paramType, lvl+1, u);
									Object[] pippo = k.getParamArray();
									obj[i] = Class.forName(temp).getConstructor(paramType).newInstance(pippo);
									s += k.getParamString();
								}catch(NoSuchMethodException  | IllegalArgumentException | InvocationTargetException e) {
									System.err.println("Instantiator error, it will try again " + e);
									flag=true;
								}
							}else {
								obj[i] = Class.forName(temp).newInstance();
								s+=temp;
							}
						}while(flag);
						if(rG.map.containsKey(temp)){
							rG.addToMap(new MapValue(obj[i],u,lvl,i));
						}else {
							ArrayList<MapValue> value = new ArrayList<MapValue>();
							value.add(new MapValue(obj[i],u,lvl,i));
							rG.map.put(temp, value);
						}
					}else {
						//taking an old value
						ArrayList<MapValue> value = rG.map.get(temp);
						int valueSize = value.size();
						int tempVal = random.nextInt(valueSize);
						MapValue nmv= value.get(tempVal);
						if(nmv.getMethodNumber()==-1 && nmv.getLvlNumber()==-1 && nmv.getCounterNumber()==-1) {
							value.remove(nmv);
							MapValue mvT = new MapValue(nmv.getValue(),u,lvl,i);
							rG.addToMap(mvT);
							s += mvT;
						}else {
							obj[i] = nmv.getValue();
							s += nmv;
						}
					}
					break;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("Instantiator error ClassNotFoundException " + e);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				System.err.println("Instantiator error InstantiationException " + e);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				System.err.println("Instantiator error IllegalAccessException " + e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				System.err.println("Instantiator error InvocationTargetException " + e);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				System.err.println("Instantiator error NoSuchMethodException " + e);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				System.err.println("Instantiator error SecurityException " + e);
			}
		}
		return new ParamSaver(obj,s);
	}

	/**
	 * @return the rG
	 */
	public RandomGenerator getrG() {
		return rG;
	}

	/**
	 * @param rG the rG to set
	 */
	public void setrG(RandomGenerator rG) {
		this.rG = rG;
	}
}

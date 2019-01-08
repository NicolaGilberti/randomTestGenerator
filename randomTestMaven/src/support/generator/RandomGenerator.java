package support.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomGenerator {
	private static Random random;
	protected HashMap<String, ArrayList<MapValue>> map;

	/**
	 * Constructor
	 */
	public RandomGenerator() {
		random = new Random();
		map = new HashMap<String, ArrayList<MapValue>>();
	}
	
	/**
	 * This method take in input an object and it return the same object instantiated with a random value 
	 * 
	 * @param inst Instantiator necessary for the function
	 * @param obj an object with a specific class but with no value
	 * @return the input instantiated with a random value
	 */
	public MapValue setValue(Instantiator inst, MapValue obj) {
		String key = obj.getValue().getClass().getName();
		if(!map.containsKey(key)) {
			obj = setNewValue(obj);
		}else {
			ArrayList<MapValue> value = map.get(key);
			int valueSize = value.size();
			int randomValue = random.nextInt(101);
			if(randomValue==100 && inst.getKey(key).contains("java.")) {
				obj.setValue(null);
			}else if(randomValue<49){
				obj=setNewValue(obj);
			}else {
				int tempVal = random.nextInt(valueSize);
				MapValue nmv= value.get(tempVal);
				if(nmv.getMethodNumber()==-1 && nmv.getLvlNumber()==-1 && nmv.getCounterNumber()==-1 && nmv.getVarDepthRef()==-1) {
					value.remove(nmv);
					obj.setValue(nmv.getValue());
					addToMap(obj);
				}else {
					obj = nmv;
				}
			}
		}
		return obj;
	}
	

	/**
	 * to add a new value to the map
	 * if the class of the value is already inside the map, then it find the list and it add the new value
	 * in other cases, a new row is inserted with the new class type and its first value
	 * 
	 * @param obj the object to add
	 */
	public void addToMap(MapValue obj) {
		String key = obj.getValue().getClass().getName();
		if(map.containsKey(key)) {
			ArrayList<MapValue> value = map.get(key);
			if(!value.contains(obj)) {
				value.add(obj);
			}
		}
		else {
			ArrayList<MapValue> value = new ArrayList<MapValue>();
			value.add(obj);
			map.put(key, value);
		}
	}

	/**
	 * This function generate new values for 'primitive' object given in input
	 * 
	 * @param obj the object to be instantiated
	 * @return the object set with a random value
	 */
	public MapValue setNewValue(MapValue obj) {
		random.setSeed(System.currentTimeMillis());
		switch(obj.getValue().getClass().getName()) {
		case "java.lang.Integer":
			obj.setValue(random.nextInt(Integer.MAX_VALUE));
			break;
		case "java.lang.Float":
			obj.setValue(random.nextFloat());
			break;
		case "java.lang.Short":
			obj.setValue(random.nextInt(65536) - 32768);
			break;
		case "java.lang.Long":
			obj.setValue(random.nextLong());
			break;
		case "java.lang.Double":
			obj.setValue(random.nextDouble());
			break;	
		case "java.lang.Boolean":
			obj.setValue((random.nextInt()%2==0) ? true : false);
			break;
		case "java.lang.Character":
			obj.setValue(generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefhijklmnopqrstuvwxyz", random));
			break;
		case "java.lang.String":
			obj.setValue(generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefhijklmnopqrstuvwxyz", random.nextInt(20), random));//word can be at most 19 character!!
			break;
		default:
			//do nothing -> maybe it can be a way to random generate a non primitive class in the future
			break;
		}
		addToMap(obj);
		return obj;
	}

	/**
	 * a simple String random generator
	 * 
	 * @param candidateChars  the candidate chars
	 * @param length  of the number of random chars to be generated
	 * @param random  a random generator
	 * @return a random String
	 */
	public static String generateRandomChars(String candidateChars, int length, Random random) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(candidateChars.charAt(random.nextInt(candidateChars
					.length())));
		}
		return sb.toString();
	}
	
	/**
	 * a simple Character random generator
	 * 
	 * @param candidateChars  the candidate chars
	 * @param random a random generator
	 * @return a random Character
	 */
	public static Character generateRandomChars(String candidateChars, Random random) {
		return candidateChars.charAt(random.nextInt(candidateChars.length()));
	}	

	/**
	 * print method to visualize the map and what contain
	 */
	public void showMap() {
		String s="";
		for(int i=0;i<map.size();i++) {
			s+="Key: "+map.keySet().toArray()[i]+" Values: ";
			for(int j=0; j<map.get(map.keySet().toArray()[i]).size() ;j++) {
				s+=map.get(map.keySet().toArray()[i]).get(j)+",";
			}
			s+="\n";
		}
		System.err.println(s);
	}
}

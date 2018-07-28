package support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomGenerator {
	private static int prob =1;
	private static Random random = new Random();
	private static HashMap<String, ArrayList<Object>> map = new HashMap();
	
	public static Object setValue(Object obj) {
		String key = obj.getClass().getName();
		if(!map.containsKey(key)) {
			obj = setNewValue(obj);
		}else {
			ArrayList value = map.get(key);
			int valueSize = value.size();
			int randomValue = random.nextInt(101);
			if(randomValue==100) {
				obj=null;
			}else if(randomValue<45){
				obj=setNewValue(obj);
			}else {
				int tempVal = random.nextInt(valueSize);
				obj = value.get(tempVal);
			}
		}
		return obj;
	}
	
	private static void addToMap(Object obj) {
		String key = obj.getClass().getName();
		if(map.containsKey(key)) {
			ArrayList value = map.get(key);
			if(!value.contains(obj)) {
				value.add(obj);
			}
		}
		else {
			ArrayList value = new ArrayList();
			value.add(obj);
			map.put(key, value);
		}
	}
	
	public static void addToMap(String className, Object obj) {
		if(map.containsKey(className)) {
			ArrayList value = map.get(className);
			if(!value.contains(obj)) {
				value.add(obj);
			}
		}else {
			ArrayList value = new ArrayList();
			value.add(obj);
			map.put(className, value);
		}
	}
	
	public static Object setNewValue(Object obj) {
		switch(obj.getClass().getName()) {
		case "java.lang.Integer":
			obj = random.nextInt(Integer.MAX_VALUE);
			break;
		case "java.lang.Float":
			obj = random.nextFloat();
			break;
		case "java.lang.Short":
			obj = random.nextInt(65536) - 32768;
			break;
		case "java.lang.Long":
			obj = random.nextLong();
			break;
		case "java.lang.Double":
			obj = random.nextDouble();
			break;	
		case "java.lang.Boolean":
			obj = (random.nextInt()%2==0) ? true : false;
			break;
		case "java.lang.String":
			obj = generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefhijklmnopqrstuvwxyz", random.nextInt(20), random);//word can be at most 19 character!!
			break;
		case "java.util.ArrayList":
			((ArrayList)obj).clear();
			break;
		default:
			//obj = null;
			break;
		}
		//System.out.println(obj.getClass().toString());
		addToMap(obj);
		return obj;
	}

	/**
	 * 
	 * @param candidateChars
	 *            the candidate chars
	 * @param length
	 *            the number of random chars to be generated
	 * @param random
	 * 			  random generator
	 * @return
	 */
	public static String generateRandomChars(String candidateChars, int length, Random random) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(candidateChars.charAt(random.nextInt(candidateChars
					.length())));
		}
		return sb.toString();
	}	
	
	public static void showMap() {
		String s="";
		for(int i=0;i<map.size();i++) {
			s+="Key: "+map.keySet().toArray()[i]+" Values: ";
			for(int j=0; j<map.get(map.keySet().toArray()[i]).size() ;j++) {
				s+=map.get(map.keySet().toArray()[i]).get(j)+",";
			}
			s+="\n";
		}
		System.out.println(s);
	}
}

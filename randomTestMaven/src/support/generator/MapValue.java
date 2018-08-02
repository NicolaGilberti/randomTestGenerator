package support.generator;

public class MapValue {
	private Object value;
	private int methodNumber;
	private int lvlNumber;
	private int counterNumber;
	
	public MapValue(Object o, int u, int lvl, int count) {
		this.value = o;
		this.methodNumber = u;
		this.lvlNumber = lvl;
		this.counterNumber = count;
	}
	
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return the methodNumber
	 */
	public int getMethodNumber() {
		return methodNumber;
	}
	/**
	 * @param methodNumber the methodNumber to set
	 */
	public void setMethodNumber(int methodNumber) {
		this.methodNumber = methodNumber;
	}
	/**
	 * @return the lvlNumber
	 */
	public int getLvlNumber() {
		return lvlNumber;
	}
	/**
	 * @param lvlNumber the lvlNumber to set
	 */
	public void setLvlNumber(int lvlNumber) {
		this.lvlNumber = lvlNumber;
	}
	/**
	 * @return the counterNumber
	 */
	public int getCounterNumber() {
		return counterNumber;
	}
	/**
	 * @param counterNumber the counterNumber to set
	 */
	public void setCounterNumber(int counterNumber) {
		this.counterNumber = counterNumber;
	}
	
	/**
	 * toString
	 */
	@Override
	public String toString() {
		String s="";
		
		s += this.value + ":" + this.methodNumber + ":" + this.lvlNumber + ":" + this.counterNumber;
		
		return s;
	}
}

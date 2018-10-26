package support.generator;

public class ParamSaver {
	//attributes
	private Object[] paramArray;
	private String paramString;

	/**
	 * 
	 * @param arr an object array
	 * @param s a String
	 */
	public ParamSaver(Object[] arr, String s) {
		this.paramArray = arr;
		this.paramString = s;
	}

	/**
	 * @return the paramArray
	 */
	public Object[] getParamArray() {
		return paramArray;
	}
	/**
	 * @param paramArray the paramArray to set
	 */
	public void setParamArray(Object[] paramArray) {
		this.paramArray = paramArray;
	}
	/**
	 * @return the paramString
	 */
	public String getParamString() {
		return paramString;
	}
	/**
	 * @param paramString the paramString to set
	 */
	public void setParamString(String paramString) {
		this.paramString = paramString;
	}

}

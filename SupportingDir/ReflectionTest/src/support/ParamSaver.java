package support;

public class ParamSaver {
	//attributes
	private Object[] paramArray;
	private String paramString;
	
	/**
	 * constructor 1
	 */
	public ParamSaver(int n) {
		this.paramArray = new Object[n];
		this.paramString = "";
	}
	
	/**
	 * constructor 2
	 */
	public ParamSaver(Object[] arr) {
		this.paramArray = arr;
		this.paramString = "";
	}
	
	/**
	 * constructor 3
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



import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Vector;


public class Stack2<String> extends Vector<String> {
	public static ArrayList<Integer> checker;

	public Stack2() {
		checker = new ArrayList();
	}

	public String push(String item) {
		addElement(item);
		return item;
	}

	public synchronized String pop() {
		String obj;
		int len = size();
		obj = peek();
		removeElementAt((len - 1));
		return obj;
	}

	public synchronized String peek() {
		int len = size();
		if (len == 0) {
			checker.add(0);
			throw new EmptyStackException();
		}
		return elementAt((len - 1));
	}

	public boolean empty() {
		return (size()) == 0;
	}

	public synchronized int search(String o) {
		int i = lastIndexOf(o);
		if (i >= 0) {
			checker.add(1);
			return (size()) - i;
		}
		return -1;
	}

	private static final long serialVersionUID = 1224463164541339165L;

	public ArrayList getChecker() {
		return checker;
	}

	public int setChecker() {
		checker.clear();
		return 0;
	}
}


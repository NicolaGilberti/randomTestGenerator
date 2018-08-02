

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Vector;


public class StackIntInstr extends Vector {
	public static ArrayList<Integer> checker;

	public StackIntInstr() {
		StackIntInstr.checker = new ArrayList();
	}

	public int push(int item) {
		addElement(item);
		return item;
	}

	public synchronized int pop() {
		int obj;
		int len = size();
		obj = peek();
		removeElementAt((len - 1));
		return obj;
	}

	public synchronized int peek() {
		int len = size();
		if (len == 0) {
			StackIntInstr.checker.add(0);
			throw new EmptyStackException();
		}
		return ((int) (elementAt((len - 1))));
	}

	public boolean empty() {
		return (size()) == 0;
	}

	public synchronized int search(int o) {
		int i = lastIndexOf(o);
		if (i >= 0) {
			StackIntInstr.checker.add(1);
			return (size()) - i;
		}
		return -1;
	}

	public ArrayList getChecker() {
		return StackIntInstr.checker;
	}

	public int setChecker() {
		StackIntInstr.checker.clear();
		return 0;
	}
}


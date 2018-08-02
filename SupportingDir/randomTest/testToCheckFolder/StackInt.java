import java.util.Vector;

import java.util.*;

public class StackInt extends Vector {

    public StackInt() {
    }

    public int push(int item) {
        addElement(item);

        return item;
    }

    public synchronized int pop() {
        int obj;
        int len = size();

        obj = peek();
        removeElementAt(len - 1);

        return obj;
    }

    public synchronized int peek() {
        int len = size();

        if (len == 0)
            throw new EmptyStackException();
        return (int) elementAt(len - 1);
    }

    public boolean empty() {
        return size() == 0;
    }

    public synchronized int search(int o) {
        int i = lastIndexOf(o);

        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }
}
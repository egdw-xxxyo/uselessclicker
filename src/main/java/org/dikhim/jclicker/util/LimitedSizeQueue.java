package org.dikhim.jclicker.util;

import java.util.ArrayList;

public class LimitedSizeQueue<K> extends ArrayList<K> {

	private static final long serialVersionUID = -2102376514568353785L;
	private int maxSize;

    public LimitedSizeQueue(int size){
        this.maxSize = size;
    }

    public boolean add(K k){
        boolean r = super.add(k);
        if (size() > maxSize){
            removeRange(0, size() - maxSize - 1);
        }
        return r;
    }

    public K getFirst() {
        return get(size() - 1);
    }

    public K getLast() {
        return get(0);
    }
    
    public K getFromBegin(int index) {
    	return get(index);
    }
    public K getFromEnd(int index) {
    	return get(size()-1-index);
    }
}
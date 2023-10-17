package org.dikhim.clickauto.util;

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
        	remove(0);
        }

        return r;
    }

    /**
     * returns last element
     * @return
     */
    public K getLast() {
        return get(size() - 1);
    }

    public K getFirst() {
        return get(0);
    }

    public K getFromBegin(int index) {
    	return get(index);
    }
    public K getFromEnd(int index) {
    	return get(size()-1-index);
    }
}
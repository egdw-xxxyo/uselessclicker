package org.dikhim.clickauto.jsengine.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LimitedPriorityQueue<T extends Comparable<T>> {
    private List<T> list = new ArrayList<>();
    private int sizeLimit;

    public LimitedPriorityQueue(int sizeLimit) {
        if (sizeLimit < 1) throw new IllegalArgumentException("Queue size should not be less than 1");
        this.sizeLimit = sizeLimit;
    }

    public void add(T t) {
        int i = 0;
        while (i < list.size() && list.get(i).compareTo(t) >= 0) {
            i++;
        }
        if (i >= list.size()) {
            list.add(t);
        } else {
            list.add(i, t);
        }

        if (list.size() > sizeLimit) {
            list.remove(list.size() - 1);
        }
    }

    public void addAll(Collection<T> collection) {
        collection.forEach(this::add);
    }

    public List<T> toList() {
        return list;
    }

    public void clear() {
        list.clear();
    }
}

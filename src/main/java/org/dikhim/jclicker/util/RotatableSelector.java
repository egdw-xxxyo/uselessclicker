package org.dikhim.jclicker.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class RotatableSelector<T> {
    private Property<T> selected = new SimpleObjectProperty<>();
    private List<T> list = new ArrayList<>();
    private int currentIndex = 0;

    public RotatableSelector(T object) {
        list.add(object);
        select(0);
    }

    public RotatableSelector(List<T> list) {
        this.list = list;
        if(list.size()<1) throw new IllegalArgumentException("List size should contain at least 1 element");
        select(0);
    }

    public void add(T object) {
        list.add(object);
    }

    public void rotate() {
        if (currentIndex + 1 > list.size() -1) {
            currentIndex = 0;
        }else {
            currentIndex++;
        }

        select(currentIndex);
    }

    public void select(int index) {
        if(index<0 || index> list.size()) throw new IllegalArgumentException("index should be between 0 and list.size");
        currentIndex = index;
        selected.setValue(list.get(index));
    }

    public T getSelected() {
        return selected.getValue();
    }

    public Property<T> selectedProperty() {
        return selected;
    }
    
    public int size() {
        return list.size();
    }
}

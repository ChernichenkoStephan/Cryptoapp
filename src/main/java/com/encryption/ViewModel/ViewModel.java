package com.encryption.ViewModel;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public abstract class ViewModel implements ActionListener {
    protected final ArrayList<PropertyChangeListener> listenerList = new ArrayList<>();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listenerList.add(listener);
    }

    public ViewModel(PropertyChangeListener listener) {
        listenerList.add(listener);
    }

    protected void notifyPropertyChangeListeners(String changeName, Object oldValue, Object newValue) {
        listenerList.forEach(propertyChangeListener ->
                propertyChangeListener.propertyChange(new PropertyChangeEvent(this, changeName, oldValue, newValue))
        );
    }
}

package emnist_number_predictor.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Listener<T> implements ChangeListener<T>{
    
    Runnable method;

    public Listener(Runnable method) {
        this.method = method;
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        method.run();
    }

}

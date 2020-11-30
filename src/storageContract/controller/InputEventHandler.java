package storageContract.controller;

import storageContract.controller.listener.EventListener;

import java.util.LinkedList;
import java.util.List;

public class InputEventHandler<T> {
    List<EventListener> listenerList = new LinkedList<>();

    public void add(EventListener listener){
        listenerList.add(listener);
    }

    public void remove(EventListener listener){
        listenerList.remove(listener);
    }

    public void handle(T incomingEvent){
        for (EventListener listener : listenerList) {
            listener.onInputEvent(incomingEvent);
        }
    }
}

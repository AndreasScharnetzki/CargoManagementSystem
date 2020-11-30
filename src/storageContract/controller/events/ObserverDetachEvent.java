package storageContract.controller.events;


import java.util.EventObject;

public class ObserverDetachEvent extends EventObject {

    private String observerName;
    public String getObserverName(){
        return this.observerName;
    }

    public ObserverDetachEvent(Object source, String observerName) {
        super(source);
        this.observerName = observerName;
    }
}

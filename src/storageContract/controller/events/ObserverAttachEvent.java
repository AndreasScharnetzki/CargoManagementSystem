package storageContract.controller.events;

import java.util.EventObject;

public class ObserverAttachEvent extends EventObject {

    private String observerName;

    public String getObserverName(){
        return this.observerName;
    }

    public ObserverAttachEvent(Object source, String observerName) {
        super(source);
        this.observerName = observerName;
    }
}

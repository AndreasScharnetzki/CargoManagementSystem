package storageContract.controller.events;

import java.util.EventObject;

public class CustomerDeletionEvent extends EventObject {

    private String nameToBeRemoved;

    public CustomerDeletionEvent(Object source, String nameToBeRemoved) {
        super(source);
        this.nameToBeRemoved = nameToBeRemoved;
    }

    public String getNameToBeDeleted() {
        return this.nameToBeRemoved;
    }
}

package storageContract.controller.events;

import java.util.EventObject;

public class CargoDeletionEvent extends EventObject {

    private int positionToBeDeleted;

    public CargoDeletionEvent(Object source, int positionToBeDeleted) {
        super(source);
        this.positionToBeDeleted = positionToBeDeleted;
    }

    public int getPositionToBeDeleted() {
        return this.positionToBeDeleted;
    }

}

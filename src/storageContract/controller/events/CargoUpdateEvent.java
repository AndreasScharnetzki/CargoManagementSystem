package storageContract.controller.events;

import java.util.EventObject;

public class CargoUpdateEvent extends EventObject {

    private int positionToBeUpdated;

    public int getPositionToBeUpdated(){
        return this.positionToBeUpdated;
    }

    public CargoUpdateEvent(Object source, int userInput) {
        super(source);
        this.positionToBeUpdated = userInput;
    }
}

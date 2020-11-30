package storageContract.controller.events;

import storageContract.cargo.interfaces.Cargo;
import java.util.EventObject;

public class CargoInsertEvent extends EventObject {

    private Cargo cargoToBeAdded;

    public CargoInsertEvent(Object source, Cargo cargoToBeAdded) {
        super(source);
        this.cargoToBeAdded = cargoToBeAdded;
    }

    public Cargo getCargoToBeAdded() {
        return cargoToBeAdded;
    }
}

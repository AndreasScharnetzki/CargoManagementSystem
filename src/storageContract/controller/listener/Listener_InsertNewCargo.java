package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoInsertEvent;
import storageContract.logic.BusinessLogicImpl;

import static storageContract.controller.CommandListAndMessages.*;

public class Listener_InsertNewCargo implements EventListener<CargoInsertEvent> {
    private ControlModel controlModel;

    public Listener_InsertNewCargo(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    @Override
    public void onInputEvent(CargoInsertEvent event) {
        if (event.getCargoToBeAdded() != null) {
            try {
                if (controlModel.getModel().addToStorehouse(event.getCargoToBeAdded())) {
                    System.out.println(SYMB_NICESU + "new Cargo item was added to storehouse.");
                }else{
                    System.out.println(SYMB_FAIL + "Cargo Insertion Process failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(SYMB_FAIL + "Cargo Insertion Process failed.");
        }
    }
}

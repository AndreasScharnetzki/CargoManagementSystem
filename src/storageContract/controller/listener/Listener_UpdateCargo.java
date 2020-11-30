package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoUpdateEvent;
import storageContract.logic.BusinessLogicImpl;

import static storageContract.controller.CommandListAndMessages.*;
import static storageContract.controller.CommandListAndMessages.ANSI_RESET;

public class Listener_UpdateCargo implements EventListener<CargoUpdateEvent> {

    private ControlModel controlModel;

    public Listener_UpdateCargo(ControlModel controlModel) {
        this.controlModel = controlModel;
    }
    @Override
    public void onInputEvent(CargoUpdateEvent event) {
        int positionToBeUpdated = event.getPositionToBeUpdated();
        try {
            if (positionToBeUpdated < 0 || positionToBeUpdated > controlModel.getModel().getMAX_STORAGE_CAPACITY()+1) {
                System.err.println(SYMB_FAIL + "The position you typed in is out of range, returning to main menu");
                return;
            } if (controlModel.getModel().getCargoByPosition(positionToBeUpdated) == null){
                System.err.println(SYMB_FAIL + "No Cargo is stored at the position you specified, returning to main menu");
            } else {
                controlModel.getModel().updateDateOfInspection(positionToBeUpdated);
              System.out.println(SYMB_NICESU + "cargo at position " + ANSI_GREEN + positionToBeUpdated + ANSI_RESET + " was successfully updated.");
            }
        } catch (Exception e) {
            System.err.println(SYMB_FAIL + "Unknown error occurred whilst trying to update the date of inspection on position " + positionToBeUpdated);
        }
    }
}

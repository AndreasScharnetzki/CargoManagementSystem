package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoDeletionEvent;
import storageContract.logic.BusinessLogicImpl;
import static storageContract.controller.CommandListAndMessages.*;

public class Listener_DeleteCargoAtPosition implements EventListener<CargoDeletionEvent> {
    private ControlModel controlModel;

    public Listener_DeleteCargoAtPosition(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    @Override
    public void onInputEvent(CargoDeletionEvent event) {
        try {
            controlModel.getModel().deleteStockAtPosition(event.getPositionToBeDeleted());
            System.out.println(SYMB_NICESU + "Cargo item at position: " + ANSI_GREEN + event.getPositionToBeDeleted() + ANSI_RESET + " was successfully removed from storehouse.");
        } catch (Exception e) {
            //Syntax related Error
            System.out.println(SYMB_FAIL + e.getLocalizedMessage() + " Returning to Main Menu.");
            return;
        }
    }
}


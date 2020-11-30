package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.CustomerDeletionEvent;
import storageContract.logic.BusinessLogicImpl;

import static storageContract.controller.CommandListAndMessages.*;

public class Listener_DeleteCustomerWithName implements EventListener<CustomerDeletionEvent> {
    private ControlModel controlModel;

    public Listener_DeleteCustomerWithName(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    public void onInputEvent(CustomerDeletionEvent event) {

        try {
            controlModel.getModel().deleteCustomer(event.getNameToBeDeleted());
            System.out.println(SYMB_NICESU + "Customer with name " + ANSI_GREEN + event.getNameToBeDeleted() + ANSI_RESET + " was successfully removed from Database.");
        } catch (Exception e) {
            //Syntax related Error
            System.out.println(SYMB_FAIL + e.getLocalizedMessage() + " Returning to Main Menu.");
            return;
        }
    }
}


package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.CustomerInsertEvent;
import storageContract.logic.BusinessLogicImpl;

import static storageContract.controller.CommandListAndMessages.*;

public class Listener_InsertNewCustomer implements EventListener<CustomerInsertEvent> {

    private ControlModel controlModel;

    public Listener_InsertNewCustomer(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    @Override
    public void onInputEvent(CustomerInsertEvent event) {
        String customerName= event.getCustomerName();
        if (customerName!= null) {
            try {
                controlModel.getModel().addToCustomerDatabase(customerName);
                System.out.println(SYMB_NICESU + "new customer " + ANSI_GREEN + customerName + ANSI_RESET + " was added to database added");
            } catch (Exception e) {
                //ADD related exceptions
                System.err.println(SYMB_FAIL + e.getLocalizedMessage() + "- returning to main menu.");
            }
        }
    }
}


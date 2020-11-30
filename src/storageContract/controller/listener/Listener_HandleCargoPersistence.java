package storageContract.controller.listener;

import storageContract.cargo.interfaces.Cargo;
import storageContract.controller.ControlModel;
import storageContract.controller.events.CargoPersistenceEvent;
import storageContract.serialisation.Persistence;

import static storageContract.controller.CommandListAndMessages.SYMB_FAIL;

public class Listener_HandleCargoPersistence implements EventListener<CargoPersistenceEvent> {
    private ControlModel controlModel;

    public Listener_HandleCargoPersistence(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    public void onInputEvent(CargoPersistenceEvent event) {
        if(event.getCommand().matches("save")){
            Persistence.serializeSingleCargo(controlModel.getModel(), event.getPosition());
        }else{
            //load
            int position =event.getPosition();
            Cargo cargo = Persistence.deserializeSingleCargo();
            Cargo[] storehouse = controlModel.getModel().getEntireCargo();

            if(position >= 0 && position < controlModel.getModel().getMAX_STORAGE_CAPACITY()){
                if(storehouse[position] == null){
                    if(controlModel.getModel().isKnownCustomer(cargo.getOwner().getName())){
                        storehouse[position] = cargo;
                        controlModel.getModel().getDateOfStorage()[position] = new java.util.Date();
                        controlModel.getModel().updateStorehouse(storehouse);
                    }else{
                        System.err.println(SYMB_FAIL + "Due to business policy unregistered customer["+cargo.getOwner().getName()+"] aren't allowed to store cargo. Returning to main menu.");
                    }
                }else{
                    System.err.println(SYMB_FAIL + "Cargo already exists at position(" + position + ") - business policy does not allow overwriting. Returning to main menu.");
                }
            }else{
                System.err.println(SYMB_FAIL + "Position(" + position + ") is out of scope - Returning to main menu.");
            }
        }
    }
}

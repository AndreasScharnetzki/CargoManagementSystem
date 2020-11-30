package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.PersistenceTriggerEvent;
import storageContract.serialisation.Persistence;

import java.io.FileNotFoundException;

public class Listener_HandlePersistence implements EventListener<PersistenceTriggerEvent> {
    private ControlModel controlModel;

    public Listener_HandlePersistence(ControlModel controlModel) {
        this.controlModel = controlModel;
    }

    @Override
    public void onInputEvent(PersistenceTriggerEvent event) {
        switch (event.getCommand()){
            case "saveJOS":
                Persistence.serializeJOS(controlModel.getModel(), event.getFilename());
                break;

            case "loadJOS":
                try {
                    if(Persistence.deserializeJOS(event.getFilename())!=null) {
                        controlModel.setModel(Persistence.deserializeJOS(event.getFilename()));
                    }else {
                        throw new IllegalArgumentException("An unknown error occurred.");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case "saveJBP":
                Persistence.serializeJBP(controlModel.getModel(), event.getFilename());
                break;

            case "loadJBP":
                try {
                    if(Persistence.deserializeJBP(event.getFilename())!=null) {
                        controlModel.setModel(Persistence.deserializeJBP(event.getFilename()));
                    }else {
                        throw new IllegalArgumentException("An unknown error occurred.");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

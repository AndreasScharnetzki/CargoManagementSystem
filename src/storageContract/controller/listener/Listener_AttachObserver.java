package storageContract.controller.listener;

import storageContract.controller.ControlModel;
import storageContract.controller.events.ObserverAttachEvent;
import storageContract.controller.observer.CapacityObserver;
import storageContract.controller.observer.HazardObserver;
import storageContract.logic.BusinessLogicImpl;

import static storageContract.controller.CommandListAndMessages.SYMB_FAIL;

public class Listener_AttachObserver implements EventListener<ObserverAttachEvent> {

        private ControlModel controlModel;

        public Listener_AttachObserver(ControlModel model) {
                this.controlModel = model;
        }

        @Override
        public void onInputEvent(ObserverAttachEvent event) {

                switch (event.getObserverName().toLowerCase()){
                        case("capacityobserver"):
                                controlModel.getModel().attach(new CapacityObserver(controlModel.getModel()));
                                break;
                        case("hazardobserver"):
                                controlModel.getModel().attach(new HazardObserver(controlModel.getModel()));
                                break;
                        default:
                                System.err.println(SYMB_FAIL + "unable to identify Observer Name, returning to main menu");
                                break;
                }
        }
}


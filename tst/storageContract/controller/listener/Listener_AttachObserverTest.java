package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.controller.ControlModel;
import storageContract.controller.events.ObserverAttachEvent;
import storageContract.logic.BusinessLogicImpl;

import static org.junit.jupiter.api.Assertions.*;

class Listener_AttachObserverTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));

    private Listener_AttachObserver listenerAttachObserver = new Listener_AttachObserver(controlModel);
    private ObserverAttachEvent capacityObserver = new ObserverAttachEvent(this, "CapacityObserver");
    private ObserverAttachEvent hazardObserver = new ObserverAttachEvent(this, "HazardObserver");
    private ObserverAttachEvent foo = new ObserverAttachEvent(this, "foo");

    @Test
    void onInputEvent() {
        //Testing the complete switch tree( two valid observers, one invalid)
        assertEquals(0,controlModel.getModel().getObservers().size());

        listenerAttachObserver.onInputEvent(capacityObserver);
        assertEquals(1,controlModel.getModel().getObservers().size());

        listenerAttachObserver.onInputEvent(hazardObserver);
        assertEquals(2,controlModel.getModel().getObservers().size());

        listenerAttachObserver.onInputEvent(foo);
        assertEquals(2,controlModel.getModel().getObservers().size());
    }

    @Test
    void onInputEvent_weirds_pelling() {
        ObserverAttachEvent weirdSpelling = new ObserverAttachEvent(this, "cApaCiTyObSeRVEr");
        //Testing the complete switch tree( two valid observers, one invalid)
        assertEquals(0,controlModel.getModel().getObservers().size());

        listenerAttachObserver.onInputEvent(weirdSpelling);
        assertEquals(1,controlModel.getModel().getObservers().size());
    }
}
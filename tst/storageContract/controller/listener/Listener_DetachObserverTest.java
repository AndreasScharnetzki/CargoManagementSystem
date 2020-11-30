package storageContract.controller.listener;

import org.junit.jupiter.api.Test;
import storageContract.controller.ControlModel;
import storageContract.controller.events.ObserverAttachEvent;
import storageContract.controller.events.ObserverDetachEvent;
import storageContract.controller.observer.CapacityObserver;
import storageContract.controller.observer.HazardObserver;
import storageContract.logic.BusinessLogicImpl;

import static org.junit.jupiter.api.Assertions.*;

class Listener_DetachObserverTest {

    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));

    private Listener_DetachObserver listener_detachObserver = new Listener_DetachObserver(controlModel);
    private ObserverDetachEvent capacityObserver = new ObserverDetachEvent(this, "capacityobserver");
    private ObserverDetachEvent hazardObserver = new ObserverDetachEvent(this, "HazardObserver");
    private ObserverDetachEvent foo = new ObserverDetachEvent(this, "foo");

    @Test
    void onInputEvent() {
        //Testing the all possibilities (two valid observers, one invalid)
        assertEquals(0,controlModel.getModel().getObservers().size());

        controlModel.getModel().attach(new CapacityObserver(controlModel.getModel()));
        assertEquals(1,controlModel.getModel().getObservers().size());

        controlModel.getModel().attach(new HazardObserver(controlModel.getModel()));
        assertEquals(2,controlModel.getModel().getObservers().size());

        listener_detachObserver.onInputEvent(capacityObserver);
        assertEquals(1,controlModel.getModel().getObservers().size());

        listener_detachObserver.onInputEvent(foo);
        assertEquals(1,controlModel.getModel().getObservers().size());

        listener_detachObserver.onInputEvent(hazardObserver);
        assertEquals(0,controlModel.getModel().getObservers().size());
    }
}
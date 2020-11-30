package storageContract.controller;

import org.junit.jupiter.api.Test;
import storageContract.controller.events.CustomerDisplayEvent;
import storageContract.controller.listener.Listener_InsertNewCustomer;
import storageContract.logic.BusinessLogicImpl;

import static org.junit.jupiter.api.Assertions.*;

class InputEventHandlerTest {
    private ControlModel controlModel = new ControlModel(new BusinessLogicImpl(3));

    private InputEventHandler inputEventHandler = new InputEventHandler();
    private Listener_InsertNewCustomer listener_insertNewCustomer = new Listener_InsertNewCustomer(controlModel);

    @Test
    void add() {
        assertEquals(0, inputEventHandler.listenerList.size());
        inputEventHandler.add(listener_insertNewCustomer);
        assertEquals(1, inputEventHandler.listenerList.size());
    }

    @Test
    void remove() {
        assertEquals(0, inputEventHandler.listenerList.size());
        inputEventHandler.add(listener_insertNewCustomer);
        assertEquals(1, inputEventHandler.listenerList.size());
        inputEventHandler.remove(listener_insertNewCustomer);
        assertEquals(0, inputEventHandler.listenerList.size());
    }

}
package storageContract.controller.events;

import java.util.EventObject;

public class ConsoleInputEvent extends EventObject {

    private String text;

    public ConsoleInputEvent(Object source, String text) {
        super(source);
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}

package storageContract.controller.events;

import java.util.EventObject;

public class CargoPersistenceEvent extends EventObject {
    private final String command;
    private final int position;

    public CargoPersistenceEvent(Object source, String command, int position) {
        super(source);
        this.command = command;
        this.position = position;
    }

    public String getCommand() {
        return command;
    }

    public int getPosition() {
        return position;
    }
}

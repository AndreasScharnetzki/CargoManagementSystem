package storageContract.controller.events;

import java.util.EventObject;

public class PersistenceTriggerEvent extends EventObject {
    private final String command;
    private final String filename;

    public PersistenceTriggerEvent(Object source, String command, String filename) {
        super(source);
        this.command = command;
        this.filename = filename;
    }

    public String getCommand() {
        return command;
    }

    public String getFilename() {
        return filename;
    }
}

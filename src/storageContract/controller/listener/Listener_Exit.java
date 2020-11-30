package storageContract.controller.listener;

import storageContract.controller.events.ConsoleInputEvent;

public class Listener_Exit implements EventListener<ConsoleInputEvent> {

    @Override
    public void onInputEvent(ConsoleInputEvent event) {
        if(event.getText() != null && event.getText().equals("exit")){
            System.out.println("The application will shut down now.");
            System.exit(0);
        }
    }
}

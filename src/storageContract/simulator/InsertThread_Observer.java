package storageContract.simulator;

import storageContract.controller.observer.Observer;

public class InsertThread_Observer implements Observer {

    private Thread insertThread;
    private Simulator s;

    public InsertThread_Observer(Thread insertThread, Simulator s) {
        this.insertThread = insertThread;
        this.s = s;
    }

    @Override
    public void update() {
        //have fun future_me!
    }
}

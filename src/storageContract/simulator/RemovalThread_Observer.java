package storageContract.simulator;

import storageContract.controller.observer.Observer;

public class RemovalThread_Observer implements Observer {

    private Thread removalThread;
    private Simulator s;

    public RemovalThread_Observer(Thread removalThread, Simulator s){
        this.removalThread = removalThread;
        this.s = s;
    }

    public void update() {
        //so long so long so long and thanks for all the fish!
    }
}

package storageContract.controller.observer;

//equates a "Publisher"
public interface Subject {
    void attach(Observer o);    //register new subscriber
    void detach(Observer o);    //de-register an observer/subscriber
    void informObserver();
}

package storageContract.controller.listener;

public interface EventListener<T> extends java.util.EventListener {
    void onInputEvent(T event);
}

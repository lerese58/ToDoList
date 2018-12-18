package App.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundOperation implements Observable, Runnable {

    private List<Observer> listeners;
    private Thread newThread;

    public BackgroundOperation(Runnable job) {
        newThread = new Thread(job);
        listeners = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer o) {
        listeners.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        listeners.remove(o);
    }

    @Override
    public void notifyObserver() {
        if (!listeners.isEmpty())
            listeners.forEach(Observer::handleEvent);
    }

    @Override
    public void run() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(newThread);
        service.execute(this::notifyObserver);
        service.shutdown();
    }
}

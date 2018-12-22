package App.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundOperation<TResult> implements IBackgroundOperation<TResult> {

    private List<Observer> _observers;
    private ExecutorService _executorService;
    private TResult _result;
    private Callable<TResult> _callable;

    public BackgroundOperation(ExecutorService executorService) {
        _executorService = executorService;
        _observers = new ArrayList<>();
    }

    public BackgroundOperation() {
        _executorService = Executors.newSingleThreadExecutor();
        _observers = new ArrayList<>();
    }

    @Override
    public void setCallable(Callable<TResult> callable) {
        _callable = callable;
    }

    @Override
    public TResult start() {
        ExecutorService _executorService = Executors.newSingleThreadExecutor();
        _executorService.execute(this::run);
        _executorService.shutdown();
        return _result;
    }

    @Override
    public void run() {
        try {
            _result = _executorService.submit(_callable).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            _executorService.shutdown();
        }
    }

    @Override
    public void addObserver(Observer o) {
        _observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        _observers.remove(o);
    }

    @Override
    public void notifyObserver(Long progress) {
        _observers.forEach((observer) -> observer.handleEvent(progress));
    }
}
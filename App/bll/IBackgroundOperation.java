package App.bll;

import java.util.concurrent.Callable;

public interface IBackgroundOperation<TResult> {
    void run();

    void setCallable(Callable<TResult> o);

    TResult start();

    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObserver(Long progress);
}

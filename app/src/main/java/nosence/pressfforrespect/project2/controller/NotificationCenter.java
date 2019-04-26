package nosence.pressfforrespect.project2.controller;

import java.util.ArrayList;
import java.util.List;

public class NotificationCenter {
    private List<Observer> observers = new ArrayList<>();
    private static NotificationCenter notificationCenter;

    public static NotificationCenter getInstance(){
        if(notificationCenter == null)
            notificationCenter = new NotificationCenter();
        return notificationCenter;
    }

    public interface Observer {
        void update(int state);
    }

    public void dataLoaded(int state){
        notifyObservers(state);
    }

    public void register(Observer observer){
        observers.add(observer);
    }

    public void unRegister(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(int state){
        for (Observer observer : observers) {
            observer.update(state);
        }
    }
}

package com.aqinn.actmanagersysandroid.datafortest;

import java.util.List;

/**
 * @author Aqinn
 * @date 2020/12/18 2:51 PM
 */
public abstract class DataSource<T> {

    protected List<T> datas;

    protected List<Observer> observers;

    public void attach(Observer o) {
        this.observers.add(o);
    }

    public void notifyAllObserver() {
        for (Observer o:observers
             ) {
            o.update();
        }
    }

    public List<T> getDatas() {
        return datas;
    }

}

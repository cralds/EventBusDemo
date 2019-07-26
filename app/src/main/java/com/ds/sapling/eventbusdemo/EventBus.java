package com.ds.sapling.eventbusdemo;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {
    private static volatile EventBus instance;
    private Map<Object, List<Subcribtion>> subscribeEventMap;//事件类型 对应 订阅者，订阅方法

    private Map<Object,List<Class<?>>> eventSubscribeMap;//订阅者对应事件   主要用于解绑
    private ExecutorService executor = Executors.newCachedThreadPool();

    public static Handler handler = new Handler(Looper.getMainLooper());

    private EventBus(){
        subscribeEventMap = new HashMap<>();
        eventSubscribeMap = new HashMap<>();
    }

    public static EventBus getInstance() {
        if (instance == null){
            synchronized (EventBus.class){
                if (instance == null){
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void post(Object event){
        List<Subcribtion> subcribtions = subscribeEventMap.get(event.getClass());
        for (Subcribtion subcribtion : subcribtions){
            postByThread(event, subcribtion);
        }
    }

    private void invoke(Object event, Subcribtion subcribtion) {
        try {
            subcribtion.method.method.invoke(subcribtion.subcribe,event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void postByThread(final Object event, final Subcribtion subcribtion){
        ThreadMode mode = subcribtion.method.threadMode;
        switch (mode){
            case io:
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        invoke(event,subcribtion);
                    }
                });
                break;
            case main:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        invoke(event,subcribtion);
                    }
                });
                break;
        }
    }

    public void register(Object subcriber){
        List<SubcriptionMethod> list =  FindAllMethodUtils.getAllAnnoMethod(subcriber);

        for (SubcriptionMethod method : list){
            subscribe(subcriber,method);
        }

    }

    public void subscribe(Object subcriber,SubcriptionMethod method){
        Subcribtion subcribtion = new Subcribtion(subcriber,method);
        Object eventType = method.eventType;
        List<Subcribtion> list = subscribeEventMap.get(eventType);
        if (list == null){
            list = new ArrayList<>();
            subscribeEventMap.put(method.eventType,list);
        }
        list.add(subcribtion);

        //关联订阅者 和 事件
        List<Class<?>> types = eventSubscribeMap.get(subcriber);
        if (types == null){
            types = new ArrayList<>();
            eventSubscribeMap.put(subcriber,types);
        }
        types.add(method.eventType);
    }

    public void  unRegister(Object subscribe){
        List<Class<?>> subcribtions = eventSubscribeMap.get(subscribe);
        if (subcribtions == null){
            Log.e("EventBus","alrady unregister");
            return;
        }
        for (Class<?> eventType : subcribtions){
            unBindMap(subscribe,eventType);
        }

        eventSubscribeMap.remove(subscribe);
    }


    private void unBindMap(Object subscribe,Class<?> eventType){
        List<Subcribtion> subcribtionList = subscribeEventMap.get(eventType);
        int size = subcribtionList.size();
        for (int i=0;i<size;i++){
            Subcribtion subcribtion = subcribtionList.get(i);
            if (subcribtion.subcribe == subscribe){
                subcribtionList.remove(i);
                i--;
                size--;
            }
        }
    }

}

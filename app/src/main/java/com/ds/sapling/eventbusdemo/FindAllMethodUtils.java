package com.ds.sapling.eventbusdemo;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindAllMethodUtils {
    public static Map<Object, List<SubcriptionMethod>> cacheMap = new HashMap<>();//缓存

    //获取改类所有的注解方法,包括父类的注解方法
    public static List<SubcriptionMethod> getAllAnnoMethod(Object subcriber){

        List<SubcriptionMethod> list = cacheMap.get(subcriber);
        if (list != null){
            return list;
        }
        list = new ArrayList<>();
        Class<?> clazz = subcriber.getClass();
        while (clazz != null) {
            getSimClsMethod(list, clazz);
            clazz = getSupClass(clazz);
        }
        return list;
    }

    public static Class<?> getSupClass(Class<?> clazz){
        Class<?> superCls = clazz.getSuperclass();
        String clazzName = superCls.getName();
        if (clazzName.startsWith("java.") || clazzName.startsWith("javax.") || clazzName.startsWith("android.")) {
            superCls = null;
        }
        return superCls;
    }

    private static void getSimClsMethod(List<SubcriptionMethod> list, Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods){
            Class<?>[] eventTypes = method.getParameterTypes();
            if (eventTypes.length == 1) {
                Subcribe subcribe = method.getAnnotation(Subcribe.class);
                if (subcribe != null) {
                    SubcriptionMethod eventMethod = new SubcriptionMethod();
                    eventMethod.eventType = eventTypes[0];
                    eventMethod.method = method;
                    eventMethod.threadMode = subcribe.value();
                    list.add(eventMethod);
                }
            }else {
                Log.e("EventBus","eventbus only suppport one params");
            }
        }
    }
}

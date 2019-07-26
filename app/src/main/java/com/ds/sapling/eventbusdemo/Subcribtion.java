package com.ds.sapling.eventbusdemo;

public class Subcribtion {
    public Object subcribe;
    public SubcriptionMethod method;

    public Subcribtion(Object subcribe, SubcriptionMethod method) {
        this.subcribe = subcribe;
        this.method = method;
    }
}

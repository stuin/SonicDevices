package com.stuintech.sonicdevices.util;

import java.util.ArrayList;

public class SyncedList<T> extends ArrayList<T> {
    private int next = -1;
    private T none;

    public SyncedList(T none) {
        this.none = none;
    }

    public int addNext(T o) {
        //Add new item
        int out = next + 1;
        if(next > -1) {
            set(next, o);
        } else {
            //Locate best item location
            out = 0;
            while(out < size() && get(out) != none)
                out++;

            //Set new timer
            if(out >= size()) {
                add(o);
                out = size();
            } else
                set(out, o);
        }
        next = -1;
        return out;
    }

    public boolean has(int i) {
        return i >= 0 && i < size() && get(i) != none;
    }

    public void clear(int i) {
        set(i, none);
        if(next < i)
            next = i;
    }
}

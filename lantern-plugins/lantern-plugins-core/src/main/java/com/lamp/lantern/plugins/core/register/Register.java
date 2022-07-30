package com.lamp.lantern.plugins.core.register;

import java.util.List;

public interface Register<T> {

    default int batchRegister(List<T> list) {
        for (T t : list) {
            register(t);
        }
        return 1;
    }
    
    int register(T t);

    int unRegister(T t);
}

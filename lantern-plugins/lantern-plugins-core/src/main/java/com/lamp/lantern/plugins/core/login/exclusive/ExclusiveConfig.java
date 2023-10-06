package com.lamp.lantern.plugins.core.login.exclusive;

import lombok.Data;
import lombok.Setter;

@Data
public class ExclusiveConfig {

    private Method method = Method.KICK;
    public enum Method{
        KICK,
        REFUSE,

        NONE
    }



}

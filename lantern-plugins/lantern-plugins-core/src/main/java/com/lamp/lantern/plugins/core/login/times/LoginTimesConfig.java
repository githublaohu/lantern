package com.lamp.lantern.plugins.core.login.times;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class LoginTimesConfig {

  private boolean isWholeNumber = true;

  private Integer times = 5;

  private TimeUnit timeUnit = TimeUnit.DAYS;

  private Long timeLong = 60L;

  private Integer addressTimes = -1;

  private TimeUnit addressTimeUnit = TimeUnit.DAYS;

  private Long addressTimeLong = 60L;
}

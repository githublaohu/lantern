package com.lamp.lantern.service.action.login.handler;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class LoginTimesConfig {

  private boolean isWholeMumber = true;

  private Integer times = 5;

  private TimeUnit timeUnit = TimeUnit.DAYS;

  private Long timeLong = 1L;

  private Integer addressLimes = -1;

  private TimeUnit addressTimeUnit = TimeUnit.DAYS;

  private Long addressTimeLong = 1L;
}

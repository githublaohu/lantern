/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
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

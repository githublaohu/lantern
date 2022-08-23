package com.lamp.lantern.plugins.core.token;

import java.util.Random;
import java.util.UUID;

public class UUidTokenService {

  public static final String uuid = UUID.randomUUID().toString().replaceAll("-", "");

  public static void main(String[] args) {
    System.out.println(uuid);
  }
}

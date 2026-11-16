package com.lamp.lantern.plugins.core.login;

/** Short-lived, browser-bound OAuth requests. Values supplied here are SHA-256 digests. */
public interface EnterpriseWechatStateStore {

    void save(String stateHash, String browserHash, int ttlSeconds);

    /** Must atomically consume at most one unexpired request, including across application instances. */
    boolean consume(String stateHash, String browserHash);
}

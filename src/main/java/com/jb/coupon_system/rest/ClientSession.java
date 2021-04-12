package com.jb.coupon_system.rest;

/**
 * A class in which the value of the tokens map is defined, using id and time last accessed.
 */
public class ClientSession {
    private long clientId;
    private long lastAccessedMillis;

    public ClientSession(long clientId, long currentTimeMillis) {
        this.clientId = clientId;
        this.lastAccessedMillis = currentTimeMillis;
    }

    public long getClientId() {
        return clientId;
    }

    /**
     * A constructor method with the purpose of creating a clientSession with the current time.
     *
     * @param clientId
     * @return ClientSession
     */
    public static ClientSession create(long clientId) {
        return new ClientSession(clientId, System.currentTimeMillis());
    }

    public void access() {
        lastAccessedMillis = System.currentTimeMillis();
    }

    public long getLastAccessedMillis() {
        return lastAccessedMillis;
    }
}

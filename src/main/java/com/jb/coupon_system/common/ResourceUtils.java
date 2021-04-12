package com.jb.coupon_system.common;

import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.ex.InvalidLoginException;

/**
 * A utility class simplifying the tokens' verification and access
 */
public class ResourceUtils {

    /**
     * A utility method used in the verification process
     * the method gets the clientSession via the token
     * and either returns a valid id (belonging to an ADMIN, COMPANY or CUSTOMER)
     * or throws an InvalidLoginException
     *
     * @param clientSession
     * @return id of the user logging in
     * @throws InvalidLoginException
     */
    public static long accessAndGetIdFromClientSessionOrThrow(ClientSession clientSession) throws InvalidLoginException {

        if (clientSession == null) {
            throw new InvalidLoginException("There's a problem with the token being used!");
        }
        clientSession.access();
        return clientSession.getClientId();
    }
}

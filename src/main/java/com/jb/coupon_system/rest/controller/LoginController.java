package com.jb.coupon_system.rest.controller;

import com.jb.coupon_system.common.LoginType;
import com.jb.coupon_system.common.ResourceUtils;
import com.jb.coupon_system.rest.ClientSession;
import com.jb.coupon_system.rest.UserSystem;
import com.jb.coupon_system.rest.ex.InvalidLoginException;
import com.jb.coupon_system.rest.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class LoginController {
    private static final int LENGTH_TOKEN = 15;

    private final Map<String, ClientSession> tokensMap;
    private final UserSystem userSystem;

    @Autowired
    public LoginController(@Qualifier("tokens") Map<String, ClientSession> tokensMap, UserSystem userSystem) {
        this.tokensMap = tokensMap;
        this.userSystem = userSystem;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestParam String email, @RequestParam String password, @RequestParam String stringLoginType) throws InvalidLoginException {
        /*Make stringLoginType provided non case sensitive*/
        stringLoginType = stringLoginType.toUpperCase();

        /*Convert stringLoginType to enumLoginType verifying its existence as an enum*/
        LoginType enumLoginType = convertStringToEnumOrThrow(stringLoginType);

        /*Create ClientSession using the enumLoginType*/
        ClientSession session = userSystem.createSession(email, password, enumLoginType);

        /*Generate specified stringLoginType token*/
        String token = generateToken(stringLoginType);

        /*Store the token in the tokensMap*/
        tokensMap.put(token, session);

        Token tokenToReturn = new Token(token);
        /*Return the token*/
        return ResponseEntity.ok(tokenToReturn);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String token) throws InvalidLoginException {

        ResourceUtils.accessAndGetIdFromClientSessionOrThrow(tokensMap.get(token));

        tokensMap.remove(token);

        return ResponseEntity.ok().build()  ;
    }

    /**
     * A method that converts the string LoginType provided, to an enum type field
     * and also makes sure it actually exists as one
     *
     * @param stringLoginType provided to be converted
     * @return enumLoginType
     * @throws InvalidLoginException if the string converted doesn't match any of the existing enum types
     */
    private LoginType convertStringToEnumOrThrow(String stringLoginType) throws InvalidLoginException {
        LoginType enumLoginType;
        try {
            enumLoginType = LoginType.valueOf(stringLoginType);
        } catch (IllegalArgumentException e) {
            String msg = String.format("The loginType provided %s is incorrect!", stringLoginType);
            throw new InvalidLoginException(msg);
        }
        return enumLoginType;
    }

    /**
     * A method that creates a specified login type token using the stringLoginType
     *
     * @param loginType
     * @return token
     */
    private String generateToken(String loginType) {
        return loginType
                + "_"
                + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, LENGTH_TOKEN);
    }
}
package com.dai.userservice;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashingService {

    private static final String ALG = "SHA-256";

    public String digest(String value) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance(ALG);
        byte [] hash = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(hash);
    }
}

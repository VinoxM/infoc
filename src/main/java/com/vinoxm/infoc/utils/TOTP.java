package com.vinoxm.infoc.utils;

import lombok.extern.log4j.Log4j2;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Date;

@Log4j2
public class TOTP {
    private static final String SECRET_KEY = "Vmlub3hNL01lZXBsSW4=";

    private static final String CRYPTO = "HmacSHA1";

    private static final long STEP = 20000;

    private static final long INITIAL_TIME = 0;

    private static final long FLEXIBILIT_TIME = 5000;

    private TOTP() {
    }

    public static boolean verifyTOTPRigidity(String code, String pass, String totp) {
        log.info("Verify TOTP Code [" + totp + "]");
        return generateMyTOTP(code, pass).equals(totp);
    }

    public static boolean verify(String code, String pass, String totp) {
        log.info("Verify TOTP Code [" + totp + "]");
        long now = new Date().getTime();
        String time = Long.toHexString(timeFactor(now)).toUpperCase();
        String tempTotp = generateTOTP(code + pass + SECRET_KEY, time);
        if (tempTotp.equals(totp)) {
            return true;
        }
        String time2 = Long.toHexString(timeFactor(now - FLEXIBILIT_TIME)).toUpperCase();
        String tempTotp2 = generateTOTP(code + pass + SECRET_KEY, time2);
        return tempTotp2.equals(totp);
    }

    private static long timeFactor(long targetTime) {
        return (targetTime - INITIAL_TIME) / STEP;
    }

    private static byte[] hmac_sha(byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(CRYPTO);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "AES");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    private static byte[] hexStr2Bytes(String hex) {
        BigInteger bigInt =  new BigInteger("10" + hex, 16);
        byte[] bArray = bigInt.toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    private static String generateTOTP(String key, String time) {
        StringBuilder timeBuilder = new StringBuilder(time);
        while (timeBuilder.length() < 16)
            timeBuilder.insert(0, "0");
        time = timeBuilder.toString();

        byte[] msg = hexStr2Bytes(time);
        byte[] k = key.getBytes();
        byte[] hash = hmac_sha(k, msg);
        return truncate(hash);
    }

    public static String generateMyTOTP(String code, String pass) {
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(pass)) {
            throw new RuntimeException("账户密码不许为空");
        }
        long now = new Date().getTime();
        String time = Long.toHexString(timeFactor(now)).toUpperCase();
        String totp = generateTOTP(code + pass + SECRET_KEY, time);
        log.info("Create TOTP Code [" + totp + "]");
        return totp;
    }

    private static String truncate(byte[] target) {
        int offset = target[target.length - 1] & 0xf;
        long binary = ((target[offset] & 0x7f) << 24)
                | ((target[offset + 1] & 0xff) << 16)
                | ((target[offset + 2] & 0xff) << 8) | (target[offset + 3] & 0xff);

        return new BigInteger(String.valueOf(binary)).toString(36);
    }
}

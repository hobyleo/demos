package com.hoby.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;

public class Sm3Utils {

    private static final String ENCODING = "utf-8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String paramStr) {
        String resultHexString = "";
        try {
            byte[] srcData = paramStr.getBytes(ENCODING);
            byte[] resultHash = hash(srcData);
            resultHexString = ByteUtils.toHexString(resultHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHexString.toUpperCase();
    }

    public static boolean verify(String srcStr, String sm3Hexstring) {
        boolean flag = false;
        try {
            byte[] srcData = srcStr.getBytes(ENCODING);
            byte[] sm3Hash = ByteUtils.fromHexString(sm3Hexstring);
            byte[] newHash = hash(srcData);
            if (Arrays.equals(newHash, sm3Hash)) {
                flag = true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static byte[] hash(byte[] srcDate) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcDate, 0, srcDate.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    public static String encryptWithKey(String paramStr, String key) {
        String resultHexString = "";
        try {
            byte[] srcData = paramStr.getBytes(ENCODING);
            byte[] keyData = key.getBytes(ENCODING);
            byte[] resultHash = hmac(keyData, srcData);
            resultHexString = ByteUtils.toHexString(resultHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultHexString.toUpperCase();
    }

    public static boolean verifyWithKey(String srcStr, String sm3Hexstring, String key) {
        boolean flag = false;
        try {
            byte[] srcData = srcStr.getBytes(ENCODING);
            byte[] sm3Hash = ByteUtils.fromHexString(sm3Hexstring);
            byte[] keyData = key.getBytes(ENCODING);
            byte[] newHash = hmac(keyData, srcData);
            if (Arrays.equals(newHash, sm3Hash)) {
                flag = true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static byte[] hmac(byte[] key, byte[] srcData) {
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(srcData, 0, srcData.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    public static void main(String[] args) {
        String str = "测试字符串";
        String key = "4CF7D0E384633FBD8EE9F063A2E06E89F60FAE359515459369F29F6ED039EDFE";

        String hash = encrypt(str);
        boolean flag = verify(str, hash);

        System.out.println(hash);
        System.out.println(flag);

        hash = encryptWithKey(str, key);
        flag = verifyWithKey(str, hash, key);

        System.out.println(hash);
        System.out.println(flag);
    }

}

package pw.prok.damask.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Digest {
    public enum DigestType {
        MD5("MD5"), SHA1("SHA-1");

        private final String mType;

        DigestType(String type) {
            mType = type;
        }

        public String getType() {
            return mType;
        }
    }

    private static final Charset UTf_8 = Charset.forName("utf-8");

    public static byte[] digest(InputStream source, DigestType type) {
        try {
            MessageDigest digest = MessageDigest.getInstance(type.getType());
            byte[] buffer = new byte[4096];
            int c;
            while ((c = source.read(buffer)) > 0) {
                digest.update(buffer, 0, c);
            }
            source.close();
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException("Failed to digest message!", e);
        }
    }

    public static byte[] digest(File source, DigestType type) throws FileNotFoundException {
        return digest(new FileInputStream(source), type);
    }

    public static byte[] digest(byte[] source, DigestType type) {
        return digest(new ByteArrayInputStream(source), type);
    }

    public static byte[] digest(String source, DigestType type) {
        return digest(source.getBytes(UTf_8), type);
    }

    public static byte[] md5(InputStream source) {
        return digest(source, DigestType.MD5);
    }

    public static byte[] md5(File source) throws FileNotFoundException {
        return digest(source, DigestType.MD5);
    }

    public static byte[] md5(byte[] source) {
        return digest(source, DigestType.MD5);
    }

    public static byte[] md5(String source) {
        return digest(source, DigestType.MD5);
    }

    public static byte[] sha1(InputStream source) {
        return digest(source, DigestType.SHA1);
    }

    public static byte[] sha1(File source) throws FileNotFoundException {
        return digest(source, DigestType.SHA1);
    }

    public static byte[] sha1(byte[] source) {
        return digest(source, DigestType.SHA1);
    }

    public static byte[] sha1(String source) {
        return digest(source, DigestType.SHA1);
    }

    public static String toHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "";
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) builder.append(String.format("%02X", b & 0xFF));
        return builder.toString();
    }
}

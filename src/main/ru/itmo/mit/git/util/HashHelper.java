package ru.itmo.mit.git.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class HashHelper {

    public static String getHashFromString(String data) {
        return DigestUtils.sha1Hex(data);
    }

    public static String getHashFromFile(Path file) throws IOException {
        byte[] data = Files.readAllBytes(file);
        return DigestUtils.sha1Hex(data);
    }

    public static String hashFile(Path file, Path storage) throws IOException {
        String hash = getHashFromFile(file);
        Path copied = storage.resolve(hash);
        Files.copy(file, copied, StandardCopyOption.REPLACE_EXISTING);
        return hash;
    }

}

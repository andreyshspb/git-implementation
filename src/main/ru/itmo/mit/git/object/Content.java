package ru.itmo.mit.git.object;

import org.jetbrains.annotations.NotNull;
import ru.itmo.mit.git.util.HashHelper;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Content implements Serializable {

    private final Map<String, String> nameToHash = new HashMap<>();

    public void add(String name, String hash) {
        nameToHash.put(name, hash);
    }

    public void rm(String name) {
        nameToHash.remove(name);
    }

    public boolean fileExists(String filename) {
        return nameToHash.containsKey(filename);
    }

    public Set<String> getFiles() {
        return nameToHash.keySet();
    }

    public String getFileHash(String filename) {
        return nameToHash.get(filename);
    }

    public String getHash() {
        String serialization = nameToHash.entrySet().stream()
                .map(value -> value.getKey() + value.getValue())
                .collect(Collectors.joining());
        return HashHelper.getHashFromString(serialization);
    }


    public static @NotNull Content buildFromDirectory(Path pathToDir, String prefixBan) throws IOException {
        Content content = new Content();
        Iterable<Path> paths = Files.walk(pathToDir)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        for (Path path : paths) {
            Path tempPath = path.subpath(pathToDir.getNameCount(), path.getNameCount());
            String hash = HashHelper.getHashFromFile(path);
            if (!tempPath.startsWith(prefixBan)) {
                content.add(tempPath.toString(), hash);
            }
        }
        return content;
    }
}

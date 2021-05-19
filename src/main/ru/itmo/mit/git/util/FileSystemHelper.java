package ru.itmo.mit.git.util;

import ru.itmo.mit.git.object.Content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileSystemHelper {

    public static void clearDir(Path dir, String prefixBan) throws IOException {
        for (Path path : Files.newDirectoryStream(dir)) {
            Path tempPath = path.subpath(dir.getNameCount(), path.getNameCount());
            if (Files.isRegularFile(path)) {
                Files.delete(path);
            }
            if (Files.isDirectory(path) && !tempPath.startsWith(prefixBan)) {
                deleteDir(path);
            }
        }
    }

    public static void feelDir(Path dir, Content content, Path storage) throws IOException {
        for (var filename : content.getFiles()) {
            Path file = dir.resolve(filename);
            Path parentDir = file.getParent();
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            Path source = storage.resolve(content.getFileHash(filename));
            Files.copy(source, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void deleteDir(Path dir) throws IOException {
        for (Path path : Files.newDirectoryStream(dir)) {
            if (Files.isDirectory(path)) {
                deleteDir(path);
            } else {
                Files.delete(path);
            }
        }
        Files.delete(dir);
    }

}

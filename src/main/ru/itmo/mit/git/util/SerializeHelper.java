package ru.itmo.mit.git.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SerializeHelper {

    public static <T> void saveToFile(@NotNull T object, Path pathToFile) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(pathToFile));
        output.writeObject(object);
    }

    public static <T> @NotNull T loadFromFile(Path pathToFile, Class<T> clazz) throws IOException {
        ObjectInputStream input = new ObjectInputStream(Files.newInputStream(pathToFile));
        try {
            Object obj = input.readObject();
            return clazz.cast(obj);
        } catch (ClassNotFoundException | ClassCastException exception) {
            throw new IOException(exception);
        }
    }

}

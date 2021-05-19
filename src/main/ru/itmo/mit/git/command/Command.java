package ru.itmo.mit.git.command;

import java.io.IOException;
import java.util.List;

public abstract class Command {
    public abstract void perform(List<String> arguments) throws IOException;
}

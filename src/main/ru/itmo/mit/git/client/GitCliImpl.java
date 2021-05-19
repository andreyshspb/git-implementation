package ru.itmo.mit.git.client;

import org.jetbrains.annotations.NotNull;
import ru.itmo.mit.git.command.Command;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import ru.itmo.mit.git.command.*;

import static ru.itmo.mit.git.client.GitConstants.*;


public class GitCliImpl implements GitCli {

    private final GitClient gitClient;
    private final Map<String, Command> commandMap = new HashMap<>();

    public GitCliImpl(String workingDir) {
        gitClient = new GitClient(workingDir);
        commandMap.put(INIT, new InitCommand(gitClient));
        commandMap.put(COMMIT, new CommitCommand(gitClient));
        commandMap.put(RESET, new ResetCommand(gitClient));
        commandMap.put(LOG, new LogCommand(gitClient));
        commandMap.put(CHECKOUT, new CheckoutCommand(gitClient));
        commandMap.put(STATUS, new StatusCommand(gitClient));
        commandMap.put(ADD, new AddCommand(gitClient));
        commandMap.put(RM, new RmCommand(gitClient));
        commandMap.put(BRANCH_CREATE, new CreateBranchCommand(gitClient));
        commandMap.put(BRANCH_REMOVE, new RemoveBranchCommand(gitClient));
        commandMap.put(SHOW_BRANCHES, new ShowBranchesCommand(gitClient));
    }

    @Override
    public void runCommand(@NotNull String commandName, @NotNull List<@NotNull String> arguments) throws GitException {
        if (!commandName.equals(INIT) && !gitClient.isInit()) {
            throw new GitException("git repo is not init here");
        }
        Command command = commandMap.get(commandName);
        if (command == null) {
            throw new GitException("Unknown command: " + commandName);
        }
        try {
            command.perform(arguments);
        } catch (IOException exception) {
            throw new GitException(exception);
        }

    }

    @Override
    public void setOutputStream(@NotNull PrintStream outputStream) {
        gitClient.setOutput(outputStream);
    }

    @Override
    public @NotNull String getRelativeRevisionFromHead(int n) throws GitException {
        try {
            return gitClient.getRelativeRevision(n);
        } catch (IOException exception) {
            throw new GitException(exception);
        }
    }
}

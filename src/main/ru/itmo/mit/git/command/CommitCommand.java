package ru.itmo.mit.git.command;

import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.util.List;


public class CommitCommand extends Command {

    private final GitClient gitClient;

    public CommitCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        String message = arguments.get(0);
        gitClient.loadState();
        gitClient.saveIndex();
        gitClient.createCommit(message);
        gitClient.saveState();
    }
}

package ru.itmo.mit.git.command;

import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.util.List;


public class ResetCommand extends Command {

    private final GitClient gitClient;

    public ResetCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        gitClient.loadState();
        String commitID = arguments.get(0);
        gitClient.moveToCommit(commitID, true);
        gitClient.saveState();
    }
}

package ru.itmo.mit.git.command;

import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.util.List;


public class RmCommand extends Command {

    private final GitClient gitClient;

    public RmCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        gitClient.loadState();
        for (String filename : arguments) {
            performHelper(filename);
        }
        gitClient.saveState();
    }

    private void performHelper(String filename) {
        gitClient.getIndex().rm(filename);
    }
}

package ru.itmo.mit.git.command;

import ru.itmo.mit.git.object.Commit;
import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.util.List;


public class LogCommand extends Command {

    private final GitClient gitClient;

    public LogCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        gitClient.loadState();
        if (arguments.isEmpty()) {
            performHelper(gitClient.getHead().getHash());
        } else {
            performHelper(arguments.get(0));
        }
    }

    private void performHelper(String commitID) throws IOException {
        Commit commit = gitClient.loadFromObjectsDir(commitID, Commit.class);
        while (true) {
            gitClient.getOutput().println(commit.info());
            if (commit.getParentID() == null) {
                break;
            }
            commit = gitClient.loadFromObjectsDir(commit.getParentID(), Commit.class);
        }
    }
}

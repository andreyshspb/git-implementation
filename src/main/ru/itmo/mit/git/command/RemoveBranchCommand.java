package ru.itmo.mit.git.command;

import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static ru.itmo.mit.git.client.GitConstants.*;


public class RemoveBranchCommand extends Command {

    private final GitClient gitClient;

    public RemoveBranchCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        gitClient.loadState();
        String name = arguments.get(0);
        if (name.equals(INITIAL_BRANCH_NAME)) {
            throw new IOException("you can not remove initial branch");
        }
        if (Files.notExists(gitClient.branchesDir.resolve(name))) {
            throw new IOException("the specified branch does not exist");
        }
        if (name.equals(gitClient.getBranch().getName())) {
            throw new IOException("yoy can not remove the current branch");
        }
        Files.delete(gitClient.branchesDir.resolve(name));
        gitClient.saveState();
    }
}

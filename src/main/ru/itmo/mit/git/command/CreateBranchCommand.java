package ru.itmo.mit.git.command;

import ru.itmo.mit.git.object.Branch;
import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.util.List;

public class CreateBranchCommand extends Command {

    private final GitClient gitClient;

    public CreateBranchCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        gitClient.loadState();
        String name = arguments.get(0);
        gitClient.setBranch(new Branch(name, gitClient.getHead().getHash()));
        gitClient.saveState();
    }
}

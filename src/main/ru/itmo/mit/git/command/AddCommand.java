package ru.itmo.mit.git.command;

import ru.itmo.mit.git.util.HashHelper;
import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


public class AddCommand extends Command {

    private final GitClient gitClient;

    public AddCommand(GitClient gitClient) {
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

    private void performHelper(String filename) throws IOException {
        Path file = gitClient.workingDir.resolve(filename);
        String hash = HashHelper.hashFile(file, gitClient.objectsDir);
        gitClient.getIndex().add(filename, hash);
    }
}

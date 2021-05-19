package ru.itmo.mit.git.command;

import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class ShowBranchesCommand extends Command {

    private final GitClient gitClient;

    public ShowBranchesCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        Files.walk(gitClient.branchesDir)
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .forEach(gitClient.getOutput()::println);
    }
}

package ru.itmo.mit.git.command;

import ru.itmo.mit.git.client.GitClient;
import ru.itmo.mit.git.object.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public class InitCommand extends Command {

    private final GitClient gitClient;

    public InitCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        if (gitClient.isInit()) {
            throw new IOException("git repo already exists here");
        }
        Files.createDirectories(gitClient.configDir);
        Files.createDirectories(gitClient.objectsDir);
        Files.createDirectories(gitClient.branchesDir);
        Files.createFile(gitClient.indexFile);
        Files.createFile(gitClient.headFile);
        Files.createFile(gitClient.branchFile);
        Files.createFile(gitClient.initialBranch);

        gitClient.setIndex(new Content());
        gitClient.setHead(new Commit(null, gitClient.getIndex().getHash(),
                "initial commit", System.currentTimeMillis()));
        gitClient.setBranch(new Branch("master", gitClient.getHead().getHash()));

        gitClient.saveIndex();
        gitClient.saveState();
    }
}

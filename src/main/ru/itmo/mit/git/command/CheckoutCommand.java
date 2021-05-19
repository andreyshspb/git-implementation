package ru.itmo.mit.git.command;

import ru.itmo.mit.git.object.Branch;
import ru.itmo.mit.git.util.SerializeHelper;
import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;


public class CheckoutCommand extends Command {

    private final GitClient gitClient;

    public CheckoutCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        if (arguments.isEmpty()) {
            throw new IOException("too few arguments");
        }
        gitClient.loadState();
        String arg = arguments.get(0);
        if (arg.equals("--")) {
            for (int i = 1; i < arguments.size(); i++) {
                cancelChanges(arguments.get(i));
            }
            gitClient.saveState();
            return;
        }
        if (Files.exists(gitClient.branchesDir.resolve(arg))) {
            checkoutByBranch(arg);
            gitClient.saveState();
            return;
        }
        checkoutByCommit(arg);
        gitClient.saveState();
    }

    private void checkoutByBranch(String name) throws IOException {
        Branch branch = SerializeHelper.loadFromFile(gitClient.branchesDir
                .resolve(name), Branch.class);
        gitClient.setBranch(branch);
        checkoutByCommit(branch.getCommitID());
    }

    private void checkoutByCommit(String commitID) throws IOException {
        gitClient.moveToCommit(commitID, false);
    }

    private void cancelChanges(String filename) throws IOException {
        if (Files.notExists(gitClient.workingDir.resolve(filename))) {
            gitClient.getOutput().println(filename + " does not exist in the directory");
            return;
        }
        if (!gitClient.getIndex().fileExists(filename)) {
            gitClient.getOutput().println(filename + " does not exist in the index");
            return;
        }

        Path source = gitClient.objectsDir
                .resolve(gitClient.getIndex().getFileHash(filename));
        Path copied = gitClient.workingDir.resolve(filename);
        Files.copy(source, copied, StandardCopyOption.REPLACE_EXISTING);
    }
}

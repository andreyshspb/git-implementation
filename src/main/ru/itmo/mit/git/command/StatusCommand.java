package ru.itmo.mit.git.command;

import ru.itmo.mit.git.object.Content;
import ru.itmo.mit.git.client.GitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.itmo.mit.git.client.GitConstants.*;


public class StatusCommand extends Command {

    private final GitClient gitClient;

    public StatusCommand(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    @Override
    public void perform(List<String> arguments) throws IOException {
        gitClient.loadState();
        gitClient.getOutput().println("Current branch is " + gitClient.getBranch().getName());
        readyToCommitFiles();
        trackedFiles();
        untrackedFiles();
    }

    private void readyToCommitFiles() throws IOException {
        Content content = gitClient.loadFromObjectsDir(gitClient.getHead().getContentID(), Content.class);
        List<String> newFiles = getNewFiles(gitClient.getIndex(), content);
        List<String> removedFiles = getRemovedFiles(gitClient.getIndex(), content);
        List<String> modifiedFiles = getModifiedFiles(gitClient.getIndex(), content);
        if (!newFiles.isEmpty() || !removedFiles.isEmpty() || !modifiedFiles.isEmpty()) {
            gitClient.getOutput().println("\nReady to commit files: ");
            printNewFiles(newFiles);
            printRemovedFiles(removedFiles);
            printModifiedFiles(modifiedFiles);
        }
    }

    private void trackedFiles() throws IOException {
        Content currentTree = Content.buildFromDirectory(gitClient.workingDir, CONFIG_DIR);
        List<String> modifiedFiles = getModifiedFiles(currentTree, gitClient.getIndex());
        List<String> removedFiles = getRemovedFiles(currentTree, gitClient.getIndex());
        if (!modifiedFiles.isEmpty() || !removedFiles.isEmpty()) {
            gitClient.getOutput().println("\nTracked files: ");
            printRemovedFiles(removedFiles);
            printModifiedFiles(modifiedFiles);
        }
    }

    private void untrackedFiles() throws IOException {
        Content currentTree = Content.buildFromDirectory(gitClient.workingDir, CONFIG_DIR);
        List<String> newFiles = getNewFiles(currentTree, gitClient.getIndex());
        if (!newFiles.isEmpty()) {
            gitClient.getOutput().println("\nUntracked files: ");
            printNewFiles(newFiles);
        }
    }

    private List<String> getNewFiles(Content first, Content second) {
        List<String> result = new ArrayList<>();
        for (var filename : first.getFiles()) {
            if (!second.fileExists(filename)) {
                result.add(filename);
            }
        }
        return result;
    }

    private List<String> getRemovedFiles(Content first, Content second) {
        List<String> result = new ArrayList<>();
        for (var filename : second.getFiles()) {
            if (!first.fileExists(filename)) {
                result.add(filename);
            }
        }
        return result;
    }

    private List<String> getModifiedFiles(Content first, Content second) {
        List<String> result = new ArrayList<>();
        for (var filename : first.getFiles()) {
            String firstHash = first.getFileHash(filename);
            String secondHash = second.getFileHash(filename);
            if (secondHash != null && !secondHash.equals(firstHash)) {
                result.add(filename);
            }
        }
        return result;
    }

    private void printNewFiles(List<String> newFiles) {
        for (String filename : newFiles) {
            gitClient.getOutput().println("\tnew:\t" + filename);
        }
    }

    private void printRemovedFiles(List<String> newFiles) {
        for (String filename : newFiles) {
            gitClient.getOutput().println("\tremoved:\t" + filename);
        }
    }

    private void printModifiedFiles(List<String> newFiles) {
        for (String filename : newFiles) {
            gitClient.getOutput().println("\tmodified:\t" + filename);
        }
    }
}

package ru.itmo.mit.git.client;

import org.jetbrains.annotations.NotNull;
import ru.itmo.mit.git.object.Branch;
import ru.itmo.mit.git.object.Commit;
import ru.itmo.mit.git.object.Content;
import ru.itmo.mit.git.util.FileSystemHelper;
import ru.itmo.mit.git.util.SerializeHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.itmo.mit.git.client.GitConstants.*;


public class GitClient {

    private PrintStream output = System.out;

    public final Path workingDir;
    public final Path configDir;
    public final Path objectsDir;
    public final Path branchesDir;
    public final Path indexFile;
    public final Path headFile;
    public final Path branchFile;
    public final Path initialBranch;

    private Content index;
    private Commit head;
    private Branch branch;

    public GitClient(String workingDirName) {
        workingDir = Path.of(workingDirName);
        configDir = workingDir.resolve(CONFIG_DIR);
        objectsDir = workingDir.resolve(OBJECTS_DIR);
        branchesDir = workingDir.resolve(BRANCHES_DIR);
        indexFile = workingDir.resolve(INDEX_FILE);
        headFile = workingDir.resolve(HEAD_FILE);
        branchFile = workingDir.resolve(BRANCH_FILE);
        initialBranch = branchesDir.resolve(INITIAL_BRANCH_NAME);
    }


    public String getRelativeRevision(int n) throws IOException {
        loadState();
        Commit commit = head;
        for (int i = 0; i < n; i++) {
            if (commit.getParentID() == null) {
                throw new IOException("the specified commit does not exists");
            }
            commit = loadFromObjectsDir(commit.getParentID(), Commit.class);
        }
        return commit.getHash();
    }

    public void createCommit(String message) {
        head = new Commit(head.getHash(), index.getHash(),
                message, System.currentTimeMillis());
        branch.setCommitID(head.getHash());
    }

    public void saveIndex() throws IOException {
        String contentID = index.getHash();
        SerializeHelper.saveToFile(index, objectsDir.resolve(contentID));
    }

    public void loadState() throws IOException {
        index = SerializeHelper.loadFromFile(indexFile, Content.class);
        head = SerializeHelper.loadFromFile(headFile, Commit.class);
        branch = SerializeHelper.loadFromFile(branchFile, Branch.class);
    }

    public <T> @NotNull T loadFromObjectsDir(String filename, Class<T> clazz) throws IOException {
        return SerializeHelper.loadFromFile(objectsDir.resolve(filename), clazz);
    }

    public void moveToCommit(String commitID, boolean isReset) throws IOException {
        FileSystemHelper.clearDir(workingDir, CONFIG_DIR);
        Commit commit = SerializeHelper.loadFromFile(objectsDir
                .resolve(commitID), Commit.class);
        Content content = SerializeHelper.loadFromFile(objectsDir
                .resolve(commit.getContentID()), Content.class);
        FileSystemHelper.feelDir(workingDir, content, workingDir
                .resolve(OBJECTS_DIR));
        index = content;
        head = commit;
        if (isReset) {
            branch.setCommitID(head.getHash());
        }
    }

    public void saveState() throws IOException {
        SerializeHelper.saveToFile(index, indexFile);
        SerializeHelper.saveToFile(head, objectsDir.resolve(head.getHash()));
        SerializeHelper.saveToFile(head, headFile);
        SerializeHelper.saveToFile(branch, branchFile);
        SerializeHelper.saveToFile(branch, branchesDir.resolve(branch.getName()));
    }

    public PrintStream getOutput() {
        return output;
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    public boolean isInit() {
        return Files.exists(configDir);
    }

    public Content getIndex() {
        return index;
    }

    public void setIndex(Content index) {
        this.index = index;
    }

    public Commit getHead() {
        return head;
    }

    public void setHead(Commit head) {
        this.head = head;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}

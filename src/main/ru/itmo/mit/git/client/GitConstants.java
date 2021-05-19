package ru.itmo.mit.git.client;

import org.jetbrains.annotations.NotNull;

public final class GitConstants {
    private GitConstants() {}

    public static final @NotNull String INIT = "init";
    public static final @NotNull String COMMIT = "commit";
    public static final @NotNull String RESET = "reset";
    public static final @NotNull String LOG = "log";
    public static final @NotNull String CHECKOUT = "checkout";
    public static final @NotNull String STATUS = "status";
    public static final @NotNull String ADD = "add";
    public static final @NotNull String RM = "rm";
    public static final @NotNull String BRANCH_CREATE = "branch-create";
    public static final @NotNull String BRANCH_REMOVE = "branch-remove";
    public static final @NotNull String SHOW_BRANCHES = "show-branches";
    public static final @NotNull String MERGE = "merge";

    public static final @NotNull String MASTER = "master";

    public static final @NotNull String INITIAL_COMMIT_MESSAGE = "initial commit";
    public static final @NotNull String INITIAL_BRANCH_NAME = "master";

    public static final @NotNull String CONFIG_DIR = "git";
    public static final @NotNull String OBJECTS_DIR = "git/objects";
    public static final @NotNull String BRANCHES_DIR = "git/branches";
    public static final @NotNull String INDEX_FILE = "git/index";
    public static final @NotNull String HEAD_FILE = "git/HEAD";
    public static final @NotNull String BRANCH_FILE = "git/branch";
}

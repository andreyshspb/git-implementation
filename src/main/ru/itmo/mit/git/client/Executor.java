package ru.itmo.mit.git.client;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Executor {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("specify the command");
            return;
        }

        String command = args[0];
        List<String> arguments = Arrays.stream(args)
                .skip(1)
                .collect(Collectors.toList());

        GitCli git = new GitCliImpl(".");
        try {
            git.runCommand(command, arguments);
        } catch (GitException exception) {
            System.out.println(exception.getMessage());
        }
    }

}

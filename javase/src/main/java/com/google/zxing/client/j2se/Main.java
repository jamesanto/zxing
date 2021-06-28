package com.google.zxing.client.j2se;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            CommandLineRunner.main(args);
        } else if(args[0].equalsIgnoreCase("--server")) {
            HttpServer.main(Arrays.copyOfRange(args, 1, args.length));
        } else if(args[0].equalsIgnoreCase("--cli")) {
            CommandLineRunner.main(Arrays.copyOfRange(args, 1, args.length));
        } else {
            CommandLineRunner.main(args);
        }
    }
}

package com.siddharth;

public class InputFileNotFoundException extends Exception {
    public InputFileNotFoundException(String message) {
        super("File - " + message+"- not present in the current directory.");
        System.out.println("File - " + message+"- not present in the current directory.");
    }
}


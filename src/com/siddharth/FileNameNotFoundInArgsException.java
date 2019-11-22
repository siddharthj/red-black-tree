package com.siddharth;

public class FileNameNotFoundInArgsException extends Exception {
    public FileNameNotFoundInArgsException() {
        super("File name not provided in the args");
        System.out.println("File name not provided in the args");
    }
}


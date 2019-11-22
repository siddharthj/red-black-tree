package com.siddharth;

public class InputNotInCorrectFormatException extends Exception {
    public InputNotInCorrectFormatException(String input) {
        super("Input - "+  input + " - not in the correct format");
        System.out.println("Input - "+  input + " - not in the correct format");
    }
}

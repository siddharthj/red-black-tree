package com.siddharth;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputParser {
    private static Pattern PATTERN1 = Pattern.compile("Insert\\((\\d+),(\\d+)\\)");
    private static Pattern PATTERN2 = Pattern.compile("PrintBuilding\\((\\d+),(\\d+)\\)");
    private static Pattern PATTERN3 = Pattern.compile("PrintBuilding\\((\\d+)\\)");

    public static List<InputNode> parseInput(List<String> inputLines) throws InputNotInCorrectFormatException {
        List<InputNode> nodes = new ArrayList<>();
        for (String inputLine : inputLines) {
            String[] inputPattern = inputLine.split(":");
            String input = inputPattern[1].trim();
            Integer timeToStart = Integer.valueOf(inputPattern[0]);
            Matcher m1 = PATTERN1.matcher(input);
            if (m1.find()) {
                InputNode node = new InputNode(timeToStart, InputType.INSERT, Integer.valueOf(m1.group(1)), Integer.valueOf(m1.group(2)));
                nodes.add(node);
            } else {
                Matcher m2 = PATTERN2.matcher(input);
                if (m2.find()) {
                    InputNode node = new InputNode(timeToStart, InputType.PRINT, Integer.valueOf(m2.group(1)), Integer.valueOf(m2.group(2)));
                    nodes.add(node);
                } else {
                    Matcher m3 = PATTERN3.matcher(input);
                    if (m3.find()) {
                        InputNode node = new InputNode(timeToStart, InputType.PRINT, Integer.valueOf(m3.group(1)));
                        nodes.add(node);
                    } else {
                        throw new InputNotInCorrectFormatException(input);
                    }
                }
            }
        }
        return nodes;
    }
}


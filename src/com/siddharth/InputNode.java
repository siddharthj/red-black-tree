package com.siddharth;

public class InputNode {

    public InputNode(Integer globalTime, InputType inputType, Integer input1, Integer input2) {
        this.globalTime = globalTime;
        this.inputType = inputType;
        this.input1 = input1;
        this.input2 = input2;
    }

    public InputNode(Integer globalTime, InputType inputType, Integer input1) {
        this.globalTime = globalTime;
        this.inputType = inputType;
        this.input1 = input1;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public Integer getInput1() {
        return input1;
    }

    public void setInput1(Integer input1) {
        this.input1 = input1;
    }

    public Integer getInput2() {
        return input2;
    }

    public void setInput2(Integer input2) {
        this.input2 = input2;
    }

    public Integer getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(Integer globalTime) {
        this.globalTime = globalTime;
    }

    private InputType inputType;
    private Integer input1;
    private Integer input2;
    private Integer globalTime;
}

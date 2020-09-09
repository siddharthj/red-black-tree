/**
 * POJO for holding the input command data in code readable format
 */
public class InputNode {

    /**
     * Input Node constructor
     * @param globalTime
     * @param inputType
     * @param input1
     * @param input2
     */
    public InputNode(Integer globalTime, InputType inputType, Integer input1, Integer input2) {
        this.globalTime = globalTime;
        this.inputType = inputType;
        this.input1 = input1;
        this.input2 = input2;
    }

    /**
     * Input Node constructor  without parameter 2
     * @param globalTime
     * @param inputType
     * @param input1
     */
    public InputNode(Integer globalTime, InputType inputType, Integer input1) {
        this.globalTime = globalTime;
        this.inputType = inputType;
        this.input1 = input1;
    }

    /**
     * Get input type, INSERT or PRINT
     * @return
     */
    public InputType getInputType() {
        return inputType;
    }

    /**
     * Set input type
     * @param inputType
     */
    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    /**
     * get parameter 1
     * @return
     */
    public Integer getInput1() {
        return input1;
    }

    /**
     * set parameter 1
     * @param input1
     */
    public void setInput1(Integer input1) {
        this.input1 = input1;
    }

    /**
     * get parameter 2
     * @return
     */
    public Integer getInput2() {
        return input2;
    }

    /**
     * set parameter 2
     * @param input2
     */
    public void setInput2(Integer input2) {
        this.input2 = input2;
    }

    /**
     * get global time
     * @return
     */
    public Integer getGlobalTime() {
        return globalTime;
    }

    /**
     * set global time
     * @param globalTime
     */
    public void setGlobalTime(Integer globalTime) {
        this.globalTime = globalTime;
    }

    /**
     * Input type (one of the enum values)
     */
    private InputType inputType;
    /**
     * input param 1 in the paranthesis
     */
    private Integer input1;
    /**
     * input param 2 in the paranthesis, it can be null
     */
    private Integer input2;
    /**
     * The time at which we need to execute this input
     */
    private Integer globalTime;
}

/**
 * Node for RBT Tree, it will hold building data in the data field.
 */
public class Node {
    /**
     * Get buildingNum
     * @return
     */
    public Integer getBuildingNum() {
        return buildingNum;
    }

    /**
     * Set buildingNum
     * @param buildingNum
     */
    public void setBuildingNum(Integer buildingNum) {
        this.buildingNum = buildingNum;
    }

    /**
     * Get data
     * @return
     */
    public BuildingData getData() {
        return data;
    }

    /**
     * Set data
     * @param data
     */
    public void setData(BuildingData data) {
        this.data = data;
    }

    /**
     * Get left node
     * @return
     */
    public Node getLeftNode() {
        return left;
    }

    /**
     * Set left node
     * @param left
     */
    public void setLeftNode(Node left) {
        this.left = left;
    }

    /**
     * Get right node
     * @return
     */
    public Node getRightNode() {
        return right;
    }

    /**
     * Set right node
     * @param right
     */
    public void setRightNode(Node right) {
        this.right = right;
    }

    /**
     * Get color of the node.
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set color of the node.
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the size of this node
     * @return
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Set the size of this node
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    private Integer buildingNum;
    private BuildingData data;
    private Node left, right;
    private Color color;
    private Integer size;

    /**
     * Constructor of the node
     * @param buildingNum
     * @param data
     * @param color
     * @param size
     */
    public Node(Integer buildingNum, BuildingData data, Color color, Integer size) {
        this.buildingNum = buildingNum;
        this.data = data;
        this.color = color;
        this.size = size;
    }
}
import java.util.*;
/**
 * Red Black Tree Implementation
 */
public class RedBlackTree {
    /**
     * root of the red black tree
     */

    private Node root;

    /**
     * Constructor
     */
    public RedBlackTree() {
    }

    /**
     * Get the building data for a a given buildingNum. return Optional.empty() if the input buildingNum is not present
     * in the tree
     *
     * @param buildingNum
     * @return
     */
    Optional<BuildingData> get(Integer buildingNum) {
        if (isNull(buildingNum)) {
            throw new RuntimeException("buildingNum is null.");
        }
        return get(root, buildingNum);
    }

    /**
     * Get the data of a particular building node iteratively.
     *
     * @param node
     * @param buildingNum
     * @return
     */
    private Optional<BuildingData> get(Node node, Integer buildingNum) {
        while (null != node) {
            if (buildingNum < node.getBuildingNum()) {
                node = node.getLeftNode();
            } else if (buildingNum > node.getBuildingNum()) {
                node = node.getRightNode();
            } else {
                return Optional.of(node.getData());
            }
        }
        return Optional.empty();
    }

    /**
     * Is the buildingNum present in the red black tree or not.
     *
     * @param buildingNum
     * @return
     */
    private boolean isBuildingPresentInTree(Integer buildingNum) {
        return get(buildingNum).isPresent();
    }

    /**
     * Add building data to the red black tree.
     *
     * @param buildingNum
     * @param value
     */
    public void addBuildingToTree(Integer buildingNum, BuildingData value) {
        if (isNull(buildingNum)) {
            throw new RuntimeException("buildingNum is null");
        }

        root = addBuildingToTree(root, buildingNum, value);
        root.setColor(Color.BLACK);
    }

    /**
     * Add building to the tree
     *
     * @param node
     * @param buildingNum
     * @param value
     * @return
     */
    private Node addBuildingToTree(Node node, Integer buildingNum, BuildingData value) {
        if (isNull(node)) {
            // Added node in the tree is always red. After that we check if it violates any RBT properties
            return new Node(buildingNum, value, Color.RED, 1);
        }

        if (buildingNum < node.getBuildingNum()) {
            node.setLeftNode(addBuildingToTree(node.getLeftNode(), buildingNum, value));
        } else if (buildingNum > node.getBuildingNum()) {
            node.setRightNode(addBuildingToTree(node.getRightNode(), buildingNum, value));
        } else {
            node.setData(value);
        }

        if (isRed(node.getRightNode()) && !isRed(node.getLeftNode())) {
            node = rotateLeft(node);
        }
        if (isRed(node.getLeftNode()) && isRed(node.getLeftNode().getLeftNode())) {
            node = rotateRight(node);
        }
        if (isRed(node.getLeftNode()) && isRed(node.getRightNode())) {
            swapColorsOfNodes(node);
        }

        int newSize = size(node.getRightNode()) + size(node.getLeftNode()) + 1;
        node.setSize(newSize);

        return node;
    }

    /**
     * Delete min buildingNum in the subtrees of the node.
     *
     * @param node
     * @return
     */
    private Node deleteMin(Node node) {
        if (isNull(node.getLeftNode())) {
            return null;
        }
        if (!isRed(node.getLeftNode()) && !isRed(node.getLeftNode().getLeftNode())) {
            node = moveRedLeft(node);
        }

        node.setLeftNode(deleteMin(node.getLeftNode()));

        return checkAndFixTreeAnomalies(node);
    }

    /**
     * Delete a building with provided buildingNum, throw runtime exception if the building is not found.
     *
     * @param buildingNum
     */
    public void deleteTheGivenBuildingNum(Integer buildingNum) {
        if (buildingNum == null) {
            throw new RuntimeException("buildingNum to be deleted is null.");
        }
        if (!isBuildingPresentInTree(buildingNum)) {
            // Building to be deleted is not present in the tree, do nothing.
            return;
        }

        // if both children of root are black, set root to red
        if (!isRed(root.getLeftNode()) && !isRed(root.getRightNode())) {
            root.setColor(Color.RED);
        }

        // Delete the node!!!
        root = deleteTheGivenBuildingNodeInSubtree(root, buildingNum);

        if (!isRedBlackTreeEmpty()) {
            // If the tree is not empty and after rotations the root node is red, then set it to black.
            root.setColor(Color.BLACK);
        }
    }

    /**
     * Delete  the node provided in the input.
     *
     * @param node
     * @param buildingNum
     * @return
     */
    private Node deleteTheGivenBuildingNodeInSubtree(Node node, Integer buildingNum) {
        if (buildingNum < node.getBuildingNum()) {
            if (!isRed(node.getLeftNode()) && !isRed(node.getLeftNode().getLeftNode())) {
                node = moveRedLeft(node);
            }
            node.setLeftNode(deleteTheGivenBuildingNodeInSubtree(node.getLeftNode(), buildingNum));
        } else {
            if (isRed(node.getLeftNode())) {
                node = rotateRight(node);
            }
            if (buildingNum.equals(node.getBuildingNum()) && (node.getRightNode() == null)) {
                return null;
            }
            if (!isRed(node.getRightNode()) && !isRed(node.getRightNode().getLeftNode())) {
                node = moveRedRight(node);
            }
            if (buildingNum.equals(node.getBuildingNum())) {
                Node x = getTreeMinNode(node.getRightNode());
                node.setBuildingNum(x.getBuildingNum());
                node.setData(x.getData());
                node.setRightNode(deleteMin(node.getRightNode()));
            } else {
                node.setRightNode(deleteTheGivenBuildingNodeInSubtree(node.getRightNode(), buildingNum));
            }
        }
        return checkAndFixTreeAnomalies(node);
    }


    /**
     * Swap color of the child nodes for the given node
     *
     * @param node
     */
    private void swapColorsOfNodes(Node node) {
        if (node.getColor().equals(Color.RED)) {
            node.setColor(Color.BLACK);
        } else {
            node.setColor(Color.RED);
        }

        if (node.getLeftNode().getColor().equals(Color.RED)) {
            node.getLeftNode().setColor(Color.BLACK);
        } else {
            node.getLeftNode().setColor(Color.RED);
        }

        if (node.getRightNode().getColor().equals(Color.RED)) {
            node.getRightNode().setColor(Color.BLACK);
        } else {
            node.getRightNode().setColor(Color.RED);
        }

        node.setColor(node.getColor().equals(Color.RED) ? Color.BLACK : Color.RED);
        node.getLeftNode().setColor(node.getLeftNode().getColor().equals(Color.RED) ? Color.BLACK : Color.RED);
        node.getRightNode().setColor(node.getRightNode().getColor().equals(Color.RED) ? Color.BLACK : Color.RED);
    }

    /**
     * Move the red node to right. Suppose the node is red and right, right are black. then make right red or any of its
     * children red.
     *
     * @param node
     * @return
     */
    private Node moveRedRight(Node node) {
        swapColorsOfNodes(node);
        if (isRed(node.getLeftNode().getLeftNode())) {
            node = rotateRight(node);
            swapColorsOfNodes(node);
        }
        return node;
    }

    /**
     * Move the red node to left. Suppose the node is red and left, left are black. then make left red or any of its
     * children red.
     *
     * @param node
     * @return
     */
    private Node moveRedLeft(Node node) {
        swapColorsOfNodes(node);
        if (isRed(node.getRightNode().getLeftNode())) {
            node.setRightNode(rotateRight(node.getRightNode()));
            node = rotateLeft(node);
            swapColorsOfNodes(node);
        }
        return node;
    }


    /**
     * Rotate left for the input node.
     *
     * @param node
     * @return
     */
    private Node rotateLeft(Node node) {
        Node temp = node.getRightNode();
        node.setRightNode(temp.getLeftNode());

        temp.setLeftNode(node);
        temp.setColor(temp.getLeftNode().getColor());
        temp.getLeftNode().setColor(Color.RED);
        temp.setSize(node.getSize());

        int newsize = size(node.getLeftNode()) + size(node.getRightNode()) + 1;
        node.setSize(newsize);

        return temp;
    }

    /**
     * Rotate right for the input node.
     *
     * @param node
     * @return
     */
    private Node rotateRight(Node node) {
        Node temp = node.getLeftNode();
        node.setLeftNode(temp.getRightNode());
        temp.setRightNode(node);
        temp.setColor(temp.getRightNode().getColor());
        temp.getRightNode().setColor(Color.RED);
        temp.setSize(node.getSize());
        int newSize = size(node.getLeftNode()) + size(node.getRightNode()) + 1;
        node.setSize(newSize);
        return temp;
    }

    /**
     * Check and fix the tree anomalies.
     *
     * @param node
     * @return
     */
    private Node checkAndFixTreeAnomalies(Node node) {
        if (isRed(node.getRightNode())) {
            node = rotateLeft(node);
        }
        if (isRed(node.getLeftNode()) && isRed(node.getLeftNode().getLeftNode())) {
            node = rotateRight(node);
        }
        if (isRed(node.getLeftNode()) && isRed(node.getRightNode())) {
            swapColorsOfNodes(node);
        }

        int newSize = size(node.getLeftNode()) + size(node.getRightNode()) + 1;
        node.setSize(newSize);

        return node;
    }

    /**
     * Returns the smallest key in the subtree rooted at the given node
     *
     * @param node
     * @return
     */
    private Node getTreeMinNode(Node node) {
        if (isNull(node)) {
            throw new RuntimeException("Provided node is null");
        }
        if (isRedBlackTreeEmpty()) {
            throw new RuntimeException("Red black tree empty");
        }
        while (node.getLeftNode() != null) {
            node = node.getLeftNode();
        }
        return node;
    }

    /**
         * Get the building in range of left and right limit
     *
     * @param leftLimit
     * @param rightLimit
     * @return
     */
    void getBuildingsInRange(List<BuildingData> list, Integer leftLimit, Integer rightLimit) {
        if (isNull(leftLimit)) {
            throw new RuntimeException("leftLimit is null");
        }
        if (isNull(rightLimit)) {
            throw new RuntimeException("rightLimit is null");
        }
        getBuildingsInRange(root, list, leftLimit, rightLimit);
    }

    /**
     * Add the buildings in the list lying in the range
     *
     * @param node
     * @param list
     * @param leftLimit
     * @param rightLimit
     */
    private void getBuildingsInRange(Node node, List<BuildingData> list, Integer leftLimit, Integer rightLimit) {

        if (isNull(node)) {
            return;
        }


        // Lies to the left of limit, so check for its left node and its its right children.
        if (leftLimit < node.getBuildingNum()) {
            getBuildingsInRange(node.getLeftNode(), list, leftLimit, rightLimit);
        }

        // Lies in between the range, so add to the list.
        if (leftLimit <= node.getBuildingNum() && rightLimit >= node.getBuildingNum()) {
            list.add(node.getData());
        }

        // Lies to the right of the right limit, so check for its right node and its left children.
        if (rightLimit > node.getBuildingNum()) {
            getBuildingsInRange(node.getRightNode(), list, leftLimit, rightLimit);
        }
    }

    /**
     * Is the given node null.
     *
     * @param node
     * @return
     */
    private boolean isNull(Node node) {
        return node == null;
    }

    /**
     * Is the given integer null
     *
     * @param integer
     * @return
     */
    private boolean isNull(Integer integer) {
        return integer == null;
    }

    /**
     * Is the given node red
     *
     * @param node
     * @return
     */
    private boolean isRed(Node node) {
        return null != node && node.getColor() == Color.RED;
    }

    /**
     * Get the number of nodes in the subtrees of a given node.
     *
     * @param node
     * @return
     */
    private int size(Node node) {
        return isNull(node) ? 0 : node.getSize();
    }

    /**
     * Is the red black tree empty
     *
     * @return
     */
    private boolean isRedBlackTreeEmpty() {
        return isNull(root);
    }

    public static void main(String[] args) {
        RedBlackTree st = new RedBlackTree();
        st.addBuildingToTree(10, new BuildingData(10, 0, 0));
        st.addBuildingToTree(9, new BuildingData(9, 0, 0));
        st.addBuildingToTree(8, new BuildingData(8, 0, 0));
        st.addBuildingToTree(11, new BuildingData(11, 0, 0));
        st.addBuildingToTree(14, new BuildingData(14, 0, 0));
        st.addBuildingToTree(100, new BuildingData(100, 0, 0));
        st.addBuildingToTree(25, new BuildingData(25, 0, 0));
        st.addBuildingToTree(3, new BuildingData(3, 0, 0));
        System.out.println("Root : " + st.root.getBuildingNum() + " " + st.root.getColor());
        System.out.println("Root left: " + st.root.getLeftNode().getBuildingNum() + " " + st.root.getLeftNode().getColor());
        System.out.println("Root left left: " + st.root.getLeftNode().getLeftNode().getBuildingNum() + " " + st.root.getLeftNode().getLeftNode().getColor());
        System.out.println("Root left left left: " + st.root.getLeftNode().getLeftNode().getLeftNode().getBuildingNum() + " " + st.root.getLeftNode().getLeftNode().getLeftNode().getColor());
        System.out.println("Root left right: " + st.root.getLeftNode().getRightNode().getBuildingNum() + " " + st.root.getLeftNode().getRightNode().getColor());
        System.out.println("Root right: " + st.root.getRightNode().getBuildingNum() + " " + st.root.getRightNode().getColor());
        System.out.println("Root right right: " + st.root.getRightNode().getRightNode().getBuildingNum() + " " + st.root.getRightNode().getRightNode().getColor());
        System.out.println("Root right left: " + st.root.getRightNode().getLeftNode().getBuildingNum() + " " + st.root.getRightNode().getLeftNode().getColor());
        System.out.println();
        st.deleteTheGivenBuildingNum(25);
        System.out.println("Root : " + st.root.getBuildingNum() + " " + st.root.getColor());
        System.out.println("Root left: " + st.root.getLeftNode().getBuildingNum() + " " + st.root.getLeftNode().getColor());
        System.out.println("Root left left: " + st.root.getLeftNode().getLeftNode().getBuildingNum() + " " + st.root.getLeftNode().getLeftNode().getColor());
        System.out.println("Root left left left: " + st.root.getLeftNode().getLeftNode().getLeftNode().getBuildingNum() + " " + st.root.getLeftNode().getLeftNode().getLeftNode().getColor());
        System.out.println("Root left right: " + st.root.getLeftNode().getRightNode().getBuildingNum() + " " + st.root.getLeftNode().getRightNode().getColor());
        System.out.println("Root right: " + st.root.getRightNode().getBuildingNum() + " " + st.root.getRightNode().getColor());
        //System.out.println("Root right right: " + st.root.getRight().getRight().getBuildingNum() + " " + st.root.getRight().getRight().getColor());
        System.out.println("Root right left: " + st.root.getRightNode().getLeftNode().getBuildingNum() + " " + st.root.getRightNode().getLeftNode().getColor());

    }
}

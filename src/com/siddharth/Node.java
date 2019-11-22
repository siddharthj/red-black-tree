package com.siddharth;

public class Node {
    public Integer getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(Integer buildingNum) {
        this.buildingNum = buildingNum;
    }

    public BuildingData getData() {
        return data;
    }

    public void setData(BuildingData data) {
        this.data = data;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private Integer buildingNum;
    private BuildingData data;
    private Node left, right;
    private Color color;
    private Integer size;

    public Node(Integer buildingNum, BuildingData data, Color color, Integer size) {
        this.buildingNum = buildingNum;
        this.data = data;
        this.color = color;
        this.size = size;
    }
}
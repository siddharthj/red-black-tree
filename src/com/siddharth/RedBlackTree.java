package com.siddharth;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.siddharth.Color.BLACK;
import static com.siddharth.Color.RED;

public class RedBlackTree {


    private Node root;
    /**
     * Initializes an empty symbol table.
     */
    public RedBlackTree() {
    }

    private boolean isRed(Node node) {
        if (null == node) {
            return false;
        }
        return node.getColor() == RED;
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.getSize();
    }

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public Optional<BuildingData> get(Integer buildingNum) {
        if (buildingNum == null) throw new IllegalArgumentException("argument to get() is null");
        return get(root, buildingNum);
    }

    private Optional<BuildingData> get(Node node, Integer buildingNum) {
        while (node != null) {
            if (buildingNum < node.getBuildingNum()) {
                node = node.getLeft();
            } else if (buildingNum > node.getBuildingNum()) {
                node = node.getRight();
            } else {
                return Optional.of(node.getData());
            }
        }
        return Optional.empty();
    }

    public boolean contains(Integer key) {
        return get(key) != null;
    }

    public void put(Integer buildingNum, BuildingData value) {
        if (buildingNum == null) throw new IllegalArgumentException("first argument to put() is null");
        if (value == null) {
            delete(buildingNum);
            return;
        }

        root = put(root, buildingNum, value);
        root.setColor(BLACK);
        // assert check();
    }

    private Node put(Node node, Integer buildingNum, BuildingData value) {
        if (node == null) return new Node(buildingNum, value, RED, 1);

        int cmp = buildingNum.compareTo(node.getBuildingNum());
        if      (cmp < 0) node.setLeft(put(node.getLeft(),  buildingNum, value));
        else if (cmp > 0) node.setRight(put(node.getRight(), buildingNum, value));
        else              node.setData(value);

        // fix-up any right-leaning links
        if (isRed(node.getRight()) && !isRed(node.getLeft()))      node = rotateLeft(node);
        if (isRed(node.getLeft())  &&  isRed(node.getLeft().getLeft())) node = rotateRight(node);
        if (isRed(node.getLeft())  &&  isRed(node.getRight()))     flipColors(node);
        node.setSize(size(node.getLeft()) + size(node.getRight()) + 1);

        return node;
    }

    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.getLeft()) && !isRed(root.getRight()))
            root.setColor(RED);

        root = deleteMin(root);
        if (!isEmpty()) root.setColor(BLACK);
        // assert check();
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node node) {
        if (node.getLeft() == null)
            return null;

        if (!isRed(node.getLeft()) && !isRed(node.getLeft().getLeft()))
            node = moveRedLeft(node);

        node.setLeft(deleteMin(node.getLeft()));
        return balance(node);
    }

    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.getLeft()) && !isRed(root.getRight()))
            root.setColor(RED);

        root = deleteMax(root);
        if (!isEmpty()) root.setColor(BLACK);
        // assert check();
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node node) {
        if (isRed(node.getLeft()))
            node = rotateRight(node);

        if (node.getRight() == null)
            return null;

        if (!isRed(node.getRight()) && !isRed(node.getRight().getLeft()))
            node = moveRedRight(node);

        node.setRight(deleteMax(node.getRight()));

        return balance(node);
    }

    public void delete(Integer buildingNum) {
        if (buildingNum == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(buildingNum)) return;

        // if both children of root are black, set root to red
        if (!isRed(root.getLeft()) && !isRed(root.getRight()))
            root.setColor(RED);

        root = delete(root, buildingNum);
        if (!isEmpty()) root.setColor(BLACK);
        // assert check();
    }

    // delete the key-value pair with the given key rooted at h
    private Node delete(Node h, Integer buildingNum) {
        // assert get(h, key) != null;

        if (buildingNum.compareTo(h.getBuildingNum()) < 0)  {
            if (!isRed(h.getLeft()) && !isRed(h.getLeft().getLeft()))
                h = moveRedLeft(h);
            h.setLeft(delete(h.getLeft(), buildingNum));
        }
        else {
            if (isRed(h.getLeft()))
                h = rotateRight(h);
            if (buildingNum.compareTo(h.getBuildingNum()) == 0 && (h.getRight() == null))
                return null;
            if (!isRed(h.getRight()) && !isRed(h.getRight().getLeft()))
                h = moveRedRight(h);
            if (buildingNum.compareTo(h.getBuildingNum()) == 0) {
                Node x = min(h.getRight());
                h.setBuildingNum(x.getBuildingNum());
                h.setData(x.getData());
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.setRight(deleteMin(h.getRight()));
            }
            else h.setRight(delete(h.getRight(), buildingNum));
        }
        return balance(h);
    }

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.getLeft();
        h.setLeft(x.getRight());
        x.setRight(h);
        x.setColor(x.getRight().getColor());
        x.getRight().setColor(RED);
        x.setSize(h.getSize());
        h.setSize(size(h.getLeft()) + size(h.getRight()) + 1);
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.getRight();
        h.setRight(x.getLeft());
        x.setLeft(h);
        x.setColor(x.getLeft().getColor());
        x.getLeft().setColor(RED);
        x.setSize(h.getSize());
        h.setSize(size(h.getLeft()) + size(h.getRight()) + 1);
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.setColor(h.getColor().equals(RED) ? BLACK : RED);
        h.getLeft().setColor(h.getLeft().getColor().equals(RED) ? BLACK : RED);
        h.getRight().setColor(h.getRight().getColor().equals(RED) ? BLACK : RED);
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.getRight().getLeft())) {
            h.setRight(rotateRight(h.getRight()));
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.getLeft().getLeft())) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        // assert (h != null);

        if (isRed(h.getRight()))                      h = rotateLeft(h);
        if (isRed(h.getLeft()) && isRed(h.getLeft().getLeft())) h = rotateRight(h);
        if (isRed(h.getLeft()) && isRed(h.getRight()))     flipColors(h);

        h.setSize(size(h.getLeft()) + size(h.getRight()) + 1);
        return h;
    }


    public int height() {
        return height(root);
    }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.getLeft()), height(x.getRight()));
    }

    public Integer min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).getBuildingNum();
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) {
        // assert x != null;
        if (x.getLeft() == null) return x;
        else                return min(x.getLeft());
    }


    public Integer max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).getBuildingNum();
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) {
        // assert x != null;
        if (x.getRight() == null) return x;
        else                 return max(x.getRight());
    }

    public Integer floor(Integer key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node x = floor(root, key);
        if (x == null) return null;
        else           return x.getBuildingNum();
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node x, Integer key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.getBuildingNum());
        if (cmp == 0) return x;
        if (cmp < 0)  return floor(x.getLeft(), key);
        Node t = floor(x.getRight(), key);
        if (t != null) return t;
        else           return x;
    }

    public Integer ceiling(Integer key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node x = ceiling(root, key);
        if (x == null) return null;
        else           return x.getBuildingNum();
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node x, Integer key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.getBuildingNum());
        if (cmp == 0) return x;
        if (cmp > 0)  return ceiling(x.getRight(), key);
        Node t = ceiling(x.getLeft(), key);
        if (t != null) return t;
        else           return x;
    }

    public Integer select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + k);
        }
        Node x = select(root, k);
        return x.getBuildingNum();
    }

    // the key of rank k in the subtree rooted at x
    private Node select(Node x, int k) {
        // assert x != null;
        // assert k >= 0 && k < size(x);
        int t = size(x.getLeft());
        if      (t > k) return select(x.getLeft(),  k);
        else if (t < k) return select(x.getRight(), k-t-1);
        else            return x;
    }

    public int rank(Integer key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(Integer key, Node x) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.getBuildingNum());
        if      (cmp < 0) return rank(key, x.getLeft());
        else if (cmp > 0) return 1 + size(x.getLeft()) + rank(key, x.getRight());
        else              return size(x.getLeft());
    }

    public Iterable<Integer> keys() {
        if (isEmpty()) return new PriorityQueue<>();
        return keys(min(), max()).stream().map(BuildingData::getBuildingNum).collect(Collectors.toList());
    }

    public List<BuildingData> keys(Integer lo, Integer hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        List<BuildingData> list = new ArrayList<>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, list, lo, hi);
        return list;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the list
    private void keys(Node x, List<BuildingData> list, Integer lo, Integer hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.getBuildingNum());
        int cmphi = hi.compareTo(x.getBuildingNum());
        if (cmplo < 0) keys(x.getLeft(), list, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) list.add(x.getData());
        if (cmphi > 0) keys(x.getRight(), list, lo, hi);
    }

    public int size(Integer lo, Integer hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }

    private boolean check() {
        if (!isBST())            System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        if (!is23())             System.out.println("Not a 2-3 tree");
        if (!isBalanced())       System.out.println("Not balanced");
        return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Integer min, Integer max) {
        if (x == null) return true;
        if (min != null && x.getBuildingNum().compareTo(min) <= 0) return false;
        if (max != null && x.getBuildingNum().compareTo(max) >= 0) return false;
        return isBST(x.getLeft(), min, x.getBuildingNum()) && isBST(x.getRight(), x.getBuildingNum(), max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.getSize() != size(x.getLeft()) + size(x.getRight()) + 1) return false;
        return isSizeConsistent(x.getLeft()) && isSizeConsistent(x.getRight());
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Integer key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
        if (x == null) return true;
        if (isRed(x.getRight())) return false;
        if (x != root && isRed(x) && isRed(x.getLeft()))
            return false;
        return is23(x.getLeft()) && is23(x.getRight());
    }

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() {
        int black = 0;     // number of black links on path from root to min
        Node x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.getLeft();
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.getLeft(), black) && isBalanced(x.getRight(), black);
    }


    /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        RedBlackTree st = new RedBlackTree();
        for (int i = 0; i<10; i++) {
            BuildingData buildingData = new BuildingData(i, 10, 10);
            st.put(i, buildingData);
        }
        System.out.println(st.root.getBuildingNum() + " " + st.root.getColor());
        for (Integer s : st.keys())
            System.out.println(s + ", " + st.get(s));
        System.out.println();
    }
}

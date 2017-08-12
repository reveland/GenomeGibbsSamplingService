package com.genome.comparer.datastructures;

import java.util.ArrayList;
import java.util.List;

import com.genome.comparer.algorithm.Tree;
import com.genome.comparer.algorithm.TreeNode;

/**
 * I think this class purpose to:
 *
 * Make a Tree object from Newick string
 */
public class Newick {

    Tree tree_;

    public Tree build(String newickStr) {
        tree_ = new Tree();
        if ((newickStr.charAt(0) != '(') || !(newickStr.contains(")"))) {
            throw new RuntimeException("newickStr: " + newickStr + " does not contain ( or )");
        } else {
            String[] split = processExtParenthesis(newickStr);
            String info = split[1];
            String childrenStr = split[0];
            TreeNode root = new TreeNode();
            tree_.root = root;
            String[] infoSplit = info.split(":");
            root.name = infoSplit[0];
            if (infoSplit.length == 2) {
                root.evolDist = Double.parseDouble(infoSplit[1]);
            }
            root.owner = tree_;
            parse(childrenStr, root);
        }
        return tree_;
    }

    private void parse(String treeStr, TreeNode currentRoot) {
        List<String> children = processComma(treeStr);

        String leftChildStr = children.get(0);
        processChild(currentRoot, leftChildStr, true);
        if (children.size() == 2) {
            String rightChildStr = children.get(1);
            processChild(currentRoot, rightChildStr, false);
        }
    }

    private void processChild(TreeNode parent, String childStr, boolean isLeft) {
        TreeNode node = new TreeNode();
        node.owner = tree_;
        node.parent = parent;
        if (isLeft) {
            parent.leftChild = node;
        } else {
            parent.rightChild = node;
        }

        if (childStr.contains("(") && childStr.contains(")")) {
            String[] split = processExtParenthesis(childStr);
            String info = split[1];
            String newChildrenStr = split[0];

            String[] infoSplit = info.split(":");
            node.name = infoSplit[0];
            if (infoSplit.length == 2) {
                node.evolDist = Double.parseDouble(infoSplit[1]);
            }
            parse(newChildrenStr, node);
        } else {
            String[] infoSplit = childStr.split(":");
            node.name = infoSplit[0];
            if (infoSplit.length == 2) {
                node.evolDist = Double.parseDouble(infoSplit[1]);
            }
        }
    }

    private String[] processExtParenthesis(String str) {
        String[] result = new String[2];

        int firstOpenParIndex = str.indexOf("(");
        int lastCloseParIndex = str.lastIndexOf(")");
        result[0] = str.substring(firstOpenParIndex + 1, lastCloseParIndex);
        result[1] = str.substring(lastCloseParIndex + 1, str.length());
        return result;
    }

    private List<String> processComma(String str) {
        List<String> result = new ArrayList<>();
        int indexOfComma = -1;
        int level = 0;
        char[] characters = str.toCharArray();
        for (int i = 0; i < characters.length; ++i) {
            if (characters[i] == '(')
                level++;
            if (characters[i] == ')')
                level--;
            if (characters[i] == ',' && level == 0) {
                indexOfComma = i;
                break;
            }
        }
        if (indexOfComma == -1) {
            result.add(str);
        } else {
            result.add(str.substring(0, indexOfComma));
            result.add(str.substring(indexOfComma + 1, str.length()));
        }
        return result;
    }

}

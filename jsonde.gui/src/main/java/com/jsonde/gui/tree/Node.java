package com.jsonde.gui.tree;

import com.jsonde.client.domain.MethodCall;

import javax.swing.tree.DefaultMutableTreeNode;

public class Node extends DefaultMutableTreeNode {

    private String value;
    private MethodCall rootMethodCall;

    public Node() {
    }

    public Node(String value) {
        this.value = value;
    }

    public Node(String value, MethodCall rootMethodCall) {
        this.value = value;
        this.rootMethodCall = rootMethodCall;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MethodCall getRootMethodCall() {
        return rootMethodCall;
    }

    public void setRootMethodCall(MethodCall rootMethodCall) {
        this.rootMethodCall = rootMethodCall;
    }

    @Override
    public String toString() {
        return value;
    }

}
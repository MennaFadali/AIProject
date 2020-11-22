package code.generic;

import java.util.ArrayList;

public interface Problem {


    abstract ArrayList<Operator> getOperators();

    abstract State getInitState();

    abstract ArrayList<State> getStateSpace();

    abstract boolean isGoal(State state);

    // Any path could be represented by the start node and the end node of the path
    abstract int pathCost(Node startNode, Node endNode);

    abstract int getStateVal(State s);

    abstract void assignStateVal(State state, int val);

    abstract int getExpandedNodes();

    abstract void incrementExpandedNodes();
    
    abstract void clearMemo();
}

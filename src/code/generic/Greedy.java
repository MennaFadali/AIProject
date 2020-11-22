package code.generic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Greedy implements QngFn {

    HeuristicFunction hn;
    int expandedNodes;
    Problem problem;

    public Greedy(Problem problem, HeuristicFunction hn) {
    	this.problem = problem;
    	this.hn = hn;
    }


    @Override
    public PriorityQueue<Node> makeQueue(Node initState) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>(new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return hn.getHn(o1) - hn.getHn(o2);
            }
        });
        problem.assignStateVal(initState.getState(), hn.getHn(initState));
        pq.add(initState);
        return pq;
    }


    @Override
    public Node removeFront(PriorityQueue<Node> pq) {
        expandedNodes++;
        Node top = pq.remove();
        while (problem.getStateVal(top.getState()) < hn.getHn(top)) {
        	top = pq.remove();
        }
        return top;
    }

    @Override
    public void enqueue(ArrayList<Node> nodes, PriorityQueue<Node> pq) {
        for (Node node : nodes) {
            State state = node.getState();
            if (node.depth >= 600) continue;
            int assignedValue = problem.getStateVal(state);
            int currentValue = hn.getHn(node);
            if (assignedValue == -1 || currentValue < assignedValue) {
                problem.assignStateVal(state, currentValue);
                pq.add(node);
            }
        }
    }


    public int getExpandedNodes() {
        return expandedNodes;
    }


}

package code.generic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class UCS implements QngFn {

    Problem problem;

    public UCS(Problem problem) {
        this.problem = problem;
    }


    @Override
    public PriorityQueue<Node> makeQueue(Node initState) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>(new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return Long.compare(o1.cost, o2.cost);
            }
        });
        problem.assignStateVal(initState.getState(), initState.cost);
        pq.add(initState);
        return pq;
    }

    @Override
    public Node removeFront(PriorityQueue<Node> pq) {
        problem.incrementExpandedNodes();
        Node top = pq.remove();
        while (problem.getStateVal(top.getState()) < problem.pathCost(top, top))
            top = pq.remove();
        return top;
    }

    @Override
    public void enqueue(ArrayList<Node> nodes, PriorityQueue<Node> pq) {
        for (Node node : nodes) {
            State state = node.getState();
            if (node.depth >= 600) continue;
            int assignedValue = problem.getStateVal(state);
            int currentValue = problem.pathCost(node, node);
            if (assignedValue == -1 || currentValue < assignedValue) {
                problem.assignStateVal(state, currentValue);
                pq.add(node);
            }
        }
    }
}

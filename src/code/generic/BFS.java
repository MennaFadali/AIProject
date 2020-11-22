package code.generic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BFS implements QngFn {

    Problem problem;

    public BFS(Problem problem) {
        this.problem = problem;
    }

    @Override
    public PriorityQueue<Node> makeQueue(Node initState) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.depth - o2.depth;
            }
        });
        problem.assignStateVal(initState.getState(), 0);
        pq.add(initState);
        return pq;
    }

    @Override
    public Node removeFront(PriorityQueue<Node> pq) {
        problem.incrementExpandedNodes();
        return pq.remove();
    }

    @Override
    public void enqueue(ArrayList<Node> nodes, PriorityQueue<Node> pq) {
        for (Node node : nodes) {
            State state = node.getState();
            if (problem.getStateVal(state) == -1) {
                problem.assignStateVal(state, 0);
                pq.add(node);
            }
        }

    }

}

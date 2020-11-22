package code.generic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class IDS implements QngFn {

    Problem problem;
    int threshold;
    Node dummyNode;
    Node initState;

    public IDS(Problem problem) {
        this.problem = problem;
    }


    @Override
    public PriorityQueue<Node> makeQueue(Node initState) {
    	this.initState = initState;
        PriorityQueue<Node> pq = new PriorityQueue<Node>(new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return o2.depth - o1.depth;
            }
        });
        threshold = 0;
        problem.assignStateVal(initState.getState(), 0);
        dummyNode = new Node();
        pq.add(dummyNode);
        pq.add(initState);
        return pq;
    }

    @Override
    public Node removeFront(PriorityQueue<Node> pq) {
        problem.incrementExpandedNodes();
        Node n = pq.remove();
        if (n == dummyNode) {
        	threshold++;
        	problem.clearMemo();
        	pq.add(dummyNode);
            problem.assignStateVal(initState.getState(), 0);
        	return initState;
        }
        return n;
    }

    @Override
    public void enqueue(ArrayList<Node> nodes, PriorityQueue<Node> pq) {
        for (Node node : nodes) {
            State state = node.getState();
            if (node.depth <= threshold && problem.getStateVal(state) == -1) {
                problem.assignStateVal(state, 0);
                pq.add(node);
            }
        }

    }


}

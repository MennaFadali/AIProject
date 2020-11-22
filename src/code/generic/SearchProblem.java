package code.generic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class SearchProblem {
// This is an abstract class for any search problem and search strategy

    public static Node generalSearch(Problem problem, QngFn fn) {
        PriorityQueue<Node> nodes = fn.makeQueue(new Node(problem.getInitState()));

        while (!nodes.isEmpty()) {
            Node cur = fn.removeFront(nodes);
            if (problem.isGoal(cur.getState()))
                return cur;
            fn.enqueue(expand(cur, problem.getOperators()), nodes);
        }
        return null;
    }

    public static Node bestFirstSearch(Problem problem, EvalFunction evalFunction) {
        QngFn qngfn = new QngFn() {

            @Override
            public PriorityQueue<Node> makeQueue(Node initState) {
                PriorityQueue<Node> pq = new PriorityQueue<Node>(new Comparator<Node>() {

                    @Override
                    public int compare(Node o1, Node o2) {
                        return evalFunction.getFn(o1) - evalFunction.getFn(o2);
                    }
                });
                problem.assignStateVal(initState.getState(), evalFunction.getFn(initState));
                pq.add(initState);
                return pq;
            }


            @Override
            public Node removeFront(PriorityQueue<Node> pq) {
                problem.incrementExpandedNodes();
                Node top = pq.remove();
                while (problem.getStateVal(top.getState()) < evalFunction.getFn(top))
                    top = pq.remove();
                return top;
            }

            @Override
            public void enqueue(ArrayList<Node> nodes, PriorityQueue<Node> pq) {
                for (Node node : nodes) {
                    State state = node.getState();
                    if (node.depth >= 600) continue;
                    int assignedValue = problem.getStateVal(state);
                    int currentValue = evalFunction.getFn(node);
                    if (assignedValue == -1 || currentValue < assignedValue) {
                        problem.assignStateVal(state, currentValue);
                        pq.add(node);
                    }
                }
            }
        };
        return generalSearch(problem, qngfn);
    }

    public static ArrayList<Node> expand(Node node, ArrayList<Operator> operators) {
        ArrayList<Node> next = new ArrayList<Node>();
        for (Operator op : operators) {
            Node n = op.operate(node);
            if (n != null)
                next.add(n);
        }
        return next;
    }

}

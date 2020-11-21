package code.generic;

import java.util.ArrayList;
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

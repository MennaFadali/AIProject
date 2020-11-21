package code.generic;
import java.util.ArrayList;

public class Node {


	State state;
	Node parent;
	Operator op;
	int depth;
	int cost;
	
	
	public Node() {
		
	}
	
	
	public Node(State state) {
		//makes initial node
		 this.state = state;
		 cost = 0;
		 depth = 0;
	}

	public Node(State state, Node parent, Operator op, int depth, int cost) {
		super();
		this.state = state;
		this.parent = parent;
		this.op = op;
		this.depth = depth;
		this.cost = cost;
	}
	
	public State getState () {
		return state;
	}


	public Node getParent() {
		return parent;
	}


	public void setParent(Node parent) {
		this.parent = parent;
	}


	public Operator getOp() {
		return op;
	}


	public void setOp(Operator op) {
		this.op = op;
	}


	public int getDepth() {
		return depth;
	}


	public void setDepth(int depth) {
		this.depth = depth;
	}


	public int getCost() {
		return cost;
	}


	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Node{" +
				"state=" + state +
				", parent=" + parent +
				", op=" + op+
				", depth=" + depth +
				", cost=" + cost +
				'}';
	}
}

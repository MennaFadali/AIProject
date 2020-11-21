package code.generic;
import java.util.ArrayList;

public interface Problem {
	
	
	abstract ArrayList<Operator> getOperators(); 

	abstract State getInitState ();

	abstract ArrayList<State> getStateSpace ();
	
	abstract boolean isGoal (State state);

//	abstract boolean pathCost (ArrayList<Node> path);

	abstract long pathCost(Node lastOnPath);
	
	abstract int getStateVal(State s);

	abstract void assignStateVal(State state , int val);
}

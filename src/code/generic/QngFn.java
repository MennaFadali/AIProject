package code.generic;
import java.util.ArrayList;
import java.util.PriorityQueue;

public interface QngFn {
	
	
	public PriorityQueue<Node> makeQueue(Node initState);
	
	public Node removeFront (PriorityQueue<Node> pq);
	
	public void enqueue(ArrayList<Node> nodes , PriorityQueue<Node> pq);
}

package code.mission;

import java.util.Arrays;
import java.util.Stack;

import code.generic.BFS;
import code.generic.DFS;
import code.generic.Greedy;
import code.generic.Node;
import code.generic.SearchProblem;
import code.generic.UCS;

public class MissionImpossible extends SearchProblem {

	public Grid genGrid() {
		int n, m, c, k, sx, sy, ex, ey;
		n = getRandom(5, 15);
		m = getRandom(5, 15);
		k = getRandom(5, 10);
		c = getRandom(1, k);
		boolean[][] taken = new boolean[n][m];
		int[] e = getRandomPos(taken);
		ex = e[0];
		ey = e[1];
		taken[ex][ey] = true;
		int[] s = getRandomPos(taken);
		sx = s[0];
		sy = s[1];
		taken[sx][sy] = true;
		int[] x = new int[k], y = new int[k], h = new int[k];
		for (int i = 0; i < k; i++) {
			int[] tmp = getRandomPos(taken);
			x[i] = tmp[0];
			y[i] = tmp[1];
			h[i] = getRandom(1, 99);
			taken[x[i]][y[i]] = true;
		}
		return new Grid(c, k, n, m, ex, ey, sx, sy, x, y, h);
	}

	public static int[] getRandomPos(boolean[][] taken) {
		int x = getRandom(0, taken.length), y = getRandom(0, taken[0].length);
		while (taken[x][y])
			x = getRandom(0, taken.length);
		y = getRandom(0, taken[0].length);
		return new int[] { x, y };
	}

	public static int getRandom(int from, int to) {
		return from + (int) (Math.random() * (to + 1));
	}

	public static String solve(String grid, String strategy, boolean visualize) {
		Grid g = Grid.unparse(grid);
//		System.err.println(Arrays.toString(g.x));
//		System.err.println(Arrays.toString(g.y));
		MissionImpossibleProblem prob = new MissionImpossibleProblem(g);
		if (strategy.equals("BF")) {
			BFS bfs = new BFS(prob);
			Node goal = generalSearch(prob, bfs);
//			System.err.println(goal);
//			System.err.println(tracePath(goal, g, bfs.getExpandedNodes()));
			return tracePath(goal, g, bfs.getExpandedNodes());
		}
		if (strategy.equals("DF")) {
			DFS dfs = new DFS(prob);
			Node goal = generalSearch(prob, dfs);
			return tracePath(goal, g, dfs.getExpandedNodes());
		}
		if (strategy.equals("UC")) {
			UCS ucs = new UCS(prob);
			Node goal = generalSearch(prob, ucs);
			return tracePath(goal, g, ucs.getExpandedNodes());
		}
		if (strategy.equals("GR1")) {
			Greedy gr = new Greedy(prob, new H1(g.sx, g.sy));
			Node goal = generalSearch(prob, gr);
			return tracePath(goal, g, gr.getExpandedNodes());
		}
		if (strategy.equals("GR2")) {
			Greedy gr = new Greedy(prob, new H2(g));
			Node goal = generalSearch(prob, gr);
			return tracePath(goal, g, gr.getExpandedNodes());
		}
		return "INVALID INPUT";
	}

	public static MissionImpossibleState getMissionImpossibleState(Node node) {
		return (MissionImpossibleState) node.getState();
	}

	public static String tracePath(Node goal, Grid grid, int expandedNodes) {
		StringBuilder ans = new StringBuilder();
		Node cur = goal;
		Stack<String> plan = new Stack<String>();
		int k = grid.k;
		int[] finalHealth = new int[k];
		int dead = getMissionImpossibleState(goal).dead;
		while (cur.getParent() != null) {
			plan.add(cur.getOp().getName());
//			plan.add(cur.getOp().getName() + " " + Integer.toBinaryString(getMissionImpossibleState(cur).safe));
			MissionImpossibleState curState = getMissionImpossibleState(cur),
					parentState = getMissionImpossibleState(cur.getParent());
			int pickUp = getPickedUp(parentState.safe, curState.safe, k);
			if (pickUp != -1)
				finalHealth[pickUp] = Math.min(100, grid.getOriginalHealth(pickUp) + 2 * (curState.time - 1));
			cur = cur.getParent();
		}
		ans.append(plan.pop());
		while (!plan.isEmpty())
			ans.append("," + plan.pop());
		ans.append(";" + dead + ";" + finalHealth[0]);
		for (int i = 1; i < k; i++)
			ans.append("," + finalHealth[i]);
		ans.append(";" + expandedNodes);
		return ans.toString();
	}

	static int getPickedUp(int parentSafe, int curSafe, int k) {
		for (int i = 0; i < k; i++) {
			if ((curSafe & 1 << i) != 0 && (parentSafe & 1 << i) == 0)
				return i;
		}
		return -1;
	}
}

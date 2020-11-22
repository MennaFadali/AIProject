package code.mission;

import java.util.Stack;

import code.generic.BFS;
import code.generic.DFS;
import code.generic.EvalFunction;
import code.generic.IDS;
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
        return new int[]{x, y};
    }

    public static int getRandom(int from, int to) {
        return from + (int) (Math.random() * (to + 1));
    }

    public static String solve(String grid, String strategy, boolean visualize) {
        Grid g = Grid.unparse(grid);
        MissionImpossibleProblem prob = new MissionImpossibleProblem(g);
        Node goal = null;

        if (strategy.equals("BF"))
            goal = generalSearch(prob, new BFS(prob));

        if (strategy.equals("DF"))
            goal = generalSearch(prob, new DFS(prob));

        if (strategy.equals("UC"))
            goal = generalSearch(prob, new UCS(prob));

        if (strategy.equals("ID"))
            goal = generalSearch(prob, new IDS(prob));

        if (strategy.equals("GR1")) {
            H1 h1 = new H1(prob.grid.sx, prob.grid.sy);
            EvalFunction evalFunction = node -> h1.getHn(node);
            goal = bestFirstSearch(prob, evalFunction);
        }
        if (strategy.equals("GR2")) {
            H2 h2 = new H2(prob.grid);
            EvalFunction evalFunction = node -> h2.getHn(node);
            goal = bestFirstSearch(prob, evalFunction);
        }

        if (strategy.equals("AS1")) {
            H1 h1 = new H1(prob.grid.sx, prob.grid.sy);
            EvalFunction evalFunction = node -> h1.getHn(node) + prob.pathCost(node, node);
            goal = bestFirstSearch(prob, evalFunction);
        }
        if (strategy.equals("AS2")) {
            H2 h2 = new H2(prob.grid);
            EvalFunction evalFunction = node -> h2.getHn(node) + prob.pathCost(node, node);
            goal = bestFirstSearch(prob, evalFunction);
        }

        if (visualize) System.out.println(visualizeSolution(goal, g));

        return goal == null ? "INVALID INPUT" : tracePath(goal, g, prob.getExpandedNodes());
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

    public static String visualizeSolution(Node goal, Grid grid) {
        if (goal == null) return "NO DISCOVERED SOLUTION";
        StringBuilder ans = new StringBuilder();
        Node cur = goal;
        Stack<String> simulation = new Stack<String>();
        int k = grid.k;
        int[] finalHealth = new int[k];
        while (cur.getParent() != null) {
            String name = cur.getOp().getName();
            MissionImpossibleState curState = getMissionImpossibleState(cur),
                    parentState = getMissionImpossibleState(cur.getParent());
            if (name.equals("carry")) {
                int pickUp = getPickedUp(parentState.safe, curState.safe, k);
                if (pickUp != -1) {
                    finalHealth[pickUp] = Math.min(100, grid.getOriginalHealth(pickUp) + 2 * (curState.time - 1));
                    if (finalHealth[pickUp] == 100)
                        simulation.add("Ethan carries IMF #" + (pickUp + 1) + " which is dead by now.");
                    else
                        simulation.add("Ethan carries IMF #" + (pickUp + 1) + " and saves him while his health is " + finalHealth[pickUp]);
                }
            } else if (name.equals("drop")) {
                simulation.add("Ethan drops all IMFs into the submarine at cell " + curState.x + " " + curState.y);
            } else simulation.add("Ethan moves " + name + " to cell " + curState.x + " " + curState.y);
            cur = cur.getParent();
        }
        simulation.add("Ethan starts at cell " + grid.ex + " " + grid.ey);
        while (!simulation.isEmpty())
            ans.append(simulation.pop() + "\n");
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

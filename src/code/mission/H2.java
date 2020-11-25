package code.mission;

import code.generic.HeuristicFunction;
import code.generic.Node;

// H2 is the second heuristic function.
// It evaluates the maximum(distance between all unsafe IMFs and the submarine)
public class H2 implements HeuristicFunction {

    Grid grid;


    public H2(Grid grid) {
        this.grid = grid;
    }

    @Override
    public int getHn(Node node) {
        MissionImpossibleState state = (MissionImpossibleState) node.getState();
        int cx = state.x, cy = state.y;
        int ans = getManhattanDistanceToSubmarine(cx, cy);
        int safe = state.safe;
        for (int i = 0; i < grid.k; i++)
            if ((safe & 1 << i) == 0) ans = Math.max(ans, getManhattanDistanceToSubmarine(grid.x[i], grid.y[i]));
        return ans;
    }

    public int getManhattanDistanceToSubmarine(int x, int y) {
        return Math.abs(x - grid.sx) + Math.abs(y - grid.sy);
    }

}

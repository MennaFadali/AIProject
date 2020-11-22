package code.mission;

import code.generic.HeuristicFunction;
import code.generic.Node;

// H1 is the first heuristic function.
// It evaluates based on the remaining IMFs to be picked then on the Manhattan distance
public class H1 implements HeuristicFunction {

    int sx, sy;

    public H1(int sx, int sy) {
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public int getHn(Node node) {
        MissionImpossibleState state = (MissionImpossibleState) node.getState();
        int cx = state.x, cy = state.y;
        int safeIMFs = Integer.bitCount(state.safe);
        int manhattandistance = Math.abs(cx - sx) + Math.abs(cy - sy);
        return -(safeIMFs * 100 + manhattandistance);
    }
}

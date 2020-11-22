package code.mission;

import code.generic.HeuristicFunction;
import code.generic.Node;

// H1 is the first heuristic function.
// It calculates the Manhattan Distance between the current position and the submarine's position
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
        return Math.abs(cx - sx) + Math.abs(cy - sy);
    }
}

package code.mission;

import java.util.ArrayList;
import java.util.Arrays;

import code.generic.Node;
import code.generic.Operator;
import code.generic.Problem;
import code.generic.State;

public class MissionImpossibleProblem implements Problem {

    static final short UNCALC = -1;
    Grid grid;
    short[][][][][] memo;
    int n, m;
    ArrayList<Operator> operators;

    public MissionImpossibleProblem(Grid grid) {
        this.grid = grid;
        this.n = grid.n;
        this.m = grid.m;
        memo = new short[grid.c + 1][grid.n][grid.m][600][1 << grid.k];
        for (short[][][][] mmmm : memo)
            for (short[][][] mmm : mmmm)
                for (short[][] mm : mmm)
                    for (short[] mc : mm)
                        Arrays.fill(mc, UNCALC);
    }

    @Override
    public ArrayList<Operator> getOperators() {
        operators = new ArrayList<Operator>();
        // left
        operators.add(new Operator() {

            @Override
            public Node operate(Node n) {
                return getNextNodeDirOp(0, -1, n, 0);
            }

            @Override
            public String getName() {
                return "left";
            }

        });
        // right
        operators.add(new Operator() {

            @Override
            public Node operate(Node n) {
                return getNextNodeDirOp(0, 1, n, 1);
            }

            @Override
            public String getName() {
                return "right";
            }
        });
        // up
        operators.add(new Operator() {

            @Override
            public Node operate(Node n) {
                return getNextNodeDirOp(-1, 0, n, 2);
            }

            @Override
            public String getName() {
                return "up";
            }
        });
        // down
        operators.add(new Operator() {

            @Override
            public Node operate(Node n) {
                return getNextNodeDirOp(1, 0, n, 3);
            }

            @Override
            public String getName() {
                return "down";
            }
        });
        // carry
        operators.add(new Operator() {

            @Override
            public Node operate(Node n) {
                MissionImpossibleState mState = (MissionImpossibleState) n.getState();
                MissionImpossibleState newState = new MissionImpossibleState();
                newState.x = mState.x;
                newState.y = mState.y;
                newState.carrying = mState.carrying;
                newState.safe = mState.safe;
                newState.time = mState.time + 1;
                if (!(containsIMF(mState.x, mState.y, mState.safe) && mState.carrying < grid.c))
                    return null;
                newState.carrying++;
                newState.safe = saveIMF(newState.x, newState.y, mState.safe);
                newState.totalDamage = evalDamage(mState.totalDamage, newState.safe, newState.time);
                newState.dead = evalDead(mState.dead, newState.safe, newState.time);
                Node newNode = new Node(newState, n, operators.get(4), evalDepth(n.getDepth()), evalCost(newState));
                return newNode;
            }

            @Override
            public String getName() {
                return "carry";
            }
        });
        // drop
        operators.add(new Operator() {

            @Override
            public Node operate(Node n) {
                MissionImpossibleState mState = (MissionImpossibleState) n.getState();
                MissionImpossibleState newState = new MissionImpossibleState();
                newState.x = mState.x;
                newState.y = mState.y;
                if (!(grid.sx == mState.x && grid.sy == mState.y))
                    return null;
                newState.carrying = 0;
                newState.time = mState.time + 1;
                newState.safe = mState.safe;
                newState.totalDamage = evalDamage(mState.totalDamage, newState.safe, newState.time);
                newState.dead = evalDead(mState.dead, newState.safe, newState.time);
                Node newNode = new Node(newState, n, operators.get(5), evalDepth(n.getDepth()), evalCost(newState));
                return newNode;
            }

            @Override
            public String getName() {
                return "drop";
            }
        });

        return operators;
    }

    public int evalCost(MissionImpossibleState state) {

        return state.dead * 1001 + state.totalDamage;
    }

    public int evalDamage(int oldDamage, int newSafe, int time) {
        int tot = oldDamage;
        for (int i = 0; i < grid.k; ++i) {
            if (((newSafe & (1 << i)) != 0))
                continue;
            int newHealth = Math.min(100, grid.h[i] + 2 * (time));
            tot += Math.min(100 - newHealth, 2);
        }
        return tot;
    }

    public int evalDead(int oldDead, int newSafe, int time) {
        int totDead = oldDead;
        for (int i = 0; i < grid.k; ++i) {
            if (((newSafe & (1 << i)) != 0))
                continue;
            int newHealth = grid.h[i] + 2 * (time);
            int oldHealth = grid.h[i] + 2 * (time - 1);
            if (newHealth >= 100 && oldHealth < 100)
                totDead += 1;
        }

        return totDead;
    }

    public int evalDepth(int oldDepth) {

        return oldDepth + 1;
    }

    public boolean containsIMF(int x, int y, int safe) {
        for (int i = 0; i < grid.k; ++i) {
            if (grid.x[i] == x && grid.y[i] == y && ((safe & (1 << i)) == 0))
                return true;
        }
        return false;
    }

    public int saveIMF(int x, int y, int oldSafe) {
        for (int i = 0; i < grid.k; ++i) {
            if (grid.x[i] == x && grid.y[i] == y)
                oldSafe |= (1 << i);
        }
        return oldSafe;
    }

    public Node getNextNodeDirOp(int dx, int dy, Node n, int opIdx) {
        MissionImpossibleState mState = (MissionImpossibleState) n.getState();
        MissionImpossibleState newState = new MissionImpossibleState();
        newState.time = mState.time + 1;

        if (!safe(mState.x, mState.y, dx, dy)) {
//			newState.x = mState.x;
//			newState.y = mState.y;
//			newState.carrying = mState.carrying;
//			newState.safe = mState.safe;
//			newState.totalDamage = evalDamage(mState.totalDamage, newState.safe, newState.time);
//			newState.dead = evalDead(mState.dead, newState.safe, newState.time);
//			Node newNode = new Node(newState, n, operators.get(opIdx), evalDepth(n.getDepth()), evalCost(newState));
            return null;
        }

        newState.x = mState.x + dx;
        newState.y = mState.y + dy;
        newState.carrying = mState.carrying;
        newState.safe = mState.safe;
        newState.totalDamage = evalDamage(mState.totalDamage, newState.safe, newState.time);
        newState.dead = evalDead(mState.dead, newState.safe, newState.time);
        Node newNode = new Node(newState, n, operators.get(opIdx), evalDepth(n.getDepth()), evalCost(newState));
        return newNode;
    }

    private boolean safe(int x, int y, int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;
        if (newX <= -1 || newX >= n || newY <= -1 || newY >= m)
            return false;
        return true;
    }

    @Override
    public State getInitState() {
        MissionImpossibleState initState = new MissionImpossibleState();
        initState.x = grid.ex;
        initState.y = grid.ey;
        initState.totalDamage = sum(grid.h);
        initState.dead = countDead(grid.h);
        return initState;
    }

    @Override
    public ArrayList<State> getStateSpace() {
//		ArrayList<MissionImpossibleState> stateSpace = new ArrayList<MissionImpossibleState>();
//		for (int i = 0; i<memo.length; ++i)
//			for (int j =0 ;j<memo[0].length; ++j)
//				for (int k=0; k<memo[0][0].length; ++k)
//					for (int l=0; l<memo[0][0][0].length; ++l)
//						for (int m=0; m<memo[0][0][0][0].length; ++m) {
//							MissionImpossibleState s = new MissionImpossibleState(j,k,i,l,m,0,0);
//							s.totalDamage = evalDamage(s.safe, s.time);
//							s.dead = evalDead(s.safe, s.time);
//							stateSpace.add(s);
//						}
        return null;
    }

    @Override
    public boolean isGoal(State state) {
        MissionImpossibleState mState = (MissionImpossibleState) state;
        if (mState.carrying == 0 && allSafe(mState.safe))
            return true;
        return false;
    }

    @Override
    public long pathCost(Node lastOnPath) {
        return lastOnPath.getCost();
    }

    @Override
    public int getStateVal(State s) {
        MissionImpossibleState mState = (MissionImpossibleState) s;
        return memo[mState.carrying][mState.x][mState.y][mState.time][mState.safe];
    }

    @Override
    public void assignStateVal(State state, int val) {
        MissionImpossibleState mState = (MissionImpossibleState) state;
        memo[mState.carrying][mState.x][mState.y][mState.time][mState.safe] = (short) val;
    }

    public boolean allSafe(int safe) {
        int imfCount = grid.k;
        return Integer.bitCount(safe) == imfCount;
    }

    static int sum(int[] d) {
        int tot = 0;
        for (int x : d)
            tot += x;
        return tot;
    }

    static int countDead(int[] health) {
        int c = 0;
        for (int x : health)
            if (x == 100)
                c++;
        return c;
    }

}

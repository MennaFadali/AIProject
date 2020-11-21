package code.mission;

import code.generic.State;

public class MissionImpossibleState implements State{

	int x , y , carrying , time , safe , dead, totalDamage;

	@Override
	public String toString() {
		return "MissionImpossibleState [x=" + x + ", y=" + y + ", carrying=" + carrying + ", time=" + time + ", safe="
				+ safe + ", dead=" + dead + ", totalDamage=" + totalDamage + "]";
	}

	public MissionImpossibleState(int x, int y, int carrying, int time, int safe, int dead, int totalDamage) {
		super();
		this.x = x;
		this.y = y;
		this.carrying = carrying;
		this.time = time;
		this.safe = safe;
		this.dead = dead;
		this.totalDamage = totalDamage;
	}
	
	public MissionImpossibleState() {}

	
}

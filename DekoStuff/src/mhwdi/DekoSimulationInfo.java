package mhwdi;

/**
 *
 * @author mhe
 */
public class DekoSimulationInfo {

	private int steps;
	private Dekoration lastMissingDeko;
	private int lastMissingDekoStart;
	private int lastMissingDekoMissing;
	private int excessSmeltingPoints;

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public Dekoration getLastMissingDeko() {
		return lastMissingDeko;
	}

	public void setLastMissingDeko(Dekoration lastMissingDeko) {
		this.lastMissingDeko = lastMissingDeko;
	}

	public int getLastMissingDekoStart() {
		return lastMissingDekoStart;
	}

	public void setLastMissingDekoStart(int lastMissingDekoStart) {
		this.lastMissingDekoStart = lastMissingDekoStart;
	}

	public int getLastMissingDekoMissing() {
		return lastMissingDekoMissing;
	}

	public void setLastMissingDekoMissing(int lastMissingDekoMissing) {
		this.lastMissingDekoMissing = lastMissingDekoMissing;
	}

	public DekoSimulationInfo(int steps, Dekoration lastMissingDeko, int lastMissingDekoStart,
			int lastMissingDekoMissing) {
		this.steps = steps;
		this.lastMissingDeko = lastMissingDeko;
		this.lastMissingDekoStart = lastMissingDekoStart;
		this.lastMissingDekoMissing = lastMissingDekoMissing;
	}

	public DekoSimulationInfo() {
	}

	public int getExcessSmeltingPoints() {
		return excessSmeltingPoints;
	}

	public void setExcessSmeltingPoints(int excessSmeltingPoints) {
		this.excessSmeltingPoints = excessSmeltingPoints;
	}
}

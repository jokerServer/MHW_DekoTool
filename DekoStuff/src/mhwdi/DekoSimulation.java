package mhwdi;

/**
 *
 * @author mhe
 */
public class DekoSimulation implements Runnable {
	private DekoList dekoList;
	private DekoSimulationInfo dekoSimInfo = new DekoSimulationInfo();

	public DekoSimulation(DekoList pDL) {
		this.dekoList = pDL;
	}

	@Override
	public void run() {
		Dekoration lastMissingDeko = null;
		int lastMissingDekoStart = -1;
		int lastMissingDekoMissing = -1;

		int tbd = dekoList.getDekoListSize();
		int steps = 0;

		while (tbd > 0) {
			Dekoration d = dekoList.getDekoByT2SumChance(Math.random());
			if (d != null) {
				d.setOwned(d.getOwned() + 1);
				if (d.getSkillMaxLvl() == d.getOwned()) {
					if (--tbd == 1) {
						Dekoration[] missingList = dekoList.getIncompleteDekoList();
						if (missingList.length != 1) {
							throw new RuntimeException("inner tasklenght mismatch!");
						}

						// annotate last missing deko information
						lastMissingDeko = missingList[0];
						lastMissingDekoStart = steps;
						lastMissingDekoMissing = lastMissingDeko.getSkillMaxLvl() - lastMissingDeko.getOwned();
					}
				}
			}
			steps++;
		}

		// Sanity Checks
		if (dekoList.getIncompleteDekoList().length != 0) {
			throw new RuntimeException("outer tasklenght mismatch!");
		}

		if (lastMissingDeko == null) {
			throw new RuntimeException("no last missing, deko");
		}

		dekoSimInfo.setLastMissingDeko(lastMissingDeko);
		dekoSimInfo.setLastMissingDekoMissing(lastMissingDekoMissing);
		dekoSimInfo.setLastMissingDekoStart(lastMissingDekoStart);
		dekoSimInfo.setExcessSmeltingPoints(dekoList.getExcessSmeltingPoints());
		dekoSimInfo.setSteps(steps);
	}

	public DekoList getDekoList() {
		return dekoList;
	}

	public void setDekoList(DekoList dekoList) {
		this.dekoList = dekoList;
	}

	public DekoSimulationInfo getDekoSimInfo() {
		return dekoSimInfo;
	}

	public void setDekoSimInfo(DekoSimulationInfo dekoSimInfo) {
		this.dekoSimInfo = dekoSimInfo;
	}
}

package mhwdi;

/**
 *
 * @author mhe
 */
public class DekoSimulation implements Runnable {
	DekoList dekoList;
	DekoSimulationInfo dekoSimInfo = new DekoSimulationInfo();

	public DekoSimulation(DekoList pDL) {
		this.dekoList = pDL;
	}

	public DekoSimulationInfo simulate() {
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
					tbd--;
					if (tbd == 1) {
						Dekoration[] missingList = dekoList.getIncompleteDekoList();
						if (missingList.length != 1) {
							throw new RuntimeException("wat");
						}

						Dekoration d2 = missingList[0];
						lastMissingDeko = d2;
						lastMissingDekoStart = steps;
						lastMissingDekoMissing = d2.getSkillMaxLvl() - d2.getOwned();
					}
				}
			}
			steps++;
		}

		if (dekoList.getIncompleteDekoList().length != 0) {
			throw new RuntimeException("wat");
		}

		dekoSimInfo.setLastMissingDeko(lastMissingDeko);
		dekoSimInfo.setLastMissingDekoMissing(lastMissingDekoMissing);
		dekoSimInfo.setLastMissingDekoStart(lastMissingDekoStart);
		dekoSimInfo.setSteps(steps);
		return this.dekoSimInfo;
	}

	@Override
	public void run() {
		simulate();
	}
}

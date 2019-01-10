package mhwdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 *
 * @author mhe
 */
public class DekoSimulator {

	public static void main(String[] args) {
		final int RUNS = 10000;
		final int THREADCOUNT = 8;
		ExecutorService executor = Executors.newFixedThreadPool(THREADCOUNT);

		String csvFilename = "myDekoCsv3.csv";
		DekoList dekoList = new DekoList(csvFilename, 1);

		ArrayList<DekoSimulationInfo> simInfo = new ArrayList<>();

		for (int i = 0; i < RUNS; i++) {
			DekoSimulation dekoSim = new DekoSimulation(dekoList.clone());
			simInfo.add(dekoSim.dekoSimInfo);
			Thread worker = new Thread(dekoSim);
			worker.setName("" + i);
			executor.execute(worker);
		}
		System.out.println("init done");

		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Finished all threads");
		System.out.println();

		Map<String, Long> dekoCountMap = simInfo.stream()
				.collect(Collectors.groupingBy(s -> s.getLastMissingDeko().getDekoName(), Collectors.counting()));
		// Entry<String, Long> maxLast = dekoCountMap.entrySet().stream().max((p1, p2)
		// -> Long.compare( p1.getValue(),
		// p2.getValue())).orElseThrow(RuntimeException::new);

		ArrayList<Entry<String, Long>> dekoCountSort = new ArrayList<>(dekoCountMap.entrySet());
		dekoCountSort.sort(Entry.comparingByValue());
		Collections.reverse(dekoCountSort);

		int lastAvgSteps = simInfo.stream().mapToInt(s -> s.getSteps() - s.getLastMissingDekoStart()).sum();
		int lastAvgSize = simInfo.stream().mapToInt(s -> s.getLastMissingDekoMissing()).sum();
		float avgSteps = simInfo.stream().mapToInt(s -> s.getSteps()).sum() / (RUNS * 1f);

		System.out.println("Simulation of " + RUNS + " runs resulted in " + avgSteps + " T2 Rewards average.");
		System.out.println("The last deko type required an average of " + lastAvgSteps / (RUNS * 1f)
				+ " steps to collect the last " + lastAvgSize / (RUNS * 1f) + " missing dekos");
		dekoCountSort.stream().forEach(s -> System.out
				.println(s.getKey() + " in " + s.getValue() + " runs (" + (s.getValue() * 100f) / (RUNS * 1f) + "%)"));
	}
}

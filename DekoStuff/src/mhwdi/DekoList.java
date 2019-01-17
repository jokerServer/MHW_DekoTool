package mhwdi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 *
 * @author mhe
 */
public class DekoList {
	private File csvDekoFile;
	public final static Map<Integer, Integer> DEKO_RARITY_SMELTPOINTS = Map.ofEntries(
	        Map.entry(5, 4),
	        Map.entry(6, 8),
	        Map.entry(7, 64),
	        Map.entry(8, 124)
	    );

	Boolean useTemporaryCSV = false;
	ArrayList<Dekoration> dekoList = new ArrayList<>();

	public DekoList(String dekoFile, int version) {
		csvDekoFile = new File("data/" + dekoFile);

		if (version == 0) {
			reloadCSV();
		} else if (version == 1) {
			reloadCSVv2();
		}
	}

	public DekoList(ArrayList<Dekoration> pDekoList, File pCsvDekoFile) {
		pDekoList.stream().forEachOrdered(s -> this.dekoList.add(s.clone()));
		this.csvDekoFile = pCsvDekoFile;
	}

	public final void reloadCSV() {
		throw new UnsupportedOperationException("shits broken yo");
	}

//    public final void reloadCSV() {
//        Integer csvLineCount = 1;
//        try (BufferedReader br = new BufferedReader(new FileReader(csvDekoFile))) {
//            String csvLine;
//            String[] csvColumns;
//            br.readLine(); //ignore column names
//            while ((csvLine = br.readLine()) != null && !"".equals(csvLine)) {
//                csvColumns = csvLine.split(";");
//
//                Dekoration csvLineDeko = new Dekoration(
//                        /* rownum */csvLineCount,
//                        /* owned  */ ("".equals(csvColumns[0])) ? 0 : Integer.parseInt(csvColumns[0]),
//                        /* trash  */ //"".equals(csvColumns[1]),
//                        /*dekoName*/ csvColumns[3],
//                        /* skill  */ csvColumns[4],
//                        /*skilldsc*/ csvColumns[5],
//                        /*skillmax*/ Integer.parseInt(csvColumns[6]),
//                        /*dekolvl */ Integer.parseInt(csvColumns[7]),
//                        /*dekorar */ Integer.parseInt(csvColumns[8]),
//                        /* myst%  */ "-".equals(csvColumns[9]) ? 0f : Float.parseFloat(csvColumns[9].split("%")[0].replace(",", ".")),
//                        /* glow%  */ "-".equals(csvColumns[10]) ? 0f : Float.parseFloat(csvColumns[10].split("%")[0].replace(",", ".")),
//                        /* worn%  */ "-".equals(csvColumns[11]) ? 0f : Float.parseFloat(csvColumns[11].split("%")[0].replace(",", ".")),
//                        /* warped%*/ "-".equals(csvColumns[12]) ? 0f : Float.parseFloat(csvColumns[12].split("%")[0].replace(",", ".")));
//
//                dekoList.add(csvLineDeko);  */
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException("Error opening deko File", e);
//        } catch (IOException e) {
//            throw new RuntimeException("Error while parsing CSV-File " + csvDekoFile.getAbsolutePath() + " at " + csvLineCount, e);
//        }
//    }

	public final void reloadCSVv2() {
		Integer csvLineCount = 1;
		Boolean german = false;

		try (BufferedReader br = new BufferedReader(new FileReader(csvDekoFile))) {
			String csvLine;
			String[] csvColumns;
			br.readLine(); // ignore column names
			while ((csvLine = br.readLine()) != null && !"".equals(csvLine)) {
				csvColumns = csvLine.split(";");
				Dekoration csvLineDeko;

				csvLineDeko = new Dekoration(/* rownum */csvLineCount, /* owned */ Integer.parseInt(csvColumns[0]),
						/* tbs */ 0, /* dekoName */ csvColumns[1], /* skill */ csvColumns[2],
						/* skilldsc */ csvColumns[3], /* skillmax */ Integer.parseInt(csvColumns[6]),
						/* dekolvl */ Integer.parseInt(csvColumns[7]), /* dekorar */ Integer.parseInt(csvColumns[8]),
						/* weight */ Integer.parseInt(csvColumns[9]), /* myst% */ Float.parseFloat(csvColumns[10]),
						/* glow% */ Float.parseFloat(csvColumns[11]), /* worn% */ Float.parseFloat(csvColumns[12]),
						/* warped% */ Float.parseFloat(csvColumns[13]), /* T1 inv */ Float.parseFloat(csvColumns[14]),
						/* T2 inv */ Float.parseFloat(csvColumns[15]), /* T3 inv */ Float.parseFloat(csvColumns[16]));

				//if (german) {
				//	/* dekoName */ csvColumns[4]
				//	/* skill */ csvColumns[5]
				//	/* skilldsc */ csvColumns[3]
				//}

				dekoList.add(csvLineDeko);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error opening deko File", e);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while parsing CSV-File " + csvDekoFile.getAbsolutePath() + " at " + csvLineCount, e);
		}
	}

	public Dekoration[] findMatching(String searchText, Boolean useDekoName, Boolean useSkillName,
			Boolean useSkillDesc) {
		return dekoList.stream()
				.filter(s -> (useDekoName && s.getDekoName().toLowerCase().contains(searchText))
						|| (useSkillName && s.getSkillName().toLowerCase().contains(searchText))
						|| (useSkillDesc && s.getSkillDesc().toLowerCase().contains(searchText)))
				.toArray(Dekoration[]::new);
	}

	public Dekoration[] findExcess() {
		return dekoList.stream().filter(s -> s.getOwned() > s.getSkillMaxLvl())
				.sorted((x, y) -> Integer.compare(
						(x.getSkillMaxLvl() - x.getOwned()) * DEKO_RARITY_SMELTPOINTS.get(x.getDekoRarity()),
						(y.getSkillMaxLvl() - y.getOwned()) * DEKO_RARITY_SMELTPOINTS.get(y.getDekoRarity())))
				.toArray(Dekoration[]::new);
	}

	public int getExcessSmeltingPoints() {
		return dekoList.stream()
				.map((d) -> (d.getOwned() - d.getSkillMaxLvl()) * DEKO_RARITY_SMELTPOINTS.get(d.getDekoRarity()))
				.map((sp) -> (sp > 0) ? sp : 0).reduce(0, Integer::sum);
	}

	public int getUncoolExcessSmeltingPoints() {
		int result = 0;
		for (Dekoration d : dekoList) {
			int tmp = (d.getOwned() - d.getSkillMaxLvl()) * DEKO_RARITY_SMELTPOINTS.get(d.getDekoRarity());
			result += (tmp > 0) ? tmp : 0;
		}
		return result;
	}

	public Dekoration[] findSmeltingSetup(int wantedPoints) {
		int neededPoints = wantedPoints;

		dekoList.forEach(s -> s.setToBeSmelted(0)); // prob useless :x

		List<Dekoration> tmp = dekoList.stream().filter(s -> s.getOwned() > s.getSkillMaxLvl())
				.sorted((x, y) -> Integer.compare(DEKO_RARITY_SMELTPOINTS.get(y.getDekoRarity()),
						DEKO_RARITY_SMELTPOINTS.get(x.getDekoRarity())))
				.collect(Collectors.toList());

		for (Dekoration d : tmp) {
			d.setToBeSmelted(0);
			for (int i = 0; i < (d.getOwned() - d.getSkillMaxLvl()); i++) {
				if (neededPoints - DEKO_RARITY_SMELTPOINTS.get(d.getDekoRarity()) >= 0) {
					d.setToBeSmelted(d.getToBeSmelted() + 1);
					neededPoints = neededPoints - DEKO_RARITY_SMELTPOINTS.get(d.getDekoRarity());
				}
			}
		}

		if (neededPoints > 0) {
			System.out.println("You dont have enough decorations to smelt efficiently, missing points: " + neededPoints
					+ "/" + wantedPoints);
		}
		return tmp.stream().filter(s -> s.getToBeSmelted() > 0).toArray(Dekoration[]::new);
	}

	public void useSmeltingSetup() {
		dekoList.stream().filter(s -> s.getToBeSmelted() > 0).forEach(s -> {
			s.setOwned(s.getOwned() - s.getToBeSmelted());
			s.setToBeSmelted(0);
		});
	}

	public void setDekoByRow(int newDekoRow, Dekoration newDeko) {
		dekoList.set(newDekoRow, newDeko);
	}

	public Dekoration getDekoByMatch(String searchText, Boolean useDekoName, Boolean useSkillName,
			Boolean useSkillDesc) {
		Dekoration[] result = findMatching(searchText, useDekoName, useSkillName, useSkillDesc);
		if (result.length > 1) {
			System.out.println("Too many results in getDekoByMatch! Using first element");
		} else if (result.length == 0) {
			throw new NoSuchElementException();
		}
		return result[0];
	}

	/*
	 * @param Double between 0 and 1
	 *
	 * @return the first Dekoration that has a aggregated chance of sumChance or
	 * higher
	 */
	public Dekoration getDekoByT2SumChance(Double sumChance) {
		double aggregate = 0;
		for (Dekoration d : dekoList) {
			double t2chance = d.getDekoChanceT2();
			if (t2chance + aggregate >= sumChance) {
				return d;
			}
			aggregate += t2chance;
		}
		return null;
	}

	@Deprecated
	public void writeCSV(String path) {
		// writeCsvFile(csvDekoFile.getAbsolutePath());
		File csvSaveFile = new File(path);
		try {
			List<String> lines = new LinkedList<>();
			lines.add("lead"); // rplc head
			dekoList.forEach(d -> lines.add(d.printCSVData()));
			Files.write(csvSaveFile.toPath(), lines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public void updateCSVFile() {
		List<String> lines = new LinkedList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvDekoFile))) {
			lines.add(br.readLine());
			for (Dekoration d : dekoList) {
				lines.add(d.printCSVData());
			}
			System.out.println(csvDekoFile.getAbsolutePath());

			Files.write(csvDekoFile.toPath(), lines);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}


	public Dekoration[] getIncompleteDekoList() {
		return dekoList.stream().filter(s -> s.getOwned() < s.getSkillMaxLvl()).toArray(Dekoration[]::new);
	}

	public void resetOwned() {
		dekoList.forEach((d) -> {
			d.setOwned(0);
		});
	}

	public Dekoration getDekoByRow(int dekoRow) {
		return dekoList.get(dekoRow);
	}

	public int getDekoListSize() {
		return dekoList.size();
	}

	@Override
	public DekoList clone() {
		return new DekoList(dekoList, csvDekoFile);
	}
}

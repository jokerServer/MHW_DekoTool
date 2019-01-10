package mhwdi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author mhe
 */
public class MHWDI {
	public static void main(String[] args) {
		System.out.println("MHW Deko Manager - V0.1");

		String csvFilename = "myDekoCsv.csv";
		if (args.length > 0)
			csvFilename = args[0];

		DekoList dekoList = new DekoList(csvFilename, 0);
		Dekoration[] result;
		String input = "";
		int useIndex;
		Boolean useFirst;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// System.out.println(dekoList.getDekoByRow(23).printCSVData());
		System.out.println("Hi Jan :D - you may now enter your command:");
		System.out.println();

		while (true) {
			try {
				System.out.print("#: ");
				input = br.readLine();
			} catch (IOException e) {
				throw new RuntimeException("input error", e);
			}
			String[] inputA = input.split(" ");
			for (String s : inputA)
				s = s.replace("#", " ");

			useFirst = false;
			useIndex = -1;
			result = null;

			switch (inputA[0]) {
			case "excess":
				result = dekoList.findExcess();
				break;
			case "list":
				switch (inputA.length) {
				case 1:
					result = dekoList.dekoList.toArray(new Dekoration[0]);
					break;
				case 2:
					result = dekoList.findMatching(inputA[1], true, false, false);
					break;
				case 3:
					switch (inputA[2]) {
					case "deko":
						result = dekoList.findMatching(inputA[1], true, false, false);
						break;
					case "skill":
						result = dekoList.findMatching(inputA[1], false, true, false);
						break;
					case "ds":
						result = dekoList.findMatching(inputA[1], true, true, false);
						break;
					case "dsd":
						result = dekoList.findMatching(inputA[1], true, true, true);
						break;
					case "sd":
						result = dekoList.findMatching(inputA[1], false, true, true);
						break;
					case "desc":
						result = dekoList.findMatching(inputA[1], false, false, true);
						break;
					default:
						System.out.println("Illegal Filter Param!");
						break;
					}
					break;
				default:
					System.out.println("illegal param count");
					break;
				}
				break;
			case "addi":
				useIndex = Integer.parseInt(inputA[inputA.length - 1]);
			case "addf":
				useFirst = true;
			case "add":
				useIndex = (useIndex >= 0) ? useIndex : 0;
				switch (inputA.length - ((useIndex > 0) ? 1 : 0)) {
				case 2:
					result = dekoList.findMatching(inputA[1].toLowerCase(), true, false, false);
					if ((result.length == 1 || (useFirst && result.length > 1))
							&& (result.length > useIndex || useIndex <= 0)) {
						result[useIndex].setOwned(result[useIndex].getOwned() + 1);
					} else
						System.out.println("Result size not 1 or index out of bounds! " + useIndex);
					break;
				case 3:
					result = dekoList.findMatching(inputA[1].toLowerCase(), true, false, false);
					if ((result.length == 1 || (useFirst && result.length > 1))
							&& (result.length > useIndex || useIndex <= 0)) {
						result[useIndex].setOwned(result[useIndex].getOwned() + Integer.parseInt(inputA[2]));
					} else
						System.out.println("Result size not 1 or index out of bounds! " + result.length);
					break;
				default:
					System.out.println("illegal param count");
					break;
				}
				break;
			case "setup":
				if (inputA.length == 2)
					result = dekoList.findSmeltingSetup(Integer.parseInt(inputA[1]));
				else
					System.out.println("illegal param count");
				break;
			case "smelt":
				if (inputA.length == 1)
					dekoList.useSmeltingSetup();
				else
					System.out.println("illegal param count");
				break;
			case "save":
				if (inputA.length == 2)
					dekoList.writeCSV(inputA[1]);
				else
					System.out.println("illegal param count");
				break;
			case "update":
				if (inputA.length == 1)
					dekoList.updateCSVFile();
				else
					System.out.println("illegal param count");
				break;
			case "reset":
				if (inputA.length == 1)
					dekoList.reloadCSV();
				else
					System.out.println("illegal param count");
				break;
			case "wipe":
				if (inputA.length == 1)
					dekoList.resetOwned();
				else
					System.out.println("illegal param count");
				break;
			case "exit":
				return;
			default:
				System.out.println("unknown command");
				break;
			}

			// Dekoration[] result = dekoList.findExcess();
			if (result instanceof Dekoration[] && result.length > 0) {
				StringBuilder sb;
				String dekoName;
				String skillName;
				Integer owned, excess, excessSmeltingPoints, toBeSmelted, smeltingPointPerDeko;
				System.out.println("| Dekoration          | Skill               | Owned | Excess |"
						+ (("setup".equals(inputA[0])) ? " Smelt |" : "") + " SPD | ExSmP. |");
				System.out.println("--------------------------------------------------------------"
						+ (("setup".equals(inputA[0])) ? "-------" : "") + "---------------");
				for (Dekoration d : result) {
					sb = new StringBuilder();
					dekoName = d.getDekoName();
					skillName = d.getSkillName();
					owned = d.getOwned();
					excess = d.getOwned() - d.getSkillMaxLvl();
					toBeSmelted = d.getToBeSmelted();
					smeltingPointPerDeko = DekoList.DEKO_RARITY_SMELTPOINTS.get(d.getDekoRarity());
					excessSmeltingPoints = excess * smeltingPointPerDeko;
					excessSmeltingPoints = (excessSmeltingPoints > 0) ? excessSmeltingPoints : 0;

					sb.append("| ").append((dekoName.length() > 19) ? dekoName.substring(0, 19) : dekoName);
					for (int i = d.getDekoName().length(); i < 19; i++)
						sb.append(' ');
					sb.append(" | ").append((skillName.length() > 19) ? skillName.substring(0, 19) : skillName);
					for (int i = d.getSkillName().length(); i < 19; i++)
						sb.append(' ');
					sb.append(" | ").append(owned);
					for (int i = owned.toString().length(); i < 5; i++)
						sb.append(' ');
					sb.append(" | ").append(excess);
					for (int i = excess.toString().length(); i < 6; i++)
						sb.append(' ');
					if ("setup".equals(inputA[0])) {
						sb.append(" | ").append(toBeSmelted);
						for (int i = toBeSmelted.toString().length(); i < 5; i++)
							sb.append(' ');
					}
					sb.append(" | ").append(smeltingPointPerDeko);
					for (int i = smeltingPointPerDeko.toString().length(); i < 3; i++)
						sb.append(' ');
					sb.append(" | ").append(excessSmeltingPoints);
					for (int i = excessSmeltingPoints.toString().length(); i < 6; i++)
						sb.append(' ');
					sb.append(" |");
					System.out.println(sb);
				}
				if ("excess".equals(inputA[0]))
					System.out.println("Sum of excess smelting points: " + dekoList.getExcessSmeltingPoints());
			} else
				System.out.println("-/-");
			System.out.println();
		}
	}
}

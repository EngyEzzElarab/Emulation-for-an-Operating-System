import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class Kernel {
	Memory memory;
	Queue<Integer> readyQueue = new LinkedList<Integer>();
	int arrOfInstructions[] = new int[3];
	int processID = 1;

	public Kernel() {
		memory = new Memory();
	}

	public static int index(String name, int ProcessID) {
		if (ProcessID == 1) {
			if (name.equals("ProcessID")) {
				return 0;
			}
			if (name.equals("ProcessState")) {
				return 1;
			}
			if (name.equals("PC")) {
				return 2;
			}
			if (name.equals("LowerBoundary")) {
				return 3;
			}
			if (name.equals("UpperBoundary")) {
				return 4;
			}
		} else if (ProcessID == 2) {
			if (name.equals("ProcessID")) {
				return 5;
			}
			if (name.equals("ProcessState")) {
				return 6;
			}
			if (name.equals("PC")) {
				return 7;
			}
			if (name.equals("LowerBoundary")) {
				return 8;
			}
			if (name.equals("UpperBoundary")) {
				return 9;
			}
		} else if (ProcessID == 3) {
			if (name.equals("ProcessID")) {
				return 10;
			}
			if (name.equals("ProcessState")) {
				return 11;
			}
			if (name.equals("PC")) {
				return 12;
			}
			if (name.equals("LowerBoundary")) {
				return 13;
			}
			if (name.equals("UpperBoundary")) {
				return 14;
			}
		}
		return -1;
	}

	public void shortTermScheduler() throws IOException {
		boolean processState = false;
		int PC = 0;
		int lowerBoundary = 0;
		int upperBoundary = 0;
		boolean flag = false;
		while (!readyQueue.isEmpty()) {
			processID = readyQueue.remove();
			System.out.println("Started : Process:" + processID);
			processState = true;
			memory._memory[index("ProcessState", processID)] = processState;
			PC = (int) memory._memory[index("PC", processID)];
			lowerBoundary = (int) memory._memory[index("LowerBoundary", processID)];
			upperBoundary = (int) memory._memory[index("UpperBoundary", processID)];
			for (int i = 0; i < 2 && (PC) < (arrOfInstructions[processID - 1]); i++) {
				Vector<String> vector = new Vector<>();
				if (i == 1) {
					flag = true;
				}
				if (!(memory._memory[lowerBoundary + PC] instanceof Pair)) {
					String[] temp = ((String) memory._memory[lowerBoundary + PC]).split(" ");

					for (int j = 0; j < temp.length; j++)
						vector.add(temp[j]);
					_codeParser.tester(vector);
					PC++;
					memory._memory[index("PC", processID)] = PC;
				}
			}
			processState = false;
			memory._memory[index("ProcessState", processID)] = processState;
			if (PC < arrOfInstructions[processID - 1]) {
				readyQueue.add(processID);
			} else {
				if (flag) {
					System.out.println("Ended : Process:" + processID + " Two Instructions were executed");
				} else {
					System.out.println("Ended : Process:" + processID + " One Instructions were executed");
				}
			}
			flag = false;
		}
	}

	public void longTermScheduler() throws IOException {

		boolean flagA = false;
		boolean flagB = false;
		for (int i = 1; i <= 3; i++) {
			int count = 0;
			String path = "Program " + i + ".txt";
			PCB pcb = new PCB(i);
			pcb.lowerBoundary = memory.sofar;
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while (line != null) {
				count++;

				if (!memory.isFull) {
					int j = memory.sofar;
					memory._memory[j] = line;
					memory.sofar++;
				}

				String[] instruction = line.split(" ");
				for (int i1 = 0; i1 < instruction.length; i1++) {
					if (instruction[i1].length() == 1 && !isNumeric(instruction[i1])) {
						if (instruction[i1].equals("a") && !flagA) {
							flagA = true;
						}
						if (instruction[i1].equals("b") && !flagB) {
							flagB = true;
						}
					}
				}
				line = br.readLine();
			}
			if (flagA) {
				Pair p = new Pair("a", null);
				memory._memory[memory.sofar++] = p;
			}
			if (flagB) {
				Pair p = new Pair("b", null);
				memory._memory[memory.sofar++] = p;
			}
			pcb.upperBoundary = memory.sofar;
			pcb.processID = i;
			processID = i;
			readyQueue.add(i);
			arrOfInstructions[i - 1] = count;
			memory._memory[index("ProcessID", processID)] = pcb.processID;
			memory._memory[index("ProcessState", processID)] = pcb.processState;
			memory._memory[index("PC", processID)] = pcb.PC;
			memory._memory[index("LowerBoundary", processID)] = pcb.lowerBoundary;
			memory._memory[index("UpperBoundary", processID)] = pcb.upperBoundary;
			System.out.println(" processID: " + processID);
			System.out.println(" Process State: " + pcb.processState);
			System.out.println(" PC: " + pcb.PC);
			System.out.println("LowerBoundary: " + pcb.lowerBoundary);
			System.out.println("UpperBoundary: " + pcb.upperBoundary);

			flagA = false;
			flagB = false;
		}
		shortTermScheduler();
	}

	public Object readFromMemory(String variableName) {
		int lo = Integer.parseInt(memory._memory[index("LowerBoundary", processID)] + "");
		int high = Integer.parseInt(memory._memory[index("UpperBoundary", processID)] + "");
		while (lo <= high) {
			if (memory._memory[lo] instanceof Pair && ((Pair) memory._memory[lo]).name.equals(variableName)) {
				System.out.println("Read from Memory: " +(Pair) memory._memory[lo]+" From index: "+ lo);
				return ((Pair) memory._memory[lo]).value;
			}
			lo++;
		}
		return -1;
	}

	public void writeToMemory(String variableName, Object value) {
		boolean found = false;
		int lo = Integer.parseInt(memory._memory[index("LowerBoundary", processID)] + "");
		int high = Integer.parseInt(memory._memory[index("UpperBoundary", processID)] + "");
		while (lo <= high) {
			if (memory._memory[lo] instanceof Pair && ((Pair) memory._memory[lo]).name.equals(variableName)) {
				((Pair) memory._memory[lo]).value = value;
				System.out.println("Write to Memory: " +(Pair) memory._memory[lo]+" From index: "+ lo);
				found = true;
			}
			lo++;
		}
//		if (!found) {
//			Pair p = new Pair(variableName, value);
//		}
	}

	public static Object userInput() {
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}

	public void print(String print) {
		boolean found = false;
		int lo = Integer.parseInt(memory._memory[index("LowerBoundary", processID)] + "");
		int high = Integer.parseInt(memory._memory[index("UpperBoundary", processID)] + "");
		while (lo <= high) {
			if (memory._memory[lo] instanceof Pair && ((Pair) memory._memory[lo]).name.equals(print.trim())) {
				System.out.println(((Pair) memory._memory[lo]).value);
				found = true;
			}
			lo++;
		}
		if (!found) {
			System.out.println((print.trim()));
		}
	}

	public static boolean isNumeric(String str) {
		return str != null && str.matches("[-+]?\\d*\\.?\\d+");
	}

	public String readFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
//		print(sb.toString());
		return sb.toString();
	}

	public void writeFile(String path,String data) throws IOException {
		File tmpDir = null;
		tmpDir= new File(path + "");
		boolean exists = tmpDir.exists();
		if (exists) {
			try {
				FileWriter fstream = new FileWriter(path, true);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("\n" + data + "\n");
				out.close();
			} catch (Exception e) {
				System.err.println("Error while writing to file: " + e.getMessage());
			}
		} else {
			try {
				File myObj = new File(path);
				if (myObj.createNewFile()) {
					System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			try {
				FileWriter fstream = new FileWriter(path, true);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(data + "\n");
				out.close();
			} catch (Exception e) {
				System.err.println("Error while writing to file: " + e.getMessage());
			}
		}
	}
}

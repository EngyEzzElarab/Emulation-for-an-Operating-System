import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;

public class _codeParser {

	static Kernel kernel;
	static String[] instruction;
	static Vector<String> _command;

	public _codeParser() {
		kernel = new Kernel();
		instruction = null;
		_command = new Vector<String>();
	}

	public static void assign(String first, String second) {
		String c = second;
		if (second.equals("input")) {
			kernel.writeToMemory(first, kernel.userInput());
		} else if (second.length() != 1) {
			String[] g = second.split(" ");
			try {
				if(g[0].equals("readFile"))
				c = kernel.readFile(kernel.readFromMemory(g[1])+"");
				kernel.writeToMemory(first,c);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int lo = Integer.parseInt(kernel.memory._memory[kernel.index("LowerBoundary", kernel.processID)] + "");
			int high = Integer.parseInt(kernel.memory._memory[kernel.index("UpperBoundary", kernel.processID)] + "");
		} else if (isNumeric(c)) {
			kernel.writeToMemory(first, Integer.parseInt(c));
		} else {
			kernel.writeToMemory(first, c);
		}
	}

	public static String getFileContent(String filepath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		Parser(sb);
		return sb.toString();
	}

	public static boolean isNumeric(String str) {
		return str != null && str.matches("[-+]?\\d*\\.?\\d+");
	}

	@SuppressWarnings("static-access")
	public static void add(String firstArgument, String secondArgument) { // TODO
		kernel.writeToMemory(firstArgument, Integer.parseInt((String) kernel.readFromMemory(firstArgument))
				+ Integer.parseInt((String) kernel.readFromMemory(secondArgument)));
	}

	@SuppressWarnings("static-access")
	public static void tester(Vector<String> vector) throws IOException {
		String operation = vector.get(0).trim();
		switch (operation) {
		case "assign":
			if (((String) vector.get(2).trim()).equals("readFile")) {
				assign((String) vector.get(1).trim(),
						(String) vector.get(2).trim() + " " + (String) vector.get(3).trim());
			} else {
				assign((String) vector.get(1).trim(), (String) vector.get(2).trim());
			}
			break;
		case "add":
			add((String) (vector.get(1).trim()), (String) (vector.get(2).trim()));
			break;
		case "readFile":
			System.out.println(kernel.readFile((String) (vector.get(1))));
			break;
		case "writeFile":
			//String[] access = vector.get(1).split(" ");
			int lo = Integer.parseInt(kernel.memory._memory[kernel.index("LowerBoundary", kernel.processID)] + "");
			int high = Integer.parseInt(kernel.memory._memory[kernel.index("UpperBoundary", kernel.processID)] + "");
			boolean flag0 = false;
			boolean flag1 = false;
			while (lo <= high) {
				if (kernel.memory._memory[lo] instanceof Pair) {
					if (((Pair) kernel.memory._memory[lo]).name.equals(vector.get(1).trim())) {
						flag1 = true;
					}
				}
				lo++;
			}
			if (flag1) {
				kernel.writeFile(kernel.readFromMemory(vector.get(1).trim()) + "",kernel.readFromMemory(vector.get(2).trim())+"");
			}
			break;
		case "print":
			kernel.print(vector.get(1));
			break;
		default:
			break;
		}

	}

	public static void Parser(StringBuilder sb) throws IOException {
		String[] array2 = sb.toString().split("\n");
		Vector<String> temp2 = new Vector<String>();
		Vector<String> temp = new Vector<String>();

		for (int o = 0; o < array2.length; o++) {
			temp2.add(array2[o]);
		}
		_command = temp2;
		for (int i = 0; i < _command.size(); i++) {
			instruction = _command.get(i).split(" ");
			boolean b = false;

			for (int j = 0; j < instruction.length; j++) {

				if ((instruction[0].equals("readFile") || instruction[0].equals("writeFile")) && j == 1) {
					String s = instruction[1] + " " + instruction[2];
					temp.add(s);
					b = true;
					continue;
				}
				if (j == 2 && b) {
					continue;
				} else {
					temp.add(instruction[j]);
				}
			}
			tester(temp);
			temp.clear();
		}
	}

	public static void main(String[] args) throws IOException {
		_codeParser codeparser = new _codeParser();
		kernel.longTermScheduler();
	}
}

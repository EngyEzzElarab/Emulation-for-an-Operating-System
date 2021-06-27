public class PCB {	
	
	int processID = 0;
	boolean processState; // Running or Not Running
	boolean finished = false;
	int PC = 0;
	int lowerBoundary;
	int upperBoundary;
	
	public PCB(int processID) {
		processState = false;
		this.processID = processID;
		
	}
	
}

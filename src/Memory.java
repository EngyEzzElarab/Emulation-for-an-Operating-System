public class Memory {
	
	boolean isFull;
	int sofar = 15;
	Object[] _memory;

	public Memory() {
		_memory = new Object[900];
		isFull = false;

	}

}

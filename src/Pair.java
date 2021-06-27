public class Pair {

	String name;
	Object value;

	public Pair(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String toString()
	{
		return name + " " + value;
	}
}

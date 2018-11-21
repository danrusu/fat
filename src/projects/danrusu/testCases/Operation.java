package projects.danrusu.testCases;

public enum Operation {

	SUM(1),

	MULTIPLICATION(2),

	DIVISION(3);

	Operation(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return value;
	}
}
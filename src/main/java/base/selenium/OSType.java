package main.java.base.selenium;

public enum OSType {

	WINDOWS,
	LINUX,
	MAC;
	

	public static final String CURRENT_OS = System.getProperty("os.name");


	private static boolean is(OSType osType) {

		return CURRENT_OS.toLowerCase().contains(
				osType.toString());
	}

	public static boolean isLinux() {

		return is(LINUX);
	}


	public static boolean isWindows() {

		return is(WINDOWS);
	}


	public static boolean isMac() {

		return is(MAC);
	}
	
	
	public static String getCurrentOs() {
		if (isLinux()) {
			return LINUX.toString();
		}
		else if (isWindows()) {
			return WINDOWS.toString();
		}
		else if (isMac()) {
			return MAC.toString();
		}
		else {
			throw new RuntimeException("Unknown OS.");
		}
	}
	
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}

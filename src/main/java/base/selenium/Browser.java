package main.java.base.selenium;

public enum Browser{

	CHROME("chromedriver.exe"),

	FIREFOX("geckodriver.exe"),

	IE("IEDriverServer.exe");

	private Browser(String driverExecutable) {

		this.driverExecutable = driverExecutable;            
	}


	private String driverExecutable; 


	public String getDriverExecutableName() {

		if(OSType.isWindows()) {
			return driverExecutable;
		}
		else if (OSType.isLinux()) {
			if (this.equals(IE)) {
				throw new RuntimeException("IE not supported for Linux.");
			}
			return driverExecutable.replaceAll(".exe", "");
		}
		
		throw new RuntimeException("No driver for this OS.");
	}
}
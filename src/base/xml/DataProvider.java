package base.xml;

import static base.Logger.log;
import static base.failures.ThrowablesWrapper.assignUnchecked;
import static utils.StringUtils.splitByAndTrim;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataProvider {

	
	public static Predicate<String> lineIsNotEmpty = line -> false == line.matches("^\\s*\\n*");

	
	public static Predicate<String> lineIsNotCommented = line -> false == line.matches("^\\s*//.*");

	
	public static List<List<String>> getData(Path filePath, String separatorRegex, int dataLength) {

		List<String> filteredData = assignUnchecked(() -> Files.readAllLines(filePath).stream()

				.filter(lineIsNotEmpty)

				.filter(lineIsNotCommented)

				.collect(Collectors.toList()), Collections.emptyList());
		

		List<List<String>> data = filteredData.stream()

				.map(line -> splitByAndTrim(line, separatorRegex, dataLength))

				.collect(Collectors.toList());

		return data;
	}

	
	public static int getDataLength(String localFilePath) {

		if (localFilePath != null) {

			log("Data provider file: " + localFilePath);

			int dataLength = assignUnchecked(countFileLines(localFilePath), 0);

			log("Data provider length: " + dataLength);

			return dataLength;

		}

		return 0;
	}

	private static Callable<Integer> countFileLines(String localFilePath) {

		return () -> Files.readAllLines(Paths.get(System.getProperty("user.dir"), (localFilePath)))

				.stream()

				.filter(lineIsNotEmpty)

				.filter(lineIsNotCommented)

				.collect(Collectors.counting())

				.intValue();
	}

}

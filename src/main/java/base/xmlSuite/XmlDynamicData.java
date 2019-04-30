package main.java.base.xmlSuite;

import static java.lang.String.format;
import static main.java.base.Logger.debug;
import static main.java.base.Logger.getLogDirPath;
import static main.java.base.Logger.log;
import static main.java.base.xmlSuite.DSLFunction.$afterNumberOfDays;
import static main.java.base.xmlSuite.DSLFunction.$dataProvider;
import static main.java.base.xmlSuite.DSLFunction.$property;
import static main.java.utils.StringUtils.removeZeroPrefixFromIntegers;
import static main.java.utils.TimeUtils.formatCurrentDate;
import static main.java.utils.TimeUtils.formatNextDate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static java.util.stream.Collectors.*;
import static main.java.base.xmlSuite.DSLValue.*;
import static main.java.base.xmlSuite.DSLFunction.*;

import main.java.base.failures.Failure;
import main.java.base.failures.ThrowablesWrapper;
import main.java.utils.StringUtils;

public class XmlDynamicData {


	private static Map<String, String> savedDataMap = new TreeMap<>();


	public static List<String> getRawTokens(String attributeValue) {

		int currentIndex = 0;
		List<String> variables = new ArrayList<>();

		while(currentIndex < attributeValue.length()) {

			int varBeginIndex = attributeValue.indexOf("{", currentIndex); 

			if (varBeginIndex >= 0) {

				int varEndIndex = attributeValue.indexOf("}", currentIndex+1);

				if (varEndIndex >= 0) {

					int nextVarBeginIndex = attributeValue.indexOf("{", varBeginIndex + 1);

					if (nextVarBeginIndex!=-1 && nextVarBeginIndex < varEndIndex) {

						currentIndex = nextVarBeginIndex;
						continue;
					}

					variables.add(attributeValue.substring(varBeginIndex, varEndIndex+1)); 

					currentIndex = varEndIndex + 1;
				}

				else {
					break;
				}
			}

			else {
				currentIndex++;
			}
		}

		return variables;
	}



	public static String evaluateAttributeToken (
			Path dataProviderFile,
			String dataProvidedIndex, 
			String token){

		// XmlDynamicValue
		if (isDSLValue(token) || isDSLFunction(token)){

			return evalDSL(dataProviderFile, dataProvidedIndex, token);
		}

		// token is a saved variable name
		else {

			// XML:  requiredOrderNumber="{orderNumbers[1]}" 
			String arrayVariablePattern = "^(\\w+)\\[(\\d+)\\]$";
			if (token.matches(arrayVariablePattern)) {

				return evaluateArrayToken(token, arrayVariablePattern);
			}


			// XML:  requiredOrderNumber="{orderNumber}" 
			// String variable like "orderNumber"
			if (savedDataMap.get(token) != null){

				return savedDataMap.get(token);
			}

			// return unmodified token
			else {

				return token;
			}
		}         
	}


	private static String evaluateArrayToken(
			String token, 
			String arrayVariablePattern) {

		String arrayVariableName = token.replaceAll(arrayVariablePattern, "$1");
		int index = Integer.parseInt(token.replaceAll(arrayVariablePattern, "$2"));

		String savedData = savedDataMap.get(arrayVariableName);

		String evaluatedToken = ThrowablesWrapper.supplyAndMapThrowableToFailure(

				() -> {

					if ( savedData != null){

						return savedData.split("\\|\\|")[index]; 
					}
					else {

						return token;
					}
				});

		return evaluatedToken;
	}


	public static String evaluateAttributeValue(
			Path dataProviderFile,
			String dataProvidedIndex, 
			String attributeValue) {

		List<String> rawTokens = getRawTokens(attributeValue);

		List<String> evaluatedTokens = rawTokens.stream()

				.map(token -> token.replaceAll("[\\{\\}]", ""))

				.map(token -> evaluateAttributeToken(dataProviderFile, dataProvidedIndex, token))

				.collect(toList());

		// replace all raw tokens in the attribute value with evaluated values 
		String evaluatedAttributeValue = attributeValue;
		for(int i=0; i < evaluatedTokens.size(); i++) {

			evaluatedAttributeValue = evaluatedAttributeValue.replace(
					rawTokens.get(i), 
					evaluatedTokens.get(i));
		}

		return evaluatedAttributeValue;
	}
	
	
	public static String evaluateAttributeValue(String attributeValue) {
		
		return evaluateAttributeValue(null, null, attributeValue);
	}


	private static String evalDSL(
			Path dataProviderFile,
			String dataProvidedIndex, 
			String rawValue) {

		// dynamic values
		if (isDSLValue(rawValue)){

			switch (DSLValue.valueOf(rawValue)){

			case $userDir:

				return System.getProperty("user.dir");

			case $logFolder:

				return getLogDirPath().toString();


				// current time
			case $HHmmss:

				return formatCurrentDate("HHmmss");

			case $HH:

				return formatCurrentDate("HH");

			case $mm:

				return formatCurrentDate("mm"); 

			case $ss:

				return formatCurrentDate("ss"); 


				// current date
			case $ddMMyyyy:

				return formatCurrentDate("ddMMyyyy"); 


			case $yyyy:

				return formatCurrentDate("yyyy");

			case $MM:

				return formatCurrentDate("MM");

			case $dd:

				return formatCurrentDate("dd"); 

			case $M:

				return removeZeroPrefixFromIntegers(
						formatCurrentDate("M"));

			case $d:

				return removeZeroPrefixFromIntegers(
						formatCurrentDate("d")); 	


				// tomorrow 
			case $tomorrow_dd:

				return formatNextDate("dd", "1"); 

			case $tomorrow_MM:

				return formatNextDate("MM", "1"); 

			case $tomorrow_yyyy:

				return formatNextDate("yyyy", "1");

			case $tomorrow_M:

				return removeZeroPrefixFromIntegers(
						formatNextDate("M", "1"));

			case $tomorrow_d:

				return removeZeroPrefixFromIntegers(
						formatNextDate("d", "1"));


			default:
				break;
			}	
		}


		// DSL functions
		if (isDSLFunction(rawValue)) {

			DSLFunction dslFunction = getDSLFunction(rawValue);

			log("DSL function: " + dslFunction.name());


			// TODO - clean this
			switch(dslFunction) {

			case $afterNumberOfDays: return replaceDayAfter(rawValue);

			case $dataProvider: return getProvidedDataOrRaw(
					dataProviderFile, 
					dataProvidedIndex, 
					rawValue);

			case $property: return getProperty(rawValue);
			}		

		}

		return rawValue;
	}


	private static String getProvidedDataOrRaw(Path dataProviderFile, String dataProvidedIndex, String rawValue) {

		if (dataProvidedIndex != null) {

			return getProvidedData(
					dataProviderFile,
					dataProvidedIndex,
					rawValue.replaceAll(
							$dataProvider.getDslRegexp(), 
							"$1"));                    
		}
		else {
			return rawValue;
		}
	}


	private static String getProvidedData(
			Path dataProviderFilePath,
			String rowIndex, 
			String columnIndex) {

		return DataProvider.getDataFromProvider(dataProviderFilePath, ",\\s*", 10)

				.get(StringUtils.toInt(rowIndex, 0))

				.get(StringUtils.toInt(columnIndex, 0));
	}


	/*i.e. - current date = 05.03.2018   
    after5days_dd -> 05
    after5days_MM -> 04
    after5days_yyyy -> 2018
    after5days_d -> 5
    after5days_M -> 4
	 */
	private static String  replaceDayAfter(String value) {

		String dayAfterRegExp = $afterNumberOfDays.getDslRegexp();

		if(value.matches(dayAfterRegExp)) { 

			String dateFormat = value.replaceAll(
					dayAfterRegExp, 
					"${dateFormat}");

			String nextFormattedDate = formatNextDate(
					dateFormat, 
					value.replaceAll(dayAfterRegExp, "${daysDelay}"));

			return dateFormat.length() == 1 ? 
					removeZeroPrefixFromIntegers(nextFormattedDate) :                                        
						nextFormattedDate;
		}

		return value;
	}


	private static String getProperty(String rawValue) {

		String propertyName = rawValue.replaceAll(
				$property.getDslRegexp(), 
				"$1");

		String propertyValue = System.getProperty(propertyName);
		if (propertyValue == null) {
		  throw new Failure(
				  format("Missing property: \"%s\"", propertyName));	
		}
		
		return propertyValue;
	}


	public static Map<String, String> getSavedData() {

		debug("Get current saved data: " + savedDataMap);
		return savedDataMap;
	}


	public static void saveData(String key, String value) {

		savedDataMap.put(key, value);
		log("Saved data: " + key + "=" + savedDataMap.get(key));
	}


	public static void resetSavedData() {

		savedDataMap = new TreeMap<>();
		debug("Current saved data: " + savedDataMap);
	}



	// "name1=value1;name2=value2" -> Map { name1: value1, name2:value2 }
	public static Map<String, String> getMapFromSaveString(String saveString){

		return List.of(saveString.split(";")).stream()

				.map(StringUtils::splitByEquals)

				.collect(toMap(

						nameAndValueList-> nameAndValueList.get(0),

						nameAndValueList -> nameAndValueList.get(1)));
	}

}


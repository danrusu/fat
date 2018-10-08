package base.xml;

import static base.Logger.debug;
import static base.Logger.getLogDirPath;
import static base.Logger.log;
import static utils.StringUtils.removeZeroPrefixFromIntegers;
import static utils.TimeUtils.formatCurrentDate;
import static utils.TimeUtils.formatNextDate;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import base.failures.ThrowablesWrapper;
import utils.StringDataProvider;
import utils.StringUtils;

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



    public static String evaluateAttributeToken (String dataProvidedIndex, String token){

        // XmlDynamicValue
        if ( DSLValue.contains(token) ||

                DSLFunction.matchesRegExp(token)){

            return evalDSL(dataProvidedIndex, token);
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

        String evaluatedToken = ThrowablesWrapper.wrapThrowable(

                () -> {

                    if ( savedData != null){

                        return savedData.split("\\|\\|")[index]; 
                    }
                    else {

                        return token;
                    }
                }
                );

        return evaluatedToken;
    }



    public static String evaluateAttributeValue(String dataProvidedIndex, String attributeValue) {

        List<String> rawTokens = getRawTokens(attributeValue);

        List<String> evaluatedTokens = rawTokens.stream()

                .map(token -> token.replaceAll("[\\{\\}]", ""))

                .map(token -> XmlDynamicData.evaluateAttributeToken(dataProvidedIndex, token))

                .collect(Collectors.toList());

        // replace all raw tokens in the attribute value with evaluated values 
        String evaluatedAttributeValue = attributeValue;
        for(int i=0; i < evaluatedTokens.size(); i++) {

            evaluatedAttributeValue = evaluatedAttributeValue.replace(
                    rawTokens.get(i), 
                    evaluatedTokens.get(i));
        }

        return evaluatedAttributeValue;
    }



    private static String evalDSL(String dataProvidedIndex, String value) {

        // dynamic values
        if (DSLValue.contains(value)){

            switch (DSLValue.valueOf(value)){

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


        if (DSLFunction.matchesRegExp(value)) {

            String dslFunctionName = DSLFunction.getName(value);

            log("DSL function: " + dslFunctionName);

            // DSL functions
            if (dslFunctionName == DSLFunction.$afterNumberOfDays.name()) {

                return replaceDayAfter(value);                    
            }

            if ( dataProvidedIndex != null) {
                if (dslFunctionName == DSLFunction.$dataProvider.name()) {

                    return getProvidedData(
                            dataProvidedIndex,
                            value.replaceAll(DSLFunction.$dataProvider.getValue(), "$1"));                    
                }
            }
        }

        return value;
    }



    private static String getProvidedData(String rowIndex, String columnIndex) {

        return StringDataProvider.readDataProviderFile(                
                Paths.get(System.getProperty("user.dir"), "dataProviders", "mockUsers.txt"), 
                ", ",
                2)

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

        String dayAfterRegExp = DSLFunction.$afterNumberOfDays.getValue();

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

                .collect(Collectors.toMap(

                        nameAndValueList-> nameAndValueList.get(0),

                        nameAndValueList -> nameAndValueList.get(1)));
    }

}


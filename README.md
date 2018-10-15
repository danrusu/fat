# fat
Framework for Automation in Testing


### Environment
 - Java 10+
 - Eclipse IDE
 - Ant
 

### Build runnable jar
```
ant -f fatAnt.xml
```


### Run test scenarios with the JAR tool
```
cd fat
java -jar fat.jar XML\Demo\browsers.xml
```


### Framework functionalities demos (scenarios in XML\Demo\, result in logs\)
 - browsers.xml, log_Demo_browsers
 - browsersDataDriven.xml, log_Demo_browsersDataDriven
 - exceptions.xml, log_Demo_exceptions
 - XmlDynamicData.xml, log_Demo_XmlDynamicData
 - dataProviderUsers.xml, log_Demo_dataProviderUsers
 - screenshotsCompare.xml, log_Demo_screenshotsCompare
 


### Logs (logs\log__timeStamp)
 - Detailed Java log: log.txt
 - Test scenario execution report: result.html
 - failure screenshots

# fat
### Framework for Automation in Testing


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


### Framework functionalities demos (scenarios and reports)
 - [browsers.xml](XML/Demo/browserss.xml), [log_Demo_browsers](logs/log_Demo_browsers)
 - [browsersDataDriven.xml](XML/Demo/browsersDataDriven.xml), [log_Demo_browsersDataDriven](logs/log_Demo_browsersDataDriven)
 - [exceptions.xml](XML/Demo/exceptions.xml), [log_Demo_exceptions](logs/log_Demo_exceptions)
 - [XmlDynamicData.xml](XML/Demo/XmlDynamicData.xml), [log_Demo_XmlDynamicData](logs/log_Demo_XmlDynamicData)
 - [dataProviderUsers.xml](XML/Demo/dataProviderUsers.xml), [log_Demo_dataProviderUsers](logs/log_Demo_dataProviderUsers)
 - [screenshotsCompare.xml](XML/Demo/screenshotsCompare.xml), [log_Demo_screenshotsCompare](logs/log_Demo_screenshotsCompare)
 


### Logs (logs\log__timeStamp)
 - Detailed Java log: log.txt
 - Test scenario execution report: result.html
 - failure screenshots

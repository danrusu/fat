# fat
### Framework for Automation in Testing

#### 0. Clone 'fat'
```
git clone https://github.com/danrusu/fat.git
```

#### 1. Environment
 - [Git](https://git-scm.com/downloads)
 - [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
 - [Eclipse IDE](https://www.eclipse.org/downloads/)
 - [Apache Ant](https://ant.apache.org/bindownload.cgi)
 

#### 2. Pull updates and build runnable fat.jar
```
cd fat
ant
```


#### 3. Run test scenarios with the JAR tool
```
cd fat
java -jar fat.jar XML\Demo\browsers.xml
```


#### 4. Framework functionalities demos ([scenarios](XML/Demo) and [reports](logs))
 - [browsers.xml](XML/Demo/browserss.xml), [log_Demo_browsers](logs/log_Demo_browsers)
 - [browsersDataDriven.xml](XML/Demo/browsersDataDriven.xml), [log_Demo_browsersDataDriven](logs/log_Demo_browsersDataDriven)
 - [exceptions.xml](XML/Demo/exceptions.xml), [log_Demo_exceptions](logs/log_Demo_exceptions)
 - [XmlDynamicData.xml](XML/Demo/XmlDynamicData.xml), [log_Demo_XmlDynamicData](logs/log_Demo_XmlDynamicData)
 - [dataProviderUsers.xml](XML/Demo/dataProviderUsers.xml), [log_Demo_dataProviderUsers](logs/log_Demo_dataProviderUsers)
 - [screenshotsCompare.xml](XML/Demo/screenshotsCompare.xml), [log_Demo_screenshotsCompare](logs/log_Demo_screenshotsCompare)
 


#### 5. [Logs](logs)
 - detailed Java log: log.txt
 - test scenario execution report: result.html
 - failure screenshots


#### 6. [Selenium drivers](http://www.webdriverjs.com/webdriverjs/)

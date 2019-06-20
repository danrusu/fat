# [Framework for Automation in Testing](https://github.com/danrusu/fat)

### Contact
- Email: danginkgo@yahoo.com
- [LinkedIn](https://www.linkedin.com/in/rusu-dan)

### [Slack workspace](https://ait-ro-fat.slack.com)

#### Tested on Windows and Ubuntu.

#### The project contains [web drivers](https://github.com/danrusu/fat/tree/master/webDrivers) for Windows (Chrome, Firefox, IE), Ubuntu (Chrome, Firefox), and MacOS (Chrome, Firefox).


#### 0. Clone 'fat'

```
git clone https://github.com/danrusu/fat.git
cd fat
```


#### 1. Environment
 - [Git](https://git-scm.com/downloads)
 - [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
 - [Eclipse IDE 2018-12](https://www.eclipse.org/eclipseide/2018-12/)
 - [Gradle](https://gradle.org/) / [Ant](https://ant.apache.org/bindownload.cgi)
 
 All binaries locations for Git, Java and Ant / Gradle, must be added to the system path.
 

#### 2. Updates and build fat.jar

- Gradle build:


```
gradle clean jar
```

- Ant build:

```
ant
```


#### 3. Run a test scenario

```
java -jar fat.jar XML\demo\simpleTest.xml
```


#### 5. Framework functionalities demos ([scenarios](XML/demo) and [reports](logs))
 
 - [Simple test](XML/demo/simpleTest.xml), 
 [Report](http://danrusu.ro/logs/log_demo_simpleTest/result.html)
 
 
 - [Data driven simple test](XML/demo/simpleDataProvider.xml),
 [Data provider 1](resources/dataProviders/demo/mockUsers.txt), 
 [Data provider 2](resources/dataProviders/demo/mockFamilyUsers.txt), 
 [Report](http://danrusu.ro/logs/log_demo_simpleDataProvider/result.html)


 - [Properties](XML/demo/properties.xml),
 [Properties file 1](resources/properties/demo/test.properties), 
 [Properties file 2](resources/properties/demo/dynamic.properties), 
 [Report](http://danrusu.ro/logs/log_demo_properties/result.html) 
 
 
 - [Exceptions](XML/demo/exceptions.xml), 
 [Report](http://danrusu.ro/logs/log_demo_exceptions/result.html)
 
 
 - [XML dynamic data capabilities](XML/demo/XmlDynamicData.xml), 
 [Report](http://danrusu.ro/logs/log_demo_XmlDynamicData/result.html)
 
 
  - [Multiple browsers](XML/demo/browsers.xml), 
 [Report](http://danrusu.ro/logs/log_demo_browsers/result.html)
 
 
 - [Multiple browsers data driven](XML/demo/browsersDD.xml),
 [Data provider](resources/dataProviders/demo/browsers),
 [Report](http://danrusu.ro/logs/log_demo_browsersDD/result.html)
 
 
 - [UI Test Calculate](XML/danrusu/uiTestCalculate.xml),
 [Report](http://danrusu.ro/logs/log_danrusu_uiTestCalculate/result.html)
 
 
  - [API Test Calculate](XML/danrusu/apiTestCalculate.xml),
 [Report](http://danrusu.ro/logs/log_danrusu_apiTestCalculate/result.html)


  - [API Test Calculate Data Driven](XML/danrusu/apiTestCalculateDD.xml),
 [Data Provider](resources/dataProviders/danrusu/calculate.txt), 
 [Report](http://danrusu.ro/logs/log_danrusu_apiTestCalculateDD/result.html)

 
  - [API Test JSON](XML/danrusu/apiTestJson.xml),
 [Report](http://danrusu.ro/logs/log_danrusu_apiTestJson/result.html)
 

#### 6. [Logging/Reporting](logs)

 - detailed log: log.txt
 
 - test scenario execution report: result.html
 
 - JUnit XML report: result.xml


#### 7. [Selenium drivers](http://www.webdriverjs.com/webdriverjs/)


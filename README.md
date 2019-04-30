# Framework for Automation in Testing


#### 0. Clone 'fat'
```
git clone https://github.com/danrusu/fat.git
```


#### 1. Environment
 - [Git](https://git-scm.com/downloads)
 - [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
 - [Eclipse IDE 2018-12](https://www.eclipse.org/eclipseide/2018-12/)
 - [Apache Ant](https://ant.apache.org/bindownload.cgi)
 

#### 2. Updates and build fat.jar

- Ant build:

```
cd bred-automation-test
ant
```


#### 3. Run test scenarios

```
cd bred-automation-test
java -jar fat.jar XML\Demo\simpleTest.xml
```


#### 5. Framework functionalities demos ([scenarios](XML/Demo) and [reports](logs))
 
 - [Simple test](XML/Demo/simpleTest.xml), 
 [Report](logs/log_Demo_simpleTest)
 
 
 - [Data driven simple test](XML/Demo/simpleDataProvider.xml),
 [Data provider 1](resources/dataProviders/Demo/mockUsers.txt), 
 [Data provider 2](resources/dataProviders/Demo/mockFamilyUsers.txt), 
 [Report](logs/log_Demo_simpleDataProvider)


 - [Properties](XML/Demo/properties.xml),
 [Properties file 1](resources/properties/Demo/test.properties), 
 [Properties file 2](resources/properties/Demo/dynamic.properties), 
 [Report](logs/log_Demo_properties) 
 
 
 - [Multiple browsers](XML/Demo/browsers.xml), 
 [Report](logs/log_Demo_browsers)
 
 
 - [Multiple browsers data driven](XML/Demo/browsersDD.xml),
 [Data provider](resources/dataProviders/Demo/browsers),
 [Report](logs/log_Demo_browsers_DD)
 
 
 - [Exceptions](XML/Demo/exceptions.xml), 
 [Report](logs/log_Demo_exceptions)
 
 
 - [XML dynamic data capabilities](XML/Demo/XmlDynamicData.xml), 
 [Report](logs/log_Demo_XmlDynamicData)
 
 
 - [Screenshots Compare](XML/Demo/screenshotsCompare.xml), 
 [Report](logs/log_Demo_screenshotsCompare)
 
 
 - [UI Test Calculate](XML/danrusu/uiTestCalculate.xml),
 [Report](logs/log_danrusu_uiTest_Calculate)
 
 
  - [API Test Calculate](XML/danrusu/apiTestCalculate.xml),
 [Report](logs/log_danrusu_apiTest_Calculate)


  - [API Test Calculate Data Driven](XML/danrusu/apiTestCalculateDD.xml),
 [Data Provider](resources/dataProviders/danrusu/calculate.txt), 
 [Report](logs/log_danrusu_apiTest_Calculate_DD)

 
  - [API Test JSON](XML/danrusu/apiTestLotto.xml),
 [Report](logs/log_danrusu_apiTest_Lotto)
 

#### 6. [Logs/log_timeStamp](logs)

 - detailed Java log: log.txt
 
 - test scenario execution report: result.html
 
 - JUnit XML report: result.xml


#### 7. [Selenium drivers](http://www.webdriverjs.com/webdriverjs/)


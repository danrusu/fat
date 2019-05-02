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
cd fat
ant
```


#### 3. Run test scenarios

```
cd fat
java -jar fat.jar XML\demo\simpleTest.xml
```


#### 5. Framework functionalities demos ([scenarios](XML/demo) and [reports](logs))
 
 - [Simple test](XML/demo/simpleTest.xml), 
 [Report](logs/log_demo_simpleTest)
 
 
 - [Data driven simple test](XML/demo/simpleDataProvider.xml),
 [Data provider 1](resources/dataProviders/demo/mockUsers.txt), 
 [Data provider 2](resources/dataProviders/demo/mockFamilyUsers.txt), 
 [Report](logs/log_demo_simpleDataProvider)


 - [Properties](XML/demo/properties.xml),
 [Properties file 1](resources/properties/demo/test.properties), 
 [Properties file 2](resources/properties/demo/dynamic.properties), 
 [Report](logs/log_demo_properties) 
 
 
 - [Exceptions](XML/demo/exceptions.xml), 
 [Report](logs/log_demo_exceptions)
 
 
 - [XML dynamic data capabilities](XML/demo/XmlDynamicData.xml), 
 [Report](logs/log_demo_XmlDynamicData)
 
 
  - [Multiple browsers](XML/demo/browsers.xml), 
 [Report](logs/log_demo_browsers)
 
 
 - [Multiple browsers data driven](XML/demo/browsersDD.xml),
 [Data provider](resources/dataProviders/demo/browsers),
 [Report](logs/log_demo_browsersDD)
 
 
 - [UI Test Calculate](XML/danrusu/uiTestCalculate.xml),
 [Report](logs/log_danrusu_uiTestCalculate)
 
 
  - [API Test Calculate](XML/danrusu/apiTestCalculate.xml),
 [Report](logs/log_danrusu_apiTestCalculate)


  - [API Test Calculate Data Driven](XML/danrusu/apiTestCalculateDD.xml),
 [Data Provider](resources/dataProviders/danrusu/calculate.txt), 
 [Report](logs/log_danrusu_apiTestCalculateDD)

 
  - [API Test JSON](XML/danrusu/apiTestJson.xml),
 [Report](logs/log_danrusu_apiTestJson)
 

#### 6. [Logs/log_timeStamp](logs)

 - detailed Java log: log.txt
 
 - test scenario execution report: result.html
 
 - JUnit XML report: result.xml


#### 7. [Selenium drivers](http://www.webdriverjs.com/webdriverjs/)


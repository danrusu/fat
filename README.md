# bred-automation-test
### Test automation tool for BrED project


#### 0. Clone 'bred-automation-test'
```
git clone https://danrusu1111@bitbucket.org/brivoinc/bred-automation-test.git
```


#### 1. Environment
 - [Git](https://git-scm.com/downloads)
 - [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
 - [Eclipse IDE 2018-12](https://www.eclipse.org/eclipseide/2018-12/)
 - [Apache Ant](https://ant.apache.org/bindownload.cgi)
 - [Gradle](https://gradle.org)
 

#### 2. Updates and build fat.jar

- Ant build:

```
cd bred-automation-test
ant
```

- Gradle build:

```
cd bred-automation-test
gradle clean jar
```


#### 3. Run test scenarios

```
cd bred-automation-test
java -jar fat.jar XML\Demo\simpleTest.xml
```

- Gradle run:

```
cd bred-automation-test
gradlew testDemoSimpleTest
gradlew testDemoProperties --args="-Dnumber1=100 -DpropertiesFile=resources/properties/demo/dynamic.properties"
```

- List all available Gradle tasks:

```
gradlew tasks --all
```



#### 4. Demo-videos
 - [Project startup](https://drive.google.com/file/d/1R6qX43AGYGor5f37308oMum7RiiHtH6J/view?usp=sharing)
 
 
 - [Create simple test](https://drive.google.com/file/d/1u1A3iHu0YeqSXPNKZdbVzZRlNv2Yy2yT/view?usp=sharing)


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
 
 
 - [Multiple browsers data driven](XML/Demo/browsers_DD.xml),
 [Data provider](resources/dataProviders/Demo/browsers),
 [Report](logs/log_Demo_browsers_DD)
 
 
 - [Exceptions](XML/Demo/exceptions.xml), 
 [Report](logs/log_Demo_exceptions)
 
 
 - [XML dynamic data capabilities](XML/Demo/XmlDynamicData.xml), 
 [Report](logs/log_Demo_XmlDynamicData)
 
 
 - [Screenshots Compare](XML/Demo/screenshotsCompare.xml), 
 [Report](logs/log_Demo_screenshotsCompare)
 
 
 - [UI Test Calculate](XML/danrusu/uiTest_Calculate.xml),
 [Report](logs/log_danrusu_uiTest_Calculate)
 
 
  - [API Test Calculate](XML/danrusu/apiTest_Calculate.xml),
 [Report](logs/log_danrusu_apiTest_Calculate)


  - [API Test Calculate Data Driven](XML/danrusu/apiTest_Calculate_DD.xml),
 [Data Provider](resources/dataProviders/danrusu/calculate.txt), 
 [Report](logs/log_danrusu_apiTest_Calculate_DD)

 
  - [API Test JSON](XML/danrusu/apiTest_Lotto.xml),
 [Report](logs/log_danrusu_apiTest_Lotto)
 

#### 6. [Logs/log_timeStamp](logs)

 - detailed Java log: log.txt
 
 - test scenario execution report: result.html
 
 - JUnit XML report: result.xml


#### 7. [Selenium drivers](http://www.webdriverjs.com/webdriverjs/)


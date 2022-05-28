# E2E Tests Project for TeamCity

## Description

Tests project has following structure:

![image](https://user-images.githubusercontent.com/25251139/170833504-4112ac9e-b53d-4802-a2c9-84a63ddd56be.png)

Tests implement the POM pattern: **page-related-classes** is stored in **src/test/java/pages** and **test-related-classes** contain only test logic. All **test-related-classes** have to extend **BaseClass** to implement some driver-related and other technical details. 

Tests use API methods to simplify some checks and speed up tests.

Tests will generate a log as they run. If there are any test failures, the tests will take a screenshot and attach it to the test report.

## Remote run

Tests are available for remote run from TeamCity instance that has already been pre-configured. The instance contains custom **TeamCity Agent** with **Google Chrome** and **Allure Commandline** for running tests. Dockerfile and docker-compose.yml are attached to project. Google Chrome will run in **"Headless"** mode during tests. Tests uses the same instance.

To run the tests click **"Run"** on **TeamCity Tests/Run E2E Tests**

To view the report open **"Allure Report"** tab after the build is complete.

![image](https://user-images.githubusercontent.com/25251139/170830972-93ae6856-d3e7-42be-86b0-443996bf17ad.png)

## Local run

For local run please follow these steps:

1. Clone current repository and open it with **Intellij idea**
2. Change **"mode"** property to **"localRun"** and **"pathToDriver"** to **your path to Chrome Driver** in **/src/main/resources/project.properties** file
3. Use **"mvn clean test"** command in terminal to run tests
4. Use **mvn allure:serve** command in terminal to generate Allure Report or use **Allure Commandline** with command **allure serve <path to the project**>/**target**/**allure-results**. This command generates a report in temporary folder from the data found in the provided path and then creates a local Jetty server instance, serves generated report and opens it in the default browser. 

See more information about installing allure commandline on https://docs.qameta.io/allure/#_installing_a_commandline

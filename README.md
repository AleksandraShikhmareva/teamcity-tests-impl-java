## **E2E Tests Project for TeamCity**

For the test project to work correctly, you need to set internal property **teamcity.csrf.paranoid=false** in _Administration | Diagnostics | 
Internal properties menu_ 

To generate Allure Report with local run use **mvn allure:serve** command in terminal or use **Allure Commandline**

See more information about installing allure commandline on https://docs.qameta.io/allure/#_installing_a_commandline

Command to generate report in Allure Command line: **allure serve <path to the project>/target/allure-results**

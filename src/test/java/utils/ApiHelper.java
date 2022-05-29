package utils;

import dataProvider.ConfigFileReader;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    private ConfigFileReader configFileReader;
    private static Logger logger = LoggerFactory.getLogger(ApiHelper.class);

    public ApiHelper() {
        configFileReader = new ConfigFileReader();
        RestAssured.baseURI = configFileReader.getProperty("url");
    }

    public String getStatusAfterBuildComplete(int buildId) {
        int pollingEfforts = 0;
        while (pollingEfforts < 5) {
            if (buildId != getLastBuildId()) {
                try {
                    Thread.sleep(15000);
                    logger.info("Requesting status of build with id " + buildId);
                    pollingEfforts++;
                } catch (InterruptedException e) {
                    logger.error(e.toString());
                }
            } else break;
        }
        if (pollingEfforts==5){
            throw new IllegalStateException("Build not found");
        }
        return getStatus(buildId);
    }

    @Step("Run build")
    public int runBuild(String buildConfId) {
        logger.info("Sending request to run build with configuration ID = " + buildConfId + " to " + RestAssured.baseURI);
        String requestBody = "{\n" +
                "\t\"buildType\": {\n" +
                "    \t\"id\": \"" + buildConfId + "\"\n" +
                "\t}\n" +
                "}";

        Response response = given()
                .auth()
                .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("app/rest/buildQueue")
                .then()
                .extract().response();
        logger.info("Build with Id = " + response.jsonPath().getInt("id") + " was run");
        return response.jsonPath().getInt("id");
    }

    public int getLastBuildId() {
        Response response = given()
                .auth()
                .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("app/rest/builds")
                .then()
                .extract().response();
        return response.jsonPath().getInt("build.id[0]");
    }

    public String getStatus(int buildId) {
        String status = null;
        logger.info("Sending request to get status of build with id=" + buildId + " to " + RestAssured.baseURI);
        Response response = given()
                .auth()
                .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("app/rest/builds?locator=id:" + buildId)
                .then()
                .extract().response();
        status = response.jsonPath().getString("build.status[0]");
        logger.info("Build  status is " + status);
        return status;
    }

    public void deleteProject(String... projectNames) {
        for (String projectName : projectNames) {
            logger.info("Sending request to delete project with name=" + projectName + " to " + RestAssured.baseURI);
            Response response = given()
                    .auth()
                    .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
                    .when()
                    .delete("httpAuth/app/rest/projects/" + projectName)
                    .then()
                    .extract().response();

            if (response.getStatusCode() == 204) {
                logger.info("Project was successfully deleted");
            }
            response.getStatusCode();
        }
    }
}

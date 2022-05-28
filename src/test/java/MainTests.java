import utils.ApiHelper;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.*;


public class MainTests extends BaseTest {
    private static final String PROJECT_NAME_1 = "Calculator";
    private static final String PROJECT_NAME_2 = "Calculator 2";
    private static final String PROJECT_ID_2 = "Calculator2";
    private static final String BRANCH_NAME = "refs/heads/main";
    private static final String BRANCH_NAME_2 = "refs/heads/new-feature-branch";
    private static final String BUILD_CONF_ID = "Calculator_Build";
    private static final String BUILD_CONF_ID_2 = "Calculator_RunTests";
    private static final String BUILD_CONF_ID_3 = "Calculator2_Build2";
    private static final String BUILD_NAME = "Build";
    private static final String BUILD_NAME_2 = "Run Tests";
    private static final String BUILD_NAME_3 = "Build 2";
    private static final String SUCCESS_CONNECTION_MESSAGE = "The connection to the VCS repository has been verified";
    private static final String PROJECT_URL_1 = "https://github.com/AleksandraShikhmareva/calculator.git";

    private MainPage mainPage;
    private LoginPage loginPage;
    private AdministrationPage administrationPage;
    private CreateItemPage createProjectPage;
    private CreateFromURLPage createProjectFromURLPage;
    private BuildConfigurationPage buildConfigurationPage;
    private BuildsPage buildsPage;
    private ProjectSettingsPage projectSettingsPage;
    private BuildInfoPage buildInfoPage;
    private ProjectInfoPage projectInfoPage;
 //   private ApiHelper apiHelper;

    @BeforeClass
    public void initiatePages() {
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        administrationPage = new AdministrationPage(driver);
        createProjectPage = new CreateItemPage(driver);
        createProjectFromURLPage = new CreateFromURLPage(driver);
        buildConfigurationPage = new BuildConfigurationPage(driver);
        buildsPage = new BuildsPage(driver);
        projectSettingsPage = new ProjectSettingsPage(driver);
        buildInfoPage = new BuildInfoPage(driver);
        projectInfoPage = new ProjectInfoPage(driver);
    }

    @BeforeClass
    public void login() {
        loginPage.login(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"));
    }

    @AfterClass
    public void deleteProject() {
        apiHelper.deleteProject(PROJECT_NAME_1, PROJECT_NAME_2);
    }

    @Description("Creating new project and build")
    @Test(testName = "Build project test", enabled = true, priority = 0)
    public void creatingBuildTest() {
        mainPage.openAdministrationLink();
        administrationPage.createProject();
        createProjectPage.addProjectFromRemoteRepository(PROJECT_URL_1);
        Assert.assertTrue(createProjectFromURLPage.checkSuccessConnectMessage().contains(SUCCESS_CONNECTION_MESSAGE));
        createProjectFromURLPage.addGeneralProjectProperties(BRANCH_NAME, PROJECT_NAME_1);
        buildConfigurationPage.openParametersTab()
                .addNewParameter("MAVEN_EXTRA_PARAMETERS", "-DskipTests=true")
                .addNewParameter("PROJECT_VERSION", "1.0")
                .addNewParameter("BUILD_VERSION", "%PROJECT_VERSION%.%build.number%")
                .openBuildStepsTab()
                .addNewBuildStep("Maven", "Set project version", "org.codehaus.mojo:versions-maven-plugin:2.7:set -DnewVersion=%BUILD_VERSION%", "")
                .addNewBuildStep("Maven", "Build", "clean package", "%MAVEN_EXTRA_PARAMETERS%")
                .goToConfigurationPage();
        int buildId = apiHelper.runBuild(BUILD_CONF_ID);
        Assert.assertEquals(apiHelper.waitForBuildFinishing(buildId), "SUCCESS");
        buildsPage.openBuildInfo(buildId);
        Assert.assertTrue(buildInfoPage.hasExpectedResult("Success"));
        buildInfoPage.openBuildLogTab();
        Assert.assertTrue(buildInfoPage.hasStringInLog("Build finished"));
        buildInfoPage.openParametersTab();
        Assert.assertTrue(buildInfoPage.hasParameterWithValue("BUILD_VERSION", "1.0.1"));
    }

    @Description("Creating a build chain with snapshot dependency")
    @Test(testName = "Create build chain", enabled = true, priority = 1, dependsOnMethods = {"creatingBuildTest"})
    public void createBuildChainTest() {
        mainPage.openProjectLink()
                .openProject(PROJECT_NAME_1)
                .editProjectSettings();
        projectSettingsPage.createBuildConfiguration();
        createProjectPage.addProjectFromRemoteRepository(PROJECT_URL_1);
        Assert.assertTrue(createProjectFromURLPage.checkSuccessConnectMessage().contains(SUCCESS_CONNECTION_MESSAGE));
        createProjectFromURLPage.addGeneralBuildProperties(BRANCH_NAME, BUILD_NAME_2)
                .selectUseThisInDialog();
        buildConfigurationPage.openBuildStepsTab()
                .addNewBuildStep("Maven", "Run Tests", "surefire:test", "")
                .openDependencyTab()
                .addNewSnapshotDependency(true, BUILD_NAME)
                .goToConfigurationPage();
        Assert.assertEquals(apiHelper.waitForBuildFinishing(apiHelper.runBuild(BUILD_CONF_ID_2)), "SUCCESS");
        mainPage.openProjectLink()
                .openProject(PROJECT_NAME_1);
        projectInfoPage.openBuildChainTab();
        Assert.assertTrue(projectInfoPage.hasAllBuildsInBuildChain());
    }

    @Description("Copy build configuration from project 1 to project 2")
    @Test(testName = "Copy configuration", enabled = true, priority = 2, dependsOnMethods = {"creatingBuildTest"})
    public void copyConfigurationTest() {
        mainPage.openAdministrationLink();
        administrationPage.createProject();
        createProjectPage.createProjectManually(PROJECT_NAME_2);
        mainPage.openProjectLink()
                .openProject(PROJECT_NAME_1)
                .editProjectSettings();
        projectSettingsPage.openBuildConfiguration(BUILD_NAME);
        buildConfigurationPage.copyConfiguration(PROJECT_NAME_2, BUILD_NAME_3);
        mainPage.openProjectLink()
                .openProject(PROJECT_NAME_2);
        projectInfoPage.editProjectSettings();
        projectSettingsPage.openBuildConfiguration(BUILD_NAME_3);
        buildConfigurationPage.openParametersTab()
                .editParameter("MAVEN_EXTRA_PARAMETERS", "")
                .goToConfigurationPage();
        int buildId = apiHelper.runBuild(BUILD_CONF_ID_3);
        Assert.assertEquals(apiHelper.waitForBuildFinishing(buildId), "SUCCESS");
        buildsPage.openBuildInfo(buildId);
        Assert.assertTrue(buildInfoPage.hasExpectedResult("Tests passed: 1"));
        buildInfoPage.openParametersTab();
        Assert.assertTrue(buildInfoPage.hasParameterWithValue("BUILD_VERSION", "1.0.3"));
    }
}

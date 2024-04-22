package in.reqres.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import in.reqres.config.ApiConfig;
import in.reqres.config.DriverConfig;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    @BeforeAll
    static void browserSettings() {
        System.setProperty("driver", System.getProperty("driver", "local"));
        DriverConfig driverConfig = ConfigFactory.create(DriverConfig.class);
        ApiConfig apiConfig = ConfigFactory.create(ApiConfig.class);
        RestAssured.baseURI = System.getProperty("baseUrl", apiConfig.reqresBaseUrl());
        RestAssured.basePath = System.getProperty("basePath", apiConfig.reqresBasePath());

        if (driverConfig.isRemote()) {
            Configuration.remote = System.getProperty("browserRemoteUrl", driverConfig.browserRemoteUrl());
        }
    }

    @BeforeEach
    void addLogger() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        RestAssured.config = RestAssuredConfig.newConfig().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation());
    }
}

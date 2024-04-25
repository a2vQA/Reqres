package in.reqres.tests;

import in.reqres.api.model.LoginModel;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static in.reqres.api.spec.ReqresApiSpec.basicRequestSpec;
import static in.reqres.api.spec.ReqresApiSpec.responseSpec200;
import static in.reqres.api.spec.ReqresApiSpec.responseSpec400;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Reqres API")
@Story("Авторизация")
@Feature("Работа с авторизацией")
@DisplayName("Работа с авторизацией")
@Tags({@Tag("smoke"), @Tag("login")})
public class LoginTests extends BaseTest {
    private final LoginModel loginModel = new LoginModel();
    private final String existingEmail = "eve.holt@reqres.in";
    private final String notExistingEmail = "guru@qa.test";
    private final String password = "cityslicka";

    @DisplayName("Проверка авторизации без пароля")
    @Owner("vvartemenko")
    @Tag("loginWithOutPassword")
    @Test
    public void loginWithOutPasswordTest() {
        loginModel.setEmail(existingEmail);

        Response response = step("Send login request without password", () -> given(basicRequestSpec)
                .body(loginModel)
                .when()
                .post("/login")
                .then()
                .spec(responseSpec400)
                .body(matchesJsonSchemaInClasspath("contracts/post/unsuccessful_WO_password_api__login.json"))
                .extract().response());

        step("Check response for correct error", () ->
                assertThat(response.path("error"), containsString("Missing password")));
    }

    @DisplayName("Проверка авторизации с несуществующим email")
    @Owner("vvartemenko")
    @Tag("loginWithNotExistingEmail")
    @Test
    public void loginWithNotExistingEmailTest() {
        loginModel.setEmail(notExistingEmail);
        loginModel.setPassword(password);

        Response response = step("Send login request", () -> given(basicRequestSpec)
                .body(loginModel)
                .when()
                .post("/login")
                .then()
                .spec(responseSpec400)
                .extract().response());

        step("Check response for correct error", () ->
                assertThat(response.path("error"), containsString("user not found")));
    }

    @DisplayName("Проверка успешной авторизации")
    @Owner("vvartemenko")
    @Tag("successLogin")
    @Test
    public void successLoginTest() {
        loginModel.setEmail(existingEmail);
        loginModel.setPassword(password);

        Response response = step("Send login request with existing user", () -> given(basicRequestSpec)
                .body(loginModel)
                .when()
                .post("/login")
                .then()
                .spec(responseSpec200)
                .extract().response());

        step("Check response for having token", () -> assertThat(response.path("token"), notNullValue()));
    }
}
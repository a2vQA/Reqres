package in.reqres.tests;

import in.reqres.api.model.UserModel;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static in.reqres.api.spec.ReqresApiSpec.basicRequestSpec;
import static in.reqres.api.spec.ReqresApiSpec.responseSpec200;
import static in.reqres.api.spec.ReqresApiSpec.responseSpec201;
import static in.reqres.api.spec.ReqresApiSpec.responseSpec204;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@Epic("Reqres API")
@Story("Пользователи")
@Feature("Работа с пользователями")
@DisplayName("Работа с пользователями")
@Tag("smoke")
public class UserTests extends BaseTest {
    private final UserModel userData = new UserModel();

    @DisplayName("Создание пользователя")
    @Owner("vvartemenko")
    @Tag("createUser")
    @Test
    public void createUserTest() {
        userData.setName("qa_guru");
        userData.setJob("student");

        Response response = step("Send user create request", () -> given(basicRequestSpec)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .spec(responseSpec201)
                .body(matchesJsonSchemaInClasspath("contracts/post/api__users.json"))
                .extract().response());

        step("Check response for correct Name and Job", () ->
                assertAll("Проверка данных созданного пользователя",
                        () -> assertThat(response.path("name"), equalTo("qa_guru")),
                        () -> assertThat(response.path("job"), equalTo("student"))));
    }

    @DisplayName("Изменение полей имени и работы пользователя")
    @Owner("vvartemenko")
    @Tag("changeUserInfo")
    @Test
    public void changeUserInfoTest() {
        userData.setName("qa_guru_changed");
        userData.setJob("job_changed");

        Response response = step("Send user change request", () -> given(basicRequestSpec)
                .body(userData)
                .when()
                .patch("users/2")
                .then()
                .spec(responseSpec200)
                .extract().response());

        step("Check response for correct changed data", () ->
                assertAll("Проверка измененных данных пользователя",
                        () -> assertThat(response.path("name"), equalTo("qa_guru_changed")),
                        () -> assertThat(response.path("job"), equalTo("job_changed"))));
    }

    @DisplayName("Проверка наличия пользователя в списке пользователей")
    @Owner("vvartemenko")
    @Tag("checkForUsernameNLastnameInList")
    @ParameterizedTest(name = ". Поиск пользователя {0} {1}")
    @CsvSource(value = {
            "Tobias , Funke",
            "Michael , Lawson",
            "Lindsay , Ferguson"
    })
    public void checkForUsernameNLastnameInListTest(String firstName, String lastName) {
        Response response = step("Send request for user list", () -> given(basicRequestSpec)
                .when()
                .get("/users?per_page=20")
                .then()
                .spec(responseSpec200)
                .body(matchesJsonSchemaInClasspath("contracts/get/api__users.json"))
                .extract().response());

        step("Check response for correct Name and Job", () ->
                assertAll("Наличие " + firstName + lastName + " в списке пользователей",
                        () -> assertThat(response.path("data").toString(), containsString(firstName)),
                        () -> assertThat(response.path("data").toString(), containsString(lastName))));
    }

    @DisplayName("Удаление пользователя")
    @Owner("vvartemenko")
    @Tag("deleteUser")
    @Test
    public void deleteUserTest() {
        step("Send user delete request", () -> given(basicRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(responseSpec204));
    }
}

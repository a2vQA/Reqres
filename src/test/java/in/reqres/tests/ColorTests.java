package in.reqres.tests;

import in.reqres.api.model.ColorModel;
import in.reqres.api.model.ColorWrapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static in.reqres.api.spec.ReqresApiSpec.basicRequestSpec;
import static in.reqres.api.spec.ReqresApiSpec.responseSpec200;
import static in.reqres.api.spec.ReqresApiSpec.responseSpec404;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@Epic("Reqres API")
@Story("Ресурс цветов")
@Feature("Работа со списками цветов")
@DisplayName("Работа со списками цветов")
@Tag("smoke")
public class ColorTests extends BaseTest {

    @Test
    @DisplayName("Проверка наличия цвета 'chili pepper' и его данных в списке цветов")
    public void checkForSpecificColorInListTest() {
        ColorWrapper response = step("Send request for colors list", () ->
                given(basicRequestSpec)
                .when()
                .get("/unknown?per_page=20")
                .then()
                .spec(responseSpec200)
                .body(matchesJsonSchemaInClasspath("contracts/get/api__unknown.json"))
                .extract()
                .as(ColorWrapper.class));

        List<ColorModel> colorsListWithParameters = response.getData();

        step("Check that color 7 is 'chili pepper' with all fields", () ->
        assertAll("Check 'chili pepper' color and its fields in list with id 7",
                () -> assertThat(colorsListWithParameters.get(7).getName(), equalTo("chili pepper")),
                () -> assertThat(colorsListWithParameters.get(7).getYear(), equalTo("2007")),
                () -> assertThat(colorsListWithParameters.get(7).getColor(), equalTo("#9B1B30")),
                () -> assertThat(colorsListWithParameters.get(7).getPantoneValue(), equalTo("19-1557"))));
    }

    @Test
    @DisplayName("Проверка ошибки 404 при несуществующем цвете")
    public void checkForNotExistingColorTest() {
        step("Send request to get color with not existing id", () -> given(basicRequestSpec)
                .when()
                .get("/unknown/43")
                .then()
                .spec(responseSpec404));
    }
}

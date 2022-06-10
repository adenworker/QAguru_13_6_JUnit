package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.util.Arrays.asList;

public class AvitoParametrizedTests {

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
    }
/*-------------------------------------------------------------------------------------------------------------------*/
    /*Простой тест с именем*/
    @DisplayName("Простая проверка открытия \"Avito\"")
    @Disabled
    @Test
    void displayNameTest() {
        Selenide.open("https://www.avito.ru/");
        $("[data-marker='header-navigation']").shouldHave(text("Недвижимость"), text("Работа"));
    }

/*-------------------------------------------------------------------------------------------------------------------*/
    /*Итерационный тест с 2 параметрами, который запускается последовательно 2 раза. Данные хранятся тут же в ValueSource
    Проверяет на наличие на новой странице тот же текст, что мы вводим в поисковую строку.*/
    @ValueSource(strings = {
            "Смартфон", "Пылесос"
    })
    @Disabled
    @ParameterizedTest(name = "При поиске \"{0}\" в выдаче есть \"{0}\"")
    void valueSourceParametrizedTest(String testDataValue) {
        Selenide.open("https://www.avito.ru/");
        $("[data-marker='search-form/suggest']").setValue(testDataValue).pressEnter();
        $("div [data-marker='item']").shouldHave(text(testDataValue)).shouldBe(visible);
    }

/*-------------------------------------------------------------------------------------------------------------------*/
    /*Итерационный тест с 2 параметрами, который запускается последовательно 2 раза. Данные хранятся в специальном
    Enum классе в виде констант.
    Проверяет на наличие на новой странице независимую от запроса строку (1 кейс - 1 Enum элемент).*/
    @EnumSource(EnumTestData.class)
    @Disabled
    @ParameterizedTest(name = "Проверка \"{0}\"")
    void enumTest(EnumTestData enumData) {
        Selenide.open("https://www.avito.ru/");
        $("[data-marker='search-form/suggest']").setValue(enumData.searchData).pressEnter();
        $("[data-marker='page-title/text']").shouldHave(text(enumData.result)).shouldBe(visible);
    }

/*-------------------------------------------------------------------------------------------------------------------*/
    /*Итерационный тест с 2 параметрами, один из которых - список, который запускается последовательно необходимое
    количество раз (зависит от объема тестовых данных). Данные хранятся в Stream<Arguments> контейнере.
    Проверяет на наличие на новой странице 2 независимых строк (1 кейс - 1 список строк).*/
    static Stream<Arguments> methodSourceComplexDataProvider() {
        return Stream.of(
                Arguments.of("Видеокарта", asList("Видеокарта", "Товары для компьютера")),
                Arguments.of("Мышка", asList("Мышь", "Товары для компьютера"))
        );
    }

    @MethodSource(value = "methodSourceComplexDataProvider")
    @ParameterizedTest(name = "При поиске \"{0}\" в результатах есть \"{1}\"")
    void methodSourceParameterizedTest(String searchData, List<String> expectedResult) {
        Selenide.open("https://www.avito.ru/");
        $("[data-marker='search-form/suggest']").setValue(searchData).pressEnter();
        $$("div[data-marker='item'].items-listItem-Gd1jN").shouldHave(CollectionCondition.texts(expectedResult));
    }

/*-------------------------------------------------------------------------------------------------------------------*/
    /*Итерационный тест с 2 параметрами, который запускается последовательно необходимое количество раз (зависит от
    объема тестовых данных). Данные хранятся тут же в CsvSource контейнере
    Проверяет на наличие на новой странице второго текста из пары из тестовых данных (1 кейс - 1 пара).*/
    @CsvSource(value = {
            "Смартфон, Объявления по запросу «Смартфон»",
            "Пылесос, Объявления по запросу «Пылесос»"
    })
    @Disabled
    @ParameterizedTest(name = "При поиске \"{0}\" на cтранице есть \"{1}\"")
    void csvSourceParametrizedTest(String searchData, String expectedResult) {
        Selenide.open("https://www.avito.ru/");
        $("[data-marker='search-form/suggest']").setValue(searchData).pressEnter();
        $$("[data-marker='page-title/text']").find(text(expectedResult)).shouldBe(visible);
    }

/*-------------------------------------------------------------------------------------------------------------------*/
    /*Итерационный тест с 2 параметрами, который запускается последовательно необходимое количество раз (зависит от
    объема тестовых данных). Данные хранятся в .csv файле в папке resources/test_data.
    Проверяет на наличие на новой странице второго текста из пары из тестовых данных (1 кейс - 1 пара).*/
    @CsvFileSource(resources = {"/test_data/avito_test_data.csv"})
    @Disabled
    @ParameterizedTest(name = "При поиске \"{0}\" на cтранице есть \"{1}\"")
    void csvFileSourceParametrizedTest(String searchData, String expectedResult) {
        Selenide.open("https://www.avito.ru/");
        $("[data-marker='search-form/suggest']").setValue(searchData).pressEnter();
        $$("[data-marker='page-title/text']").find(text(expectedResult)).shouldBe(visible);
    }

}

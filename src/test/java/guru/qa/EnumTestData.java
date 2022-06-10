package guru.qa;

public enum EnumTestData {
    MONITOR("Монитор", "Объявления по запросу «Монитор»"),
    HEADPHONES("Наушники", "Объявления по запросу «Наушники»");

    public final String searchData;
    public final String result;

    EnumTestData(String searchData, String result) {
        this.searchData = searchData;
        this.result = result;
    }

}

package demo;

public class UnlimitedDisplay implements QuotaDisplay {
    private static final UnlimitedDisplay instance = new UnlimitedDisplay();

    public static UnlimitedDisplay getInstance() {
        return instance;
    }

}

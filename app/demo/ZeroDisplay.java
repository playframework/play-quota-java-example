package demo;


public class ZeroDisplay implements QuotaDisplay {
    private static final ZeroDisplay instance = new ZeroDisplay();

    public static ZeroDisplay getInstance() {
        return instance;
    }
}

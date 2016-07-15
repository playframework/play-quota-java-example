package demo;

public class RateLimitedDisplay implements QuotaDisplay {

    private Long secondsUntilRefill;
    private Long tickSeconds;
    private int balance;
    private int maxBalance;

    public RateLimitedDisplay(Long secondsUntilRefill, Long tickSeconds, int balance, int maxBalance) {
        this.secondsUntilRefill = secondsUntilRefill;
        this.tickSeconds = tickSeconds;
        this.balance = balance;
        this.maxBalance = maxBalance;
    }

    public Long secondsUntilRefill() {
        return secondsUntilRefill;
    }

    public Long tickSeconds() {
        return tickSeconds;
    }

    public int balance() {
        return balance;
    }

    public int maxBalance() {
        return maxBalance;
    }
}

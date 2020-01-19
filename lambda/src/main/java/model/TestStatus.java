package model;

public enum TestStatus {
    NOTSOLVED (0),
    SOLVED (1),
    CHECKED (2);

    private int value;
    private TestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

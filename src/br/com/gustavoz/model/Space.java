package br.com.gustavoz.model;

public class Space {

    private Integer actual;
    private final int expectedValue;
    private final boolean fixed;

    public Space(int expectedValue, boolean fixed) {
        this.expectedValue = expectedValue;
        this.fixed = fixed;
        if (fixed) {
            this.actual = expectedValue;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        if (fixed) return;
        this.actual = actual;
    }

    public void clearSpace() {
        setActual(null);
    }

    public int getExpectedValue() {
        return expectedValue;
    }

    public boolean isFixed() {
        return fixed;
    }
}

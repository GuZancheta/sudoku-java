package br.com.gustavoz.model;

public enum GameStatusEnum {

    NOT_STARTED("Not started"),
    INCOMPLETE("Incomplete"),
    COMPLETE("Complete");

    private final String value;

    GameStatusEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

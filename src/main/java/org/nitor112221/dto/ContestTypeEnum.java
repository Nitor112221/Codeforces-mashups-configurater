package org.nitor112221.dto;

public enum ContestTypeEnum {
    DIV1("Div. 1"),
    DIV2("Div. 2"),
    DIV3("Div. 3"),
    DIV4("Div. 4"),
    DIV1DIV2("Div. 1 + Div. 2");

    private final String name;

    ContestTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static ContestTypeEnum fromContestName(String contestName) {
        if (contestName.contains("Div. 1 + Div. 2")) return DIV1DIV2;
        if (contestName.contains("Div. 4")) return DIV4;
        if (contestName.contains("Div. 3")) return DIV3;
        if (contestName.contains("Div. 2")) return DIV2;
        return DIV1;
    }
}

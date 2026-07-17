package org.nitor112221.dto;

public enum TagEnum {
    TWO_SAT("2-sat", "2-sat"),
    BINARY_SEARCH("binary search", "бинарный поиск"),
    BITMASKS("bitmasks", "битмаски"),
    BRUTE_FORCE("brute force", "полный перебор"),
    CHINESE_REMAINDER_THEOREM("chinese remainder theorem", "китайская теорема об остатках"),
    COMBINATORICS("combinatorics", "комбинаторика"),
    COMMUNICATION("communication", "коммуникационная"),
    CONSTRUCTIVE_ALGORITHMS("constructive algorithms", "конструктив"),
    DATA_STRUCTURES("data structures", "структуры данных"),
    DFS_AND_SIMILAR("dfs and similar", "DFS и подобные"),
    DIVIDE_AND_CONQUER("divide and conquer", "разделяй и властвуй"),
    DP("dp", "дп"),
    DSU("dsu", "СНМ"),
    EXPRESSION_PARSING("expression parsing", "разбор выражений"),
    FFT("fft", "бпв"),
    FLOWS("flows", "потоки"),
    GAMES("games", "игры"),
    GEOMETRY("geometry", "геометрия"),
    GRAPH_MATCHINGS("graph matchings", "паросочетания"),
    GRAPHS("graphs", "графы"),
    GREEDY("greedy", "жадные алгоритмы"),
    HASHING("hashing", "хеширование"),
    IMPLEMENTATION("implementation", "реализация"),
    INTERACTIVE("interactive", "интерактив"),
    MATH("math", "математика"),
    MATRICES("matrices", "матрицы"),
    MEET_IN_THE_MIDDLE("meet-in-the-middle", "meet-in-the-middle"),
    NUMBER_THEORY("number theory", "теория чисел"),
    PROBABILITIES("probabilities", "вероятности"),
    SCHEDULES("schedules", "расписания"),
    SHORTEST_PATHS("shortest paths", "кратчайшие пути"),
    SORTINGS("sortings", "сортировки"),
    STRING_SUFFIX_STRUCTURES("string suffix structures", "суффиксные структуры"),
    STRINGS("strings", "строки"),
    TERNARY_SEARCH("ternary search", "тернарный поиск"),
    TREES("trees", "деревья"),
    TWO_POINTERS("two pointers", "два указателя");

    private final String english;
    private final String russian;
    private Integer id;

    TagEnum(String english, String russian) {
        this.english = english;
        this.russian = russian;
    }

    public String getEnglish() { return english; }
    public String getRussian() { return russian; }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @Override
    public String toString() {
        return english;
    }

    public static TagEnum fromEnglish(String english) {
        if (english == null) {
            return null;
        }
        for (TagEnum tag : values()) {
            if (tag.english.equalsIgnoreCase(english)) {
                return tag;
            }
        }
        return null;
    }

    public static TagEnum fromRussian(String russian) {
        if (russian == null) {
            return null;
        }
        for (TagEnum tag : values()) {
            if (tag.russian.equalsIgnoreCase(russian)) {
                return tag;
            }
        }
        return null;
    }
}
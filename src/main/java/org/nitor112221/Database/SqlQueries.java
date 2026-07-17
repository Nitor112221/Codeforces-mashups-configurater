package org.nitor112221.Database;

public class SqlQueries {
    // ------------------------------------!!! CREATE TABLES !!!-----------------------------------
    protected static final String CREATE_CONTESTS =
            "CREATE TABLE IF NOT EXISTS contests (" +
                    "    id INTEGER PRIMARY KEY," +
                    "    type TEXT NOT NULL CHECK (type IN ('Div. 1', 'Div. 2', 'Div. 3', 'Div. 4', 'Div. 1 + Div. 2'))" +
                    ");";

    protected static final String CREATE_PROBLEMS =
            "CREATE TABLE IF NOT EXISTS problems (" +
                    "    contest_id INTEGER NOT NULL," +
                    "    problem_index TEXT NOT NULL," +
                    "    name TEXT NOT NULL," +
                    "    rating INTEGER NOT NULL," +
                    "    PRIMARY KEY (contest_id, problem_index)," +
                    "    FOREIGN KEY (contest_id) REFERENCES contests(id) ON DELETE CASCADE" +
                    ");";

    protected static final String CREATE_TAGS =
            "CREATE TABLE IF NOT EXISTS tags (" +
                    "    id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "    name TEXT UNIQUE NOT NULL" +
                    ");";

    protected static final String CREATE_PROBLEM_TAGS =
            "CREATE TABLE IF NOT EXISTS problem_tags (" +
                    "    contest_id INTEGER NOT NULL," +
                    "    problem_index TEXT NOT NULL," +
                    "    tag_id INTEGER NOT NULL," +
                    "    PRIMARY KEY (contest_id, problem_index, tag_id)," +
                    "    FOREIGN KEY (contest_id, problem_index) REFERENCES problems(contest_id, problem_index) ON DELETE CASCADE," +
                    "    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE" +
                    ");";

    protected static final String CREATE_INDEXES =
            "CREATE INDEX IF NOT EXISTS idx_problems_contest ON problems(contest_id);" +
                    "CREATE INDEX IF NOT EXISTS idx_problem_tags_tag ON problem_tags(tag_id);" +
                    "CREATE INDEX IF NOT EXISTS idx_tags_name ON tags(name);";

    // ------------------------------------!!! INSERT DATA !!!-----------------------------------
    protected static final String INSERT_PROBLEM =
            "INSERT INTO problems (contest_id, problem_index, name, rating) VALUES (?, ?, ?, ?)";

    protected static final String INSERT_CONTEST =
            "INSERT INTO contests (id, type) VALUES (?, ?)";

    protected static final String INSERT_TAG =
            "INSERT INTO tags (name) VALUES (?) ON CONFLICT(name) DO NOTHING";

    protected static final String GET_TAG_ID = "SELECT id FROM tags WHERE name = ?";

    protected static final String LINK_TAG =
            "INSERT INTO problem_tags (contest_id, problem_index, tag_id) VALUES (?, ?, ?)";

    // ------------------------------------!!! FIND DATA !!!-----------------------------------

    protected static final String FIND_LINKED_TAGS =
            "SELECT tag_id FROM problem_tags WHERE contest_id = ? AND problem_index = ?";
}

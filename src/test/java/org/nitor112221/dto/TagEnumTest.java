package org.nitor112221.dto;

import org.junit.jupiter.api.*;
import org.nitor112221.Database.Database;

import static org.junit.jupiter.api.Assertions.*;

class TagEnumTest {

    @BeforeAll
    static void initTags() throws Exception {
        if (TagEnum.DP.getId() == null) {
            Database.Conn();
            Database.CloseDB();
        }
    }

    @Test
    void fromEnglish_shouldReturnCorrectEnum() {
        TagEnum tag = TagEnum.fromEnglish("dp");
        assertNotNull(tag);
        assertEquals(TagEnum.DP, tag);
        assertEquals("dp", tag.getEnglish());
        assertEquals("дп", tag.getRussian());
    }

    @Test
    void fromEnglish_shouldBeCaseInsensitive() {
        TagEnum tag = TagEnum.fromEnglish("DP");
        assertNotNull(tag);
        assertEquals(TagEnum.DP, tag);
    }

    @Test
    void fromEnglish_shouldReturnNullForUnknown() {
        assertNull(TagEnum.fromEnglish("unknown"));
        assertNull(TagEnum.fromEnglish(null));
    }

    @Test
    void fromRussian_shouldReturnCorrectEnum() {
        TagEnum tag = TagEnum.fromRussian("дп");
        assertNotNull(tag);
        assertEquals(TagEnum.DP, tag);
    }

    @Test
    void fromRussian_shouldReturnNullForUnknown() {
        assertNull(TagEnum.fromRussian("неизвестно"));
        assertNull(TagEnum.fromRussian(null));
    }

    @Test
    void fromId_shouldReturnCorrectEnum() {
        assertNotNull(TagEnum.DP.getId());

        TagEnum tag = TagEnum.fromId(TagEnum.DP.getId());
        assertNotNull(tag);
        assertEquals(TagEnum.DP, tag);
    }

    @Test
    void fromId_shouldReturnNullForUnknown() {
        assertNull(TagEnum.fromId(999999));
        assertNull(TagEnum.fromId(null));
    }

    @Test
    void toString_shouldReturnEnglishName() {
        assertEquals("dp", TagEnum.DP.toString());
        assertEquals("binary search", TagEnum.BINARY_SEARCH.toString());
    }
}
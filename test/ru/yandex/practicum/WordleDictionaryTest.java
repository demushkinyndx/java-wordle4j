package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary();
        dictionary.add("привет");
        dictionary.add("слово");
        dictionary.add("ружьё");
    }

    @Test
    void testAddAndGetAll() {
        assertEquals(3, dictionary.size());
        assertTrue(dictionary.getAll().contains("привет"));
        assertTrue(dictionary.getAll().contains("слово"));
        assertTrue(dictionary.getAll().contains("ружье"));
    }

    @Test
    void testAddDuplicateWord() {
        dictionary.add("слово");
        assertEquals(4, dictionary.size());
        int count = 0;
        for (String word : dictionary.getAll()) {
            if (word.equals("слово")) {
                count++;
            }
        }
        assertEquals(2, count);
    }

    @Test
    void testContains() {
        assertTrue(dictionary.contains("слово"));
        assertFalse(dictionary.contains("нет"));
    }

    @Test
    void testGetRandomWord() {
        String randomWord = dictionary.getRandomWord();
        assertNotNull(randomWord);
        assertTrue(dictionary.getAll().contains(randomWord));
    }

    @Test
    void testEmptyDictionary() {
        WordleDictionary emptyDict = new WordleDictionary();
        assertEquals(0, emptyDict.size());
        assertTrue(emptyDict.getAll().isEmpty());
    }
}
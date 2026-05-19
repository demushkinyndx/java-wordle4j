package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.exception.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


class WordleTest {
    private WordleGame game;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException, DictionaryIsEmptyException {
        Path dictFile = tempDir.resolve("words_ru.txt");

        Files.writeString(dictFile, "кот\n"
                + "мама\n"
                + "дом\n"
                + "лес\n"
                + "река\n"
                + "море\n"
                + "поле\n"
                + "молоко\n"
                + "собака\n"
                + "перец\n"
                + "пивко\n"
                + "нора\n"
                + "луна\n");

        TestLogger testLogger = new TestLogger();
        game = new WordleGame(dictFile.toString(), 6, testLogger);
        game.startGame();
    }

    @Test
    void testGameStart() {
        assertNotNull(game.getHiddenWord());
        String hiddenWord = game.getHiddenWord();
        assertTrue(hiddenWord.equals("перец") || hiddenWord.equals("пивко"));
        assertEquals(6, game.getTries());
    }

    @Test
    void testWordGuessedCorrectly() {
        String hidden = game.getHiddenWord();
        assertTrue(game.isWordGuessed(hidden));
    }

    @Test
    void testWordNotFoundInDictionary() {
        assertThrows(WordNotFoundInDictionaryException.class,
                () -> game.isWordGuessed("кринж"));
    }

    @Test
    void testWrongWordButInDictionary() {
        String hidden = game.getHiddenWord();
        String wrongWord = hidden.equals("перец") ? "пивко" : "перец";

        boolean result = game.isWordGuessed(wrongWord);
        assertFalse(result);
        assertNotNull(game.getAnswerSymbols());
        assertEquals(5, game.getAnswerSymbols().length());
        assertEquals(5, game.getTries());
    }

    @Test
    void testStepsLimitExceeded() {
        String wrongWord = game.getHiddenWord().equals("перец") ? "пивко" : "перец";

        for (int i = 0; i < 6; i++) {
            if (i < 5) {
                game.isWordGuessed(wrongWord);
            } else {
                assertThrows(StepsLimitExceededException.class,
                        () -> game.isWordGuessed(wrongWord));
            }
        }
    }

    @Test
    void testGetSuggestNotNull() {
        String suggestion = game.getSuggest();
        assertNotNull(suggestion);
        assertTrue(suggestion.equals("перец") || suggestion.equals("пивко"));
    }

    @Test
    void testGetAnswerSymbols() {
        String hidden = game.getHiddenWord();
        String wrongWord = hidden.equals("перец") ? "пивко" : "перец";

        game.isWordGuessed(wrongWord);
        String symbols = game.getAnswerSymbols();

        System.out.println(hidden);
        System.out.println(wrongWord);
        System.out.println(symbols);

        if (hidden.equals("перец")) {
            assertEquals("+----", symbols);
        }
        if (hidden.equals("пивко")) {
            assertEquals("+----", symbols);
        }


        assertNotNull(symbols);
        assertEquals(5, symbols.length());

        for (char c : symbols.toCharArray()) {
            assertTrue(c == '+' || c == '^' || c == '-');
        }

    }

    @Test
    void testMultipleGuesses() {
        String hidden = game.getHiddenWord();
        String wrongWord1 = hidden.equals("перец") ? "пивко" : "перец";
        String wrongWord2 = hidden.equals("перец") ? "нора" : "поле";

        game.isWordGuessed(wrongWord1);
        String symbols1 = game.getAnswerSymbols();

        assertThrows(WordNotFoundInDictionaryException.class,
                () -> game.isWordGuessed(wrongWord2));

        assertNotNull(symbols1);
    }
}
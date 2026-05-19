package ru.yandex.practicum;

import ru.yandex.practicum.exception.*;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    private static Scanner scanner;
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_ERROR_FILE = "log_error.txt";
    private static final String LOG_INFO_FILE = "log_info.txt";
    private static final int STEPS = 6;

    static void main(String[] args) {
        Logger log = null;
        scanner = new Scanner(System.in);
        try {
            log = new Logger(LOG_INFO_FILE, LOG_ERROR_FILE);
            WordleGame game = new WordleGame(DICTIONARY_FILE, STEPS, log);
            game.startGame();
            System.out.println("Загадано слово из пяти букв, у вас " + STEPS + " попыток.");

            while (true) {
                String word = getStringInput("Введите слово из 5 букв (осталось попыток: " + game.getTries() + "): ");
                try {
                    if (word == null) {
                        String suggest = game.getSuggest();
                        System.out.println(suggest);
                        if (suggest.equals(game.getHiddenWord())) {
                            //не особо понял по тз что делать в этом случае, пусть подсказки выиграют
                            throw new SuggestWinException("Подсказки угадали слово. Игра окончена.");
                        }
                        continue;
                    }

                    if (game.isWordGuessed(word)) {
                        System.out.println("Вы победили. До свидания.");
                        break;
                    }

                    System.out.println(game.getAnswerSymbols());
                } catch (StepsLimitExceededException | SuggestWinException ex) {
                    log.info(ex.getMessage());
                    System.out.println(ex.getMessage());
                    break;
                } catch (WordNotFoundInDictionaryException ex) {
                    log.info(ex.getMessage());
                    System.out.println(ex.getMessage());
                }
            }
        } catch (LoggerCreationException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            assert log != null;
            log.error(e);
            System.out.println(e.getMessage());
        } catch (DictionaryIsEmptyException e) {
            assert log != null;
            log.error(e);
            System.out.println(e.getMessage());
        }


    }

    public static String getStringInput(String message) {
        while (true) {
            if (message != null && !message.isEmpty()) {
                System.out.println(message);
            }
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                //считаем, что подсказку хотят
                return null;
            }

            if (input.length() != 5) {
                System.out.println("Нужно слово из 5 букв.");
                continue;
            }

            if (!input.matches("[а-яА-Я]+")) {
                System.out.println("Введите слово только из русских букв.");
                continue;
            }

            return input;
        }
    }

}

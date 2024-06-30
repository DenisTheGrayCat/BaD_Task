import java.io.*;

/**
 * Клас являє собою головний клас застосунку.
 * Є можливість працювати із скомпільованою версією.
 * В такому випадку потрібно першим аргументом на скомпільовану програму
 * передати шлях до файлу із вхідними даними.
 * За замовченням встановлений файл із назвою 10m.txt, який розміщений в константі
 * FILE_NAME.
 *
 * Класи MyHeapMax та MyHeapMin - це скорочена по функціоналу версія
 * (робилось чисто під поставлену задачу) структури даних Heap.
 * Спільний функціонал виведено в абстрактний клас AbstractHeap.
 *
 * Клас MyCustomDeque - Являє собою скорочену версію двосторонньої черги
 * (також робилось під конкретну задачу).
 * Структура даних у вигляді зв'язного списку вибрана тому, що (на мою думку) вона найкраще підходить
 * саме для зберігання послідовностей.
 * Також зручне зберігання результату без перекопіювання даних.
 *
 *
 * Що використовував.
 * Десь пів року тому я наштовхувався на схожу задачу як пошук медіани. (поиск элементов
 * в последовательности на местах "Золотых сечений"). Но використовував тоді стандартну бібліотеку
 * PriorityQueue.
 * Зараз захотілось розібратись як працює тому скористався:
 * https://ru.wikipedia.org/wiki/%D0%9A%D1%83%D1%87%D0%B0_(%D1%81%D1%82%D1%80%D1%83%D0%BA%D1%82%D1%83%D1%80%D0%B0_%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85)
 *
 * Також в інтернеті шукав інформацію щодо PowerShell, та методику виклику команд операційної системи.
 *
 */


public class Main {
    private static final String FILE_NAME = "10m.txt";
    private static int size = 0;
    private static int[] sourceData;

    public static void main(String[] args) {
        // Секція ініціалізації
        long startTime = System.currentTimeMillis();
        int[] basicData = getBasicData(args);

        // Секція обчислення значень
        int maxValue = getMaxValue(basicData);
        int minValue = getMinValue(basicData);
        double mediana = getMediana(basicData);
        double middle = getMiddleValue(basicData);
        MyCustomDeque longestIncSeq = findLongestIncSeq(basicData);
        MyCustomDeque longestDecSeq = findLongestDecSeq(basicData);

        //Секція виведення результатів
        System.out.println("Завдання 1: \"Максимальне число в файлі\"");
        System.out.println(maxValue);
        System.out.println("Завдання 2: \"Мінімальне число в файлі\"");
        System.out.println(minValue);
        System.out.println("Завдання 3: \"Медіана\"");
        System.out.println(mediana);
        System.out.println("Завдання 4: \"Середнє арифметичне значення\"");
        System.out.println(middle);
        System.out.println("Завдання 5: \"Найбільшу послідовність чисел, яка збільшується\"");
        System.out.println(longestIncSeq);
        System.out.println("Завдання 6: \"Найбільшу послідовність чисел, яка зменшується\"");
        System.out.println(longestDecSeq);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Час виконання програми: " + executionTime + " мілісекунд.");
    }

    /**
     * Метод намагається обробити вхідні дані.
     * В разі помилки припиняє виконання програми.
     */
    private static int[] getBasicData(String[] args) {
        // Визначення файлу, який буде джерелом даних
        String sourceName = (args.length > 0) ? args[0] : FILE_NAME;
        File sourceFile = checkSourceFile(sourceName);
        if (sourceFile == null) {
            System.err.println("Source File and default file (" + FILE_NAME + ") not found");
            System.exit(1);
        }
        if (sourceFile.getName().equals(FILE_NAME)) {
            System.out.println("Used Default file with name: " + FILE_NAME);
        }


        // Перевірка довжини файлу в рядках та створення масиву
        size = getFileSize(sourceFile.getName());
        int[] sourceArray = new int[size];

        // Читання даних із файлу
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                sourceArray[counter++] = Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Something went wrong while reading file " + sourceFile.getName());
            System.err.println(e.getMessage());
        }
        return sourceArray;
    }

    /**
     * Кросплатформений метод для підрахунку строк у файлі.
     * Так. Можна було використати counter на момент читання даних, та гратися із розміром масиву.
     * Або взагалі використати щось, що implements List
     * Але в умові було: "Звісно, ви можете використовувати готові набори
     * статистичних класів/функцій/бібліотек, але це не найкращий варіант."
     * Тому вирішив використати такий підхід. Та й цікаво було трохи поритись
     * "як воно там у сімействі ОС Windows". ))
     */
    public static int getFileSize(String name) {
        String os = System.getProperty("os.name").toLowerCase();
        String command;

        if (os.contains("win")) {
            command = "powershell.exe -Command \"(Get-Content " + name + " | Measure-Object -Line).Lines\"";
        } else {
            command = "wc -l " + name;
        }

        ProcessBuilder processBuilder = new ProcessBuilder();
        if (os.contains("win")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("bash", "-c", command);
        }

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine().trim();
            return os.contains("win") ? Integer.parseInt(line) : Integer.parseInt(line.split(" ")[0]);
        } catch (IOException e) {
            System.err.println("Something went wrong while checking lines count in file " + name);
            System.exit(1);
        }
        return 0;
    }

    private static File checkSourceFile(String sourceName) {
        File result = new File(sourceName);
        if (result.exists()) return result;
        if (!result.getName().equals(FILE_NAME)) {
            result = new File(FILE_NAME);
        }
        if (!result.exists()) return null;
        return result;
    }

    /**
     * Завдання 1: "Максимальне число в файлі"
     *
     * @param basicData - масив вхідних даних
     * @return Максималье число в послідовності.
     */
    private static int getMaxValue(int[] basicData) {
        int max = Integer.MIN_VALUE;
        for (int element : basicData) {
            if (element > max) max = element;
        }
        return max;
    }

    /**
     * Завдання 2: "Мінімальне число в файлі"
     *
     * @param basicData - масив вхідних даних
     * @return Мінімальне число в послідовності.
     */

    private static int getMinValue(int[] basicData) {
        int min = Integer.MAX_VALUE;
        for (int element : basicData) {
            if (element < min) min = element;
        }
        return min;
    }

    /**
     * Завдання 3: "Пошук медіани"
     *
     * @param basicData - масив вхідних даних
     * @return Значення медіани.
     */
    private static double getMediana(int[] basicData) {

        MyHeapMax maxHeap = new MyHeapMax();
        MyHeapMin minHeap = new MyHeapMin();
        for (int element : basicData) {
            maxHeap.addElement(element);
            minHeap.addElement(maxHeap.getHead());
            if (minHeap.getSize() > maxHeap.getSize()) {
                maxHeap.addElement(minHeap.getHead());
            }
        }
        if (size % 2 == 1) {
            return maxHeap.getHead();
        }
        return (1.0 * maxHeap.getHead() + minHeap.getHead()) / 2;

    }

    /**
     * Завдання 4: "Пошук середнього арифметичного значення послідовності"
     *
     * @param basicData - масив вхідних даних
     */
    private static double getMiddleValue(int[] basicData) {
        long sum = 0L;
        for (int element : basicData) {
            sum += element;
        }
        System.out.println(sum);
        System.out.println(size);
        return (double) sum / size;
    }

    /**
     * Завдання 5: "Визначити найбільшу послідовність чисел, яка збільшується"
     *
     * @param basicData - масив вхідних даних
     * @return Результуюча послідовність
     */
    private static MyCustomDeque findLongestIncSeq(int[] basicData) {
        MyCustomDeque currentIncSeq = new MyCustomDeque();
        MyCustomDeque longestIncSeq = new MyCustomDeque();

        for (int element : basicData) {
            if (currentIncSeq.getSize() == 0) {
                currentIncSeq.addToTail(element);
            } else if (currentIncSeq.getTail().getValue() < element) {
                currentIncSeq.addToTail(element);
            } else {
                if (currentIncSeq.getSize() > longestIncSeq.getSize()) {
                    longestIncSeq.reassignQueue(currentIncSeq);
                }
                currentIncSeq = new MyCustomDeque();
                currentIncSeq.addToTail(element);
            }
        }

        if (currentIncSeq.getSize() > longestIncSeq.getSize()) {
            longestIncSeq.reassignQueue(currentIncSeq);
        }

        return longestIncSeq;
    }

    /**
     * Завдання 6: "Визначити найбільшу послідовність чисел, яка зменшується"
     *
     * @param basicData - масив вхідних даних
     * @return Результуюча послідовність
     */
    private static MyCustomDeque findLongestDecSeq(int[] basicData) {
        MyCustomDeque currentDecSeq = new MyCustomDeque();
        MyCustomDeque longestDecSeq = new MyCustomDeque();

        for (int element : basicData) {
            if (currentDecSeq.getSize() == 0) {
                currentDecSeq.addToTail(element);
            } else if (currentDecSeq.getTail().getValue() > element) {
                currentDecSeq.addToTail(element);
            } else {
                if (currentDecSeq.getSize() > longestDecSeq.getSize()) {
                    longestDecSeq.reassignQueue(currentDecSeq);
                }
                currentDecSeq = new MyCustomDeque(); // Start new sequence
                currentDecSeq.addToTail(element);
            }
        }

        if (currentDecSeq.getSize() > longestDecSeq.getSize()) {
            longestDecSeq.reassignQueue(currentDecSeq);
        }

        return longestDecSeq;
    }


}

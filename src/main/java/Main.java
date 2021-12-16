import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.ArrayUtils;

/*
Разработать программу, которая выполняет:
1. Генерацию массива числовых данных размера N со случайным
распределением значений элементов массива, подчиняющихся
вероятностному закону распределения.
2. Сортировку исходного массива простыми и сложными методами (не
менее 2 каждого вида).
3. Провести сравнительный анализ простых и сложных методов
сортировки элементов массива, рассчитав показатели производительности
(время сортировки и соотношение методов производительности
(относительное время сортировки)). Результаты анализа представить в
табличной форме записи.
4. Определить оценку качества, реализованных в программе простых
методов сортировки (выбором, вставками, обменом) по двум показателям:
 и ,c/n и m/n
где С – количество операций сравнения элементов массива;
М – количество перестановки элементов массива, потребовавшихся для
сортировки массива.
Результаты представить в табличной форме записи.
*/

public class Main {

    public static double[] GenerateNormalDistributionArray() {
        double[] numbers = new double[2000];
        Random rnd = new Random();

        for (int i = 0; i < 2000; i++) {
            numbers[i] = rnd.nextGaussian();
        }
        return numbers;
    }

    public static void swap(double[] numbers, int i, int j) {
        double temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }

    public static void InspectionsSort(double[] array) {
        int compareCount = 0, replaceCount = 0;

        //Сортировка вставками
        var timer = Stopwatch.createUnstarted();

        timer.start();
        for (int left = 0; left < array.length; left++) {

            // Вытаскиваем значение элемента
            double value = array[left];
            // Перемещаемся по элементам, которые перед вытащенным элементом
            int i = left - 1;
            for (; i >= 0; i--) {
                // Если вытащили значение меньшее — передвигаем больший элемент дальше
                compareCount++;
                if (value < array[i]) {
                    array[i + 1] = array[i];
                } else {
                    // Если вытащенный элемент больше — останавливаемся
                    break;
                }
            }
            // В освободившееся место вставляем вытащенное значение
            replaceCount++;
            array[i + 1] = value;

        }
        timer.stop();

        System.out.println(
                "Сортировка вставками " +
                        timer.elapsed(TimeUnit.MILLISECONDS)+" ms " +
                        " сравнений: " + compareCount +
                        " переставновок/вставок: " + replaceCount +
                        " Качество c/n " + (compareCount / 2000.0) +
                        " Качество m/n " + (replaceCount / 2000.0)
        );
    }

    public static void SelectionSort(double[] array) {
        int compareCount = 0, replaceCount = 0;

        //Сортировка вставками
        var timer = Stopwatch.createUnstarted();

        timer.start();
        for (int left = 0; left < array.length; left++) {
            int minInd = left;
            for (int i = left; i < array.length; i++) {
                compareCount++;
                if (array[i] < array[minInd]) {
                    minInd = i;
                }
            }
            replaceCount++;
            swap(array, left, minInd);
        }

        timer.stop();
        System.out.println(
                "Сортировка выбором " +
                        timer.elapsed(TimeUnit.MILLISECONDS)+" ms " +
                        " сравнений: " + compareCount +
                        " переставновок/вставок: " + replaceCount +
                        " Качество c/n " + (compareCount / 2000.0) +
                        " Качество m/n " + (replaceCount / 2000.0)
        );
    }


    static int compareCountQS = 0, replaceCountQS = 0;

    public static void quickSort(double[] array, int low, int high) {


        if (low >= high) {
            return;
        }
        // выбрать опорный элемент
        int middle = low + (high - low) / 2;
        double opora = array[middle];

        // разделить на подмассивы, который больше и меньше опорного элемента
        int i = low, j = high;
        while (i <= j) {
            while (array[i] < opora) {
                compareCountQS++;
                i++;
            }

            while (array[j] > opora) {
                j--;
            }

            if (i <= j) {//меняем местами
                replaceCountQS++;
                double temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        // вызов рекурсии для сортировки левой и правой части
        if (low < j)
            quickSort(array, low, j);

        if (high > i)
            quickSort(array, i, high);
    }

    static void bucketSort(double[] array) {
        int compareCount = 0, replaceCount = 0;
        var timer = Stopwatch.createUnstarted();
        timer.start();
        double[] buckets = new double[array.length];

        for (int i = 0; i < array.length; ++i) {
            buckets[i] = 0;
        }

        for (int i = 0; i < array.length - 1; ++i) {
            compareCount++;
            var index = ArrayUtils.indexOf(array, array[i]);
            ++buckets[index];
        }

        for (int i = 0, j = 0; j < array.length - 1; ++j) {
            for (double k = buckets[j]; k > 0; --k) {
                replaceCount++;
                array[i++] = j;
            }
        }
        timer.stop();
        System.out.println(
                "Блочная сортировка " +
                        timer.elapsed(TimeUnit.MILLISECONDS)+" ms " +
                        " сравнений: " + compareCount +
                        " переставновок/вставок: " + replaceCount +
                        " Качество c/n " + (compareCount / 2000.0) +
                        " Качество m/n " + (replaceCount / 2000.0)
        );
    }

    public static void main(String[] args) {
        var numbers = GenerateNormalDistributionArray();
        InspectionsSort(numbers);
        SelectionSort(numbers);

        var timer = Stopwatch.createUnstarted();
        timer.start();
        quickSort(numbers, 0, numbers.length - 1);
        timer.stop();
        System.out.println(
                "Быстрая сортировка " +
                        timer.elapsed(TimeUnit.MILLISECONDS)+" ms " +
                        " сравнений: " + compareCountQS +
                        " переставновок/вставок: " + replaceCountQS +
                        " Качество c/n " + (compareCountQS / 2000.0) +
                        " Качество m/n " + (replaceCountQS / 2000.0)
        );
        bucketSort(numbers);
    }
}

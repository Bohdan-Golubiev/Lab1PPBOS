import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int arraySize = 1_000_000_000;
        final int threadCount = 8;

        System.out.println("Розмір масиву: " + arraySize);
        System.out.println("Кількість потоків: " + threadCount);

        int[] array = new int[arraySize];
        Arrays.fill(array, 1);

        SumWorker[] workers = new SumWorker[threadCount];
        int chunkSize = arraySize / threadCount;

        for (int t = 0; t < threadCount; t++) {
            int from = t * chunkSize;
            int to = (t == threadCount - 1) ? arraySize : from + chunkSize;

            workers[t] = new SumWorker(array, from, to);
            workers[t].start();
        }

        long totalSum = 0L;
        for (int t = 0; t < threadCount; t++) {
            workers[t].join();
            totalSum += workers[t].getPartialSum();
        }

        System.out.println("Загальна сума елементів масиву: " + totalSum);
        System.out.println("Очікувана сума (перевірка): " + (long) arraySize);
    }
}

class SumWorker extends Thread {
    private final int[] array;
    private final int fromIndex;
    private final int toIndex;
    private long partialSum;

    public SumWorker(int[] array, int fromIndex, int toIndex) {
        this.array = array;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    @Override
    public void run()
    {
        long sum = 0L;
        for (int i = fromIndex; i < toIndex; i++) {
            sum += array[i];
        }
        this.partialSum = sum;
        System.out.println("Потік ID: " + Thread.currentThread().getId() + " оброблив діапазон [" + fromIndex + ", " + toIndex + ")");
    }

    public long getPartialSum() {
        return partialSum;
    }
}
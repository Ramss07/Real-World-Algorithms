import bridges.base.LineChart;
import bridges.benchmark.SortingBenchmark;
import bridges.connect.Bridges;
import bridges.validation.RateLimitException;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public class QuickSortBenchmark 
{
    static class SortingAlgorithms 
    {
        static int comparisons = 0;
        static int swaps = 0;

        static Consumer<int[]> quicksort = arr -> quicksort(arr, 0, arr.length - 1);

        static void quicksort(int[] arr, int low, int high) 
        {
            if (low < high) 
            {
                if (high - low <= 100) 
                {
                    insertionSort(arr, low, high);
                    return;
                }
                int pivotIndex = partition(arr, low, high);
                quicksort(arr, low, pivotIndex - 1);
                quicksort(arr, pivotIndex + 1, high);
            }
        }

        static int partition(int[] arr, int low, int high) 
        {
            int pivot = arr[high];
            int i = low - 1;
            for (int j = low; j < high; j++) 
            {
                comparisons++;
                if (arr[j] < pivot) 
                {
                    i++;
                    swap(arr, i, j);
                }
            }
            swap(arr, i + 1, high);
            return i + 1;
        }

        static void insertionSort(int[] arr, int low, int high) 
        {
            for (int i = low + 1; i <= high; i++) 
            {
                int key = arr[i];
                int j = i - 1;
                while (j >= low && arr[j] > key) 
                {
                    arr[j + 1] = arr[j];
                    j--;
                    swaps++;
                    comparisons++;
                }
                arr[j + 1] = key;
            }
        }

        static void swap(int[] arr, int i, int j) 
        {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            swaps++;
        }

        static Consumer<int[]> insertionSortStandalone = arr -> insertionSort(arr, 0, arr.length - 1);
    }

    public static void testSmallDataset() 
    {
        int[] testArr = {5, 3, 8, 4, 2, 7, 6, 1};
        System.out.println("Before Sorting: " + Arrays.toString(testArr));
        SortingAlgorithms.quicksort.accept(testArr);
        System.out.println("After Sorting: " + Arrays.toString(testArr));
    }


    public static void main(String[] args) throws IOException, RateLimitException, InterruptedException {

        testSmallDataset();

        Bridges bridgesQuick = new Bridges(12, "RamleyHirn", "575356762377");
        bridgesQuick.setTitle("Quicksort Performance");
        bridgesQuick.setDescription("Runtime and Operation Count for Quicksort.");
        
        LineChart plotQuick = new LineChart();
        SortingBenchmark benchQuick = new SortingBenchmark(plotQuick);
    
        benchQuick.linearRange(10000, 10000000, 20);
        benchQuick.run("Quicksort", SortingAlgorithms.quicksort);
    
        bridgesQuick.setDataStructure(plotQuick);
        bridgesQuick.visualize();
    
        Bridges bridgesInsertion = new Bridges(13, "RamleyHirn", "575356762377");
        bridgesInsertion.setTitle("Insertion Sort Performance");
        bridgesInsertion.setDescription("Runtime and Operation Count for Insertion Sort.");
    
        LineChart plotInsertion = new LineChart();
        SortingBenchmark benchInsertion = new SortingBenchmark(plotInsertion);
    
        benchInsertion.linearRange(10000, 5000000, 20);
        benchInsertion.run("Insertion Sort", SortingAlgorithms.insertionSortStandalone);
    
        bridgesInsertion.setDataStructure(plotInsertion);
        bridgesInsertion.visualize();

    }
}
    
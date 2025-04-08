import bridges.base.LineChart;
import bridges.connect.Bridges;
import bridges.validation.RateLimitException;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class lineChart 
{
    public static void main(String[] args) throws IOException, RateLimitException, InterruptedException
    {
        Bridges bridges = new Bridges(6,"RamleyHirn","575356762377");

        LineChart plot = new LineChart();
        plot.setTitle("Linechart plot");

        double x1[] = new double[] {1, 3, 5, 20};
        double y1[] = new double[] {2, 3, 5, 20};
        plot.setDataSeries("1", x1, y1);
        bridges.setDataStructure(plot);
        bridges.visualize();

        double x2[] = new double[] {2, 15.2, 40};
        double y2[] = new double[] {4, 30.5, 400.99};
        plot.setDataSeries("2", x2, y2);
        bridges.visualize();

        plot.toggleLogarithmicX(true);

        bridges.visualize();

        plot.toggleMouseTrack(true);
        plot.toggleLogarithmicY(true);

        bridges.visualize();
    }

}

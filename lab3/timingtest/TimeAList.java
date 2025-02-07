package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> ns01 = new AList<Integer>();
        AList<Double> times01 = new AList<Double>();
        AList<Integer> opcounts01 = new AList<>();


        int maxnum = 128000;

        for (int cycle = 1000; cycle <= maxnum;cycle = cycle *2) {
            AList<Integer> test = new AList<>();
            int countnum = 0;
            Stopwatch sw = new Stopwatch();
            for (int i = 1; i <= cycle; i++) {
                test.addLast(0);
                countnum++;
            }
            double timeInSeconds = sw.elapsedTime();
            ns01.addLast(cycle);
            times01.addLast(timeInSeconds);
            opcounts01.addLast(countnum);
        }

        printTimingTable(ns01, times01, opcounts01);

    }
}

import crawler.escalonadorCurtoPrazo.EscalonadorSimples;
import crawler.escalonadorCurtoPrazo.PageFetcherImp;

import java.net.MalformedURLException;

public class Main {
    private static final int THREAD_20 = 20;
    private static final int THREAD_40 = 40;
    private static final int THREAD_60 = 60;
    private static final int THREAD_80 = 80;
    private static final int THREAD_100 = 100;

    private static long startTime;
    private static long endTime;

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        startTime = System.currentTimeMillis();

        EscalonadorSimples escalonadorSimples = new EscalonadorSimples();

        for (int i = 0; i < THREAD_100; i++) {
            Thread newThread = new Thread() {
                @Override
                public void run() {
                    PageFetcherImp pageFetcherImp1 = new PageFetcherImp(escalonadorSimples);
                    pageFetcherImp1.run();

                    endTime = System.currentTimeMillis();

                    long duration = (endTime - startTime);

                    System.out.println("finish duration with " +
                            String.valueOf(THREAD_100) + " threads: " +
                            String.valueOf(duration) + " milliseconds");
                }
            };

            newThread.start();
        }
    }
}

import crawler.escalonadorCurtoPrazo.EscalonadorSimples;
import crawler.escalonadorCurtoPrazo.PageFetcherImp;

public class Main {

    public static void main(String[] args) {
        EscalonadorSimples escalonadorSimples = new EscalonadorSimples();

        PageFetcherImp pageFetcherImp1 = new PageFetcherImp(escalonadorSimples);
        PageFetcherImp pageFetcherImp2 = new PageFetcherImp(escalonadorSimples);

        pageFetcherImp1.run();
        pageFetcherImp2.run();
    }
}

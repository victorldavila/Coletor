package crawler.escalonadorCurtoPrazo;

import com.trigonic.jrobotx.util.URLEncodeTokenizer;
import crawler.ColetorUtil;
import crawler.URLAddress;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PageFetcherImp implements PageFetcher{
    private EscalonadorSimples escalonadorSimples;

    public PageFetcherImp(EscalonadorSimples escalonadorSimples) {
        this.escalonadorSimples = escalonadorSimples;
    }

    @Override
    public void run() {
        System.out.println("start");

        while(!escalonadorSimples.finalizouColeta()) {
            URLAddress urlFile = escalonadorSimples.getURL();

            if (ColetorUtil.isAbsoluteURL(urlFile.getAddress())) {
                System.out.println(urlFile.getAddress());

                try {
                    InputStream inputStream = ColetorUtil.getUrlStream("", new URL(urlFile.getAddress()));
                    String result = ColetorUtil.consumeStream(inputStream);

                    decodeUrl(result);
                } catch (IOException e) {

                }
            }
        }
    }

    private void decodeUrl(String encoded) {
        URLEncodeTokenizer urlEncodeTokenizer = new URLEncodeTokenizer(encoded, "Sitemap");
        while (urlEncodeTokenizer.hasNext()) {
            URLEncodeTokenizer.Token token = urlEncodeTokenizer.next();
        }
    }
}

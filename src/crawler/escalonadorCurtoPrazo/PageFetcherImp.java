package crawler.escalonadorCurtoPrazo;

import com.trigonic.jrobotx.Record;
import crawler.ColetorUtil;
import crawler.URLAddress;

import java.net.MalformedURLException;

public class PageFetcherImp implements PageFetcher{
    private EscalonadorSimples escalonadorSimples = new EscalonadorSimples();

    @Override
    public void run() {
        try {
            URLAddress urlAddressEduardo = new URLAddress("www.casasbahia.com.br/robots.txt", 1);
            URLAddress urlAddressVictor = new URLAddress("www.bloomberg.com/robots.txt", 1);
            URLAddress urlLoraine = new URLAddress("www.casasbahia.com.br/robots.txt", 1);
            URLAddress urlVinicios = new URLAddress("economictimes.indiatimes.com/robots.txt", 1);

            escalonadorSimples.adicionaNovaPagina(urlAddressEduardo);
            escalonadorSimples.adicionaNovaPagina(urlAddressVictor);
            escalonadorSimples.adicionaNovaPagina(urlLoraine);
            escalonadorSimples.adicionaNovaPagina(urlVinicios);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        while(escalonadorSimples.finalizouColeta()) {
            URLAddress urlFile = escalonadorSimples.getURL();

            Record record = escalonadorSimples.getRecordAllowRobots(urlFile);
            escalonadorSimples.putRecorded(urlFile.getDomain(), record);

            if (record != null) {
                
            }
        }
    }
}

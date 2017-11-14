package crawler.escalonadorCurtoPrazo;

import com.trigonic.jrobotx.util.URLEncodeTokenizer;
import crawler.ColetorUtil;
import crawler.URLAddress;
import org.apache.commons.io.FileUtils;
import org.htmlcleaner.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PageFetcherImp implements PageFetcher{
    private EscalonadorSimples escalonadorSimples;

    public PageFetcherImp(EscalonadorSimples escalonadorSimples) {
        this.escalonadorSimples = escalonadorSimples;
    }

    @Override
    public void run() {
        while(!escalonadorSimples.finalizouColeta()) {
            URLAddress urlFile = escalonadorSimples.getURL();

            if (ColetorUtil.isAbsoluteURL(urlFile.getAddress())) {
                escalonadorSimples.countFetchedPage();

                System.out.println(urlFile.getAddress());

                try {
                    decodeUrl("Bot", urlFile.getAddress());
                } catch (IOException e1) {

                }
            }
        }
    }

    private void decodeUrl(String userAgent, String domain) throws IOException {
        Document html = Jsoup.connect(domain).userAgent(userAgent).get();
        Elements links = html.select("a[href]");

        for (Element link : links) {
            escalonadorSimples.adicionaNovaPagina(new URLAddress(link.attr("abs:href"), 1));
        }

        writeToFile(html);
    }

    public synchronized void writeToFile(Document html) throws IOException {
        final File f = new File("C:/Users/victo/IdeaProjects/Coletor/coleta/" + String.valueOf(escalonadorSimples.getSaveHtmlFile()) + ".html");
        FileUtils.writeStringToFile(f, html.outerHtml(), "UTF-8");

        //System.out.println("Save file numero: " + escalonadorSimples.getSaveHtmlFile());

        escalonadorSimples.countSaveHtmlFile();
    }
}

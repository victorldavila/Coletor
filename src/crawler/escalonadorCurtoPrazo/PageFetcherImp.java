package crawler.escalonadorCurtoPrazo;

import com.trigonic.jrobotx.util.URLEncodeTokenizer;
import crawler.ColetorUtil;
import crawler.URLAddress;
import org.htmlcleaner.*;

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
        //System.out.println("\nstart\n");

        while(!escalonadorSimples.finalizouColeta()) {
            URLAddress urlFile = escalonadorSimples.getURL();
            //System.out.println(urlFile);
            if (ColetorUtil.isAbsoluteURL(urlFile.getAddress())) {
                escalonadorSimples.countFetchedPage();
                try {
                    InputStream inputStream = ColetorUtil.getUrlStream("Bot",
                            new URL(urlFile.getAddress()));

                    decodeUrl(inputStream, urlFile.getAddress());
                } catch (IOException e1) {

                } catch (IllegalArgumentException e2) {

                }
            }
        }

        //System.out.println("\nEnd");
    }

    private void decodeUrl(InputStream inputStream, String domain) throws IOException {
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode node = cleaner.clean(inputStream);

        // traverse whole DOM and update images to absolute URLs
        node.traverse(new TagNodeVisitor() {
            public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
                if (htmlNode instanceof TagNode) {
                    TagNode tag = (TagNode) htmlNode;
                    String tagName = tag.getName();
                    if ("a".equals(tagName)) {
                        String href = tag.getAttributeByName("href");
                        if (href != null) {
                            try {
                                escalonadorSimples.adicionaNovaPagina(new URLAddress(href, 1));
                            } catch (MalformedURLException e) {

                            }
                            //tag.setAttribute("href", Utils.fullUrl(siteUrl, src));
                        }
                    }
                } else if (htmlNode instanceof CommentNode) {
                    CommentNode comment = ((CommentNode) htmlNode);
                    //comment.getContent().append(" -- By HtmlCleaner");
                }
                // tells visitor to continue traversing the DOM tree
                return true;
            }
        });

        SimpleHtmlSerializer serializer =
                new SimpleHtmlSerializer(cleaner.getProperties());
        serializer.writeToFile(node, domain);
    }
}

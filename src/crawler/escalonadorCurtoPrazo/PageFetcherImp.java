package crawler.escalonadorCurtoPrazo;

import com.trigonic.jrobotx.util.URLEncodeTokenizer;
import crawler.ColetorUtil;
import crawler.URLAddress;
import org.htmlcleaner.*;

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
        System.out.println("\nstart\n");

        while(!escalonadorSimples.finalizouColeta()) {
            URLAddress urlFile = escalonadorSimples.getURL();

            if (ColetorUtil.isAbsoluteURL(urlFile.getAddress())) {
                System.out.println(urlFile.getAddress());

                try {
                    InputStream inputStream = ColetorUtil.getUrlStream("Bot",
                            new URL(urlFile.getAddress()));
                    String result = ColetorUtil.consumeStream(inputStream);

                    decodeUrl(result);
                } catch (IOException e) {

                }
            }
        }
    }

    private void decodeUrl(String encoded) throws IOException {
        System.out.println(encoded);
        HtmlCleaner cleaner = new HtmlCleaner();
        final String siteUrl = "http://www.themoscowtimes.com/";

        TagNode node = cleaner.clean(new URL(siteUrl));

        // traverse whole DOM and update images to absolute URLs
        node.traverse(new TagNodeVisitor() {
            public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
                if (htmlNode instanceof TagNode) {
                    TagNode tag = (TagNode) htmlNode;
                    String tagName = tag.getName();
                    if ("a".equals(tagName)) {
                        String href = tag.getAttributeByName("href");
                        System.out.println(href);
                        if (href != null) {
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
        serializer.writeToFile(node, "themoscowtimes.txt");
    }
}

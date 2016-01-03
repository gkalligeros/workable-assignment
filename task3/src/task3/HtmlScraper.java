package task3;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlScraper {
	public static final String WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/";

	public String get_first_paragraph(String actor_name) {
		String actor_url = WIKIPEDIA_URL + actor_name.replace(" ", "_");
		Element body;
		Element content;
		Elements paragraphs;
		Element fisrt_paragraph;
		Document doc;
		try {
			doc = Jsoup.connect(actor_url).get();
			body = doc.body();
			content = body.getElementById("mw-content-text");
			paragraphs = content.getElementsByTag("p");
			fisrt_paragraph = paragraphs.get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "not found";
		}
		System.out.println(fisrt_paragraph.text());
		return fisrt_paragraph.text();

	}
}

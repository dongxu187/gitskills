package tasks;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;

/*
 * 一点推荐
 */
public class YidianRecommend {
	private WebClient webClient = new WebClient(BrowserVersion.CHROME);

	public YidianRecommend() {
		this.webClient.getOptions().setCssEnabled(false);
		this.webClient.getOptions().setJavaScriptEnabled(false);
	}

	public void run() {

		String requestUrl = "https://a1.go2yd.com/Website/channel/best-news?platform=1&fields=docid&fields=date&fields=url";
		try {
			WebRequest request = new WebRequest(new URL(requestUrl));
			request.setAdditionalHeader("Cookie", "JSESSIONID=uND6ioTAnYEU8tLWy8mNng");
			JSONObject dataJson = new JSONObject(
					webClient.getWebConnection().getResponse(request).getContentAsString());
			JSONArray dataArray = dataJson.getJSONArray("result");
			for (int m = 0; m < dataArray.length(); m++) {
				JSONObject temp = dataArray.getJSONObject(m);
				if (temp.has("url") && temp.has("ctype") && temp.has("date") && temp.has("title")
						&& temp.getString("ctype").equals("news")) {
					HtmlPage htmlpage = null;
					htmlpage = (HtmlPage) webClient.getPage(temp.getString("url"));
					Iterator<DomElement> it = htmlpage.getDocumentElement().getDomElementDescendants().iterator();
					DomElement element = null;
					DomElement target = null;
					while (it.hasNext()) {
						element = it.next();
						if (element.getAttribute("class").equals("content-bd")) {
							target = element;
							break;
						}
					}

					if (target != null) {
						String text = "";
						Iterator<DomElement> itchi = target.getChildElements().iterator();
						DomElement child;
						while (itchi.hasNext()) {
							child = itchi.next();
							if (child.asText().length() != 0)
								text = text.concat(child.asText()) + "\n";
						}

						System.out.println("[标题：" + temp.getString("title") + "]");
						System.out.println("[发布时间：" + temp.getString("date") + "]");
						System.out.println("[地址：" + temp.getString("url") + "]");
						System.out.println("[摘要："
								+ (text.length() < 60 ? text : text.substring(0, 58)).replaceAll("\\s*", "").trim()
								+ "...]");
						System.out.println(text);
						System.out.println(
								"********************************************************************************************************");
					}

				}

			}

		} catch (JSONException | FailingHttpStatusCodeException | IOException e) {
			
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new YidianRecommend().run();

	}

}
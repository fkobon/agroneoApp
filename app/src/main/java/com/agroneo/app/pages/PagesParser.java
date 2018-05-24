package com.agroneo.app.pages;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agroneo.app.R;
import com.agroneo.app.utils.ImageLoader;
import com.agroneo.app.utils.Json;
import com.agroneo.app.utils.views.RatioImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PagesParser {

    private LinearLayout container;

    public PagesParser(LinearLayout container) {
        this.container = container;
    }


    public void parse(Json page) {


        container.removeAllViews();


        TextView title = new TextView(container.getContext());
        title.setTextAppearance(container.getContext(), R.style.bigtext);

        title.setText(page.getString("top_title", page.getString("title")));
        container.addView(title);

        if (!page.getString("intro", "").equals("")) {

            TextView intro = new TextView(container.getContext());
            intro.setTextAppearance(container.getContext(), R.style.mediumtext);

            intro.setText(page.getString("intro"));
            container.addView(intro);
        }

        if (page.getString("logo") != null) {
            RatioImageView logo = new RatioImageView(container.getContext(),240F/360F);
            container.addView(logo);
            ImageLoader.setImage(page.getString("logo") + "@360x240.jpg", logo);
        }

        if (!page.getString("text", "").equals("")) {

            TextView textbox = new TextView(container.getContext());
            textbox.setTextAppearance(container.getContext(), R.style.text);

            String text = page.getString("text");
            text = parseWiki(text);
            sectionNizer(text);
            text = parseLinks(text, page.getListJson("links"));
            text = parsePhotos(text);

            textbox.setText(Html.fromHtml(text));

            textbox.setMovementMethod(LinkMovementMethod.getInstance());


            container.addView(textbox);
        }
    }


    private String insertPub(String text, String pub) {
        if (text == null) {
            return "";
        }
        pub += "\n";
        Pattern p = Pattern.compile("(<section[^>]+>)", Pattern.MULTILINE);

        String[] spl = p.split(text);
        if (spl.length < 3) {
            return text;
        }
        double poz = Math.floor(spl.length / 2);
        Matcher m = p.matcher(text);
        while (m.find()) {
            if (poz-- == 0D) {
                text = text.substring(0, m.start()) + pub + text.substring(m.start());
            }
        }
        return text;
    }

    private String parseWiki(String text) {
        text = text.replaceAll("[\r]", "").replaceAll("( ){2,}", " ");

        StringWriter parsed_text_oul = new StringWriter();
        String[] text_list = text.split("\n");
        boolean ul_opened = false;
        boolean ol_opened = false;
        for (String line : text_list) {
            if (line.startsWith("*")) {
                if (!ul_opened) {
                    parsed_text_oul.write("<ul>\n");
                }
                ul_opened = true;
                parsed_text_oul.write("<li>" + line.substring(1) + "</li>\n");
            } else if (line.startsWith("#")) {
                if (!ol_opened) {
                    parsed_text_oul.write("<ol>\n");
                }
                ol_opened = true;
                parsed_text_oul.write("<li>" + line.substring(1) + "</li>\n");

            } else {
                if (ol_opened) {
                    parsed_text_oul.write("</ol>\n\n");
                    ol_opened = false;
                }
                if (ul_opened) {
                    parsed_text_oul.write("</ul>\n\n");
                    ul_opened = false;
                }
                parsed_text_oul.write(line + "\n");
            }
        }
        text = parsed_text_oul.toString();

        String[] text_lines = text.split("([\\s\\u0085\\p{Z}]+){2,}");
        StringWriter parsed_text_hp = new StringWriter();
        for (String line : text_lines) {
            Matcher mh1 = Pattern.compile("^([\r\n]+)?([ ]+)?= ?([^=]+) ?=([ ]+)?([\r\n]+)?$").matcher(line);
            line = mh1.replaceAll("<h1>$3</h1>");
            if (!mh1.matches()) {
                Matcher mh2 = Pattern.compile("^([\r\n]+)?([ ]+)?== ?([^=]+) ?==([ ]+)?([\r\n]+)?$").matcher(line);
                line = mh2.replaceAll("<h2>$3</h2>");
                if (!mh2.matches()) {
                    Matcher mh3 = Pattern.compile("^([\n\n]+)?([ ]+)?=== ?([^=]+) ?===([ ]+)?([\r\n]+)?$").matcher(line);
                    line = mh3.replaceAll("<h3>$3</h3>");
                    if (!mh3.matches()) {
                        Matcher mh4 = Pattern.compile("^([\n\n]+)?([ ]+)?==== ?([^=]+) ?====([ ]+)?([\r\n]+)?$").matcher(line);
                        line = mh4.replaceAll("<h4>$3</h4>");
                        if (!mh4.matches()) {
                            if (!line.equals("")) {
                                line = "<p>" + line + "</p>";
                            }
                            line = line.replaceAll("([a-zA-Z0-9\\.\\;\\, ]+)\n", "$1<br/>");
                        }
                    }
                }
            }

            parsed_text_hp.write(line.replace(" </", "</") + "\n\n");
        }
        text = parsed_text_hp.toString();

        return text;

    }

    private String sectionNizer(String text) {
        Document html = Jsoup.parse(text);
        int i = 1;
        for (int hn = 6; hn >= 1; hn--) {
            Iterator<Element> elements = html.select("body > h" + hn).iterator();
            while (elements.hasNext()) {
                Element hx = elements.next();
                hx.attr("itemprop", "alternateName");
                Element section = new Element(Tag.valueOf("section"), "");
                section.attr("itemprop", "articleSection");
                hx.before(section);
                Element next = hx.nextElementSibling();
                while ((next != null) && !next.tagName().matches("(h[1-6])")) {
                    section.appendChild(next);
                    next = hx.nextElementSibling();
                }
                section.prependChild(hx);
            }
        }

        Iterator<Element> sections_it = html.select("section").iterator();
        while (sections_it.hasNext()) {
            Element section = sections_it.next();
            Element child = section.children().first();
            if ((child != null) && child.tagName().matches("(h[1-6])")) {

                /// jumb String id = Fx.cleanURL(child.text()).replace("-", "_");
                //				child.before(new Element(Tag.valueOf("a"), "").attr("name", id));
            }
        }

        Iterator<Element> elements = html.select("body > *").iterator();
        Element section = null;
        while (elements.hasNext()) {
            Element element = elements.next();
            if (element.tagName().equals("section")) {
                section = null;
            } else {
                if (section == null) {
                    section = new Element(Tag.valueOf("section"), "");
                    element.before(section);
                    section.appendChild(element);
                } else {
                    section.appendChild(element);
                }
            }

        }
        text = html.select("body").html();
        return text;
    }

    private String parseLinks(String text, List<Json> links) {

        Json lnks = new Json();
        for (Json link : links) {
            lnks.put(link.getId(), link);
        }
        Pattern pattern = Pattern.compile("\\[Pages\\(([a-z0-9]+)\\) ([^\\]]+)\\]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            try {
                if (lnks.containsKey(matcher.group(1))) {
                    Json linked_page = lnks.getJson(matcher.group(1));
                    String title = linked_page.getString("top_title");
                    if ((title == null) || (title == "")) {
                        title = linked_page.getString("title");
                    }
                    text = text.replace(matcher.group(), "<a href=\"agroneo:/" + linked_page.getString("url") + "\">" + matcher.group(2) + "</a>");
                } else {
                    text = text.replace(matcher.group(), matcher.group(2));
                }
            } catch (Exception e) {

            }
        }
        return text;
    }

    private String parsePhotos(String text) {

        int width = 360;
        int height = 240;
        Pattern pattern = Pattern.compile("\\[Photos\\(([a-z0-9]+)\\)\\|?([^]]+)?]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {

            String key = matcher.group(1);
            String info = matcher.group(2) != null ? matcher.group(2) : "";
            text = text.replace(matcher.group(), "\n<div class=\"img\">\n" + "<a href=\"/files/" + key + "\">" + "<img width=\"" + width + "\" height=\"" + height + "\" alt=\"" + info + "\" src=\"/files/" + key + "@" + width + "x" + height + ".jpg\" />" + "</a>\n<legend>" + info + "</legend>\n</div>\n");
        }
        return text;
    }
}

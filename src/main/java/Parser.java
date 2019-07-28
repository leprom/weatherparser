import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    // Получает страницу
    public static Document getPage() throws IOException {
        String url = "https://www.pogoda.spb.ru/";

        // Парсит страницу
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    // Регулярное выражание
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()){
            return matcher.group();
        }else throw new Exception("Can't extract date from String!");
    }

    private static int printFourValues(Elements values, int index){
        int iterationCount = 4;

        if(index==0) {
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            if (isMorning) iterationCount = 3;
        }

        for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index + 1);
                for (Element td: valueLine.select("td")
                ) {
                    System.out.print(td.text()+ "    ");
                }
                System.out.println();
            }
        return iterationCount;
        }


    public static void main(String[] args) throws Exception {
        //System.out.println(getPage());
        Document page = getPage();

        // Парсит только таблтцу
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;


        for (Element name: names
             ) {

            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "      Явления     Температура     Давл        Влажность       Ветер");
            int iterationCount = printFourValues(values, index);
            index = index + iterationCount;
        }


    }

}

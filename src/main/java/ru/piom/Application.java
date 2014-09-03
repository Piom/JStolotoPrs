package ru.piom;

import au.com.bytecode.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Application {

    private static final String URL_ARG = "http://www.stoloto.ru/6x45/archive/";

    public static void main(String[] args) {
        try {
            String url;
            CSVWriter writer = new CSVWriter(new FileWriter("result.csv"), ';');
            int value;
            Integer text;
            String item;

            Map<Integer, Integer> numbers = new HashMap<Integer, Integer>();

            for (int i = 1; i <= 939; i++) {
                url = URL_ARG + i;
                System.out.println("i = " + i);
                Document doc  = Jsoup.connect(url).get();
                Iterator<Element> iterator = doc.select("p.number").iterator();
                while (iterator.hasNext()){
                    try {
                        item = iterator.next().text();
                        if (item.isEmpty()) {
                            continue;
                        }
                        text = Integer.valueOf(item);
                        if ( numbers.get(text)!= null){
                            value = numbers.get(text.intValue());
                            numbers.put(text, ++value);
                        }else{
                            numbers.put(text,1);
                        }
                    } catch (NumberFormatException e){
                        System.out.println("e = " + e);
                    }
                }
            }
            List<Map.Entry<Integer,Integer>> entrylist = new ArrayList(numbers.entrySet());

            Collections.sort(entrylist,new ValuesComparator());
            System.out.println("Sort by value: " + entrylist);
            List<String[]> entries = new ArrayList<String[]>();
            for (int i = 0; i < entrylist.size(); i++) {
                String[] str = new String[2];
                str[0]= entrylist.get(i).getKey().toString();
                str[1]= entrylist.get(i).getValue().toString();
                entries.add(str);
            }

            writer.writeAll(entries);
            writer.close();


            System.out.println("Complete");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class ValuesComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        Map.Entry e1 = (Map.Entry) o1;
        Map.Entry e2 = (Map.Entry) o2;
        Integer c1 = (Integer)e1.getValue();
        Integer c2 = (Integer)e2.getValue();
        return c2-c1;
    }
}
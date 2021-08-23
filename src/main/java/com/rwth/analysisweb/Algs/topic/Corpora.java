package com.rwth.analysisweb.Algs.topic;

import java.io.*;
import java.util.*;

public class Corpora {
    private ArrayList<TextVector> corpus;

    Corpora(Dictionary dict, ArrayList<String[]> rawdata) {
        corpus = new ArrayList<>();
        int size = rawdata.size();
        for (int i = 0; i < size; i++) {
            TextVector vec = new TextVector(dict, rawdata.get(i));
            corpus.add(vec);
        }
    }

    public void WriteBleibCorpus(String fileName) throws FileNotFoundException {
        File outFile = new File(fileName);
        FileOutputStream fos1 = new FileOutputStream(outFile, false);
        int size = corpus.size();
        //System.out.println(size);
        PrintStream out1 = new PrintStream(fos1);
        for (int i = 0; i < size; i++) {
            String str = corpus.get(i).sum + " ";
            Set set = corpus.get(i).numOfword.entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iter.next();
                str = str + entry.getKey().toString() + ":" + entry.getValue().toString() + " ";
            }
            str = str.trim() + "\r\n";
            out1.print(str);

        }
        out1.close();

    }

    public int length() {
        return corpus.size();
    }

    public static void main(String[] args) throws Exception {
        ArrayList<String[]> test = new ArrayList<>();
        String[] str1 = {"ab", "dc", "cd", "ef"};
        String[] str2 = {"ab", "ef", "mn", "cd", "bc"};
        String[] str3 = {"ab"};
        test.add(str1);
        test.add(str2);
        test.add(str3);
        Dictionary dic = new Dictionary(test, "Hannover", "dblp");
        dic.removeonce();

        Corpora cop = new Corpora(dic, test);
        cop.WriteBleibCorpus("/Users/xingran/Desktop/data/test.dat");
    }

}

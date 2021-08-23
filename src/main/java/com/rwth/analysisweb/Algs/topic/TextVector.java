package com.rwth.analysisweb.Algs.topic;

import java.util.HashMap;

public class TextVector {
    int sum;
    HashMap<Integer, Integer> numOfword;

    TextVector(Dictionary dict, String[] text) {
        numOfword = new HashMap<>();
        for (String word : text) {
            if (dict.getDict().containsKey(word)) {
                int id = dict.getDict().get(word);
                if (numOfword == null || !numOfword.containsKey(id)) {
                    numOfword.put(id, 1);
                } else {
                    int num = numOfword.get(id) + 1;
                    numOfword.put(id, num);
                }
            }
        }
        sum = numOfword.size();
    }
}

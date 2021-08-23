package com.rwth.analysisweb.Algs.topic;

import java.io.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DynamicLDA {

    static String database = "DBLP";
    static String prefix = "/Users/xingran/Desktop/data/";
    static String runPath = "/Users/xingran/Desktop/dtm-master/dtm/main";
    private final Corpora corpus;
    private final LinkedHashMap<Integer, Integer> timeslices;
    private final Dictionary dictionary;
    private final ArrayList<Paper> ids;
    private final ArrayList<String[]> rawdata;
    public String org;
    public int ntopics;
    public String start;
    public String end;
    public double[][] gamma;//gamma[5,10] is the proprtion of the 10th topic in doc5
    public double[][] topicEvolu;
    //parameters to run DLDA
    String mode = "fit";
    int rng_seed = 0;
    boolean initialize_lda = true;
    //corpus_prefix=example/test \
    //outname=example/model_run \
    double top_chain_var = 0.005;
    double alpha = 0.01;
    int lda_sequence_min_iter = 6;
    int lda_sequence_max_iter = 20;
    int lda_max_em_iter = 10;

    public DynamicLDA(String org, int n, int start, int end) throws SQLException {
        this.org = org;
        this.ntopics = n;
        this.start = start + "";
        this.end = end + "";

        this.ids = new ArrayList<>();
        this.timeslices = new LinkedHashMap<>();
        this.rawdata = getData();
        this.dictionary = buildDict();
        this.corpus = buildCorpus();


        this.gamma = new double[corpus.length()][timeslices.size()];
        this.topicEvolu = new double[ntopics][timeslices.size()];


    }

    private static void testtopk() {
        double[] test = {0.05, 0.64, 0.51, 0.08, 0.09};
        Queue<Word> queue = new PriorityQueue<>(new Comparator<Word>() {
            @Override
            public int compare(Word e1, Word e2) {
                if (e2.prob - e1.prob >= 0) return -1;
                else return 1;
            }
        });
        int i = 0;
        for (double num : test) {
            if (queue.size() < 3) {
                queue.add(new Word(i, num));
            } else if (queue.peek().prob < num) {
                queue.poll();
                queue.add(new Word(i, num));
            }
            i++;
        }
        List<Double> list = new ArrayList<>();
        while (!queue.isEmpty()) {
            list.add(queue.poll().prob);
        }

        System.out.println(list);
    }

    public static void main(String[] args) throws Exception {
        String[] orgs = {"Hannover", "KIT", "RWTH", "TUM", "TUBerlin", "Stuttgart", "TUBerlin", "TUBraunschweig", "TUDresden"};
        for (String org : orgs) {
            DynamicLDA lda = new DynamicLDA(org, 20, 1900, 2020);
            lda.wirte_prob2DB();
        }


    }

    private ArrayList<String[]> getData() {

        ArrayList<String[]> abs_data = new ArrayList<>();
        try {
            String sql = "Select paper_id,paper_year,abstraction from " + "Hannover" + " where paper_year>= " + start + " and paper_year<=" + end + " order by paper_year asc";
            DBConnection db = new DBConnection(database);
            ResultSet data = db.get(sql);

            while (data.next()) {

                long id = data.getLong(1);
                int year = data.getInt(2);
                String str = data.getString(3);


                if (str != null) {

                    if (timeslices == null) {
                        timeslices.put(year, 1);
                    } else {
                        if (!timeslices.containsKey(year)) {
                            timeslices.put(year, 1);
                        } else {
                            int num = timeslices.get(year) + 1;
                            timeslices.put(year, num);
                        }
                    }
                    String temp = str.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
                    String[] abs = temp.split(" ");
                    //System.out.println(id);
                    Paper p = new Paper(id, year);
                    ids.add(p);
                    abs_data.add(abs);
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abs_data;
    }

    private Dictionary buildDict() throws SQLException {
        Dictionary dic = new Dictionary(rawdata, org, database);
        System.out.println(dic.length());
        return dic;
    }

    private Corpora buildCorpus() {
        Corpora corp = new Corpora(dictionary, rawdata);
        return corp;
    }

    private void WriteTimeslices(String fileName) throws Exception {
        File outFile = new File(fileName);
        FileOutputStream fos1 = new FileOutputStream(outFile, false);
        PrintStream out1 = new PrintStream(fos1);
        String SEQ_LENGTH = timeslices.size() + "" + "\r\n";
        System.out.println(SEQ_LENGTH);
        out1.print(SEQ_LENGTH);
        for (Map.Entry<Integer, Integer> entry : timeslices.entrySet()) {
            String str = entry.getValue().toString() + "\r\n";
            out1.print(str);
        }
        out1.close();
    }

    private void convertInputData() throws Exception {
        String filenameOfcorpus = prefix + org + "/train-mult.dat";
        String filenameOftime = prefix + org + "/train-seq.dat";

        corpus.WriteBleibCorpus(filenameOfcorpus);
        WriteTimeslices(filenameOftime);

    }

    private void builDictandCorpusFromPython() {
        //String data=getData("DBLP");
        String data = "";
        String filepath = "python3 /Users/xingran/PycharmProjects/TopicModeling/CreateDic.py";
        String command = filepath + " " + org + " " + start + " " + end + " " + data;


        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            line = in.readLine();
            System.out.println(line);
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int re = process.waitFor();
            System.out.println(re == 1 ? "----状态码1----运行失败" : "----状态码0----运行成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void train() throws Exception {
        convertInputData();
        String train_data = prefix + org + "/train";
        String out = prefix + org + "/train_out";
        String cmd = runPath + " -ntopics " + ntopics + " -mode fit -rng_seed 0 -initialize_lda true -corpus_prefix " + train_data + " -outname " + out + " -top_chain_var 0.005 -alpha 0.01 -lda_sequence_min_iter " + this.lda_sequence_min_iter + " -lda_sequence_max_iter " + this.lda_sequence_max_iter + " -lda_max_em_iter " + this.lda_max_em_iter;
        try {
            final Process process = Runtime.getRuntime().exec(cmd);
            System.out.println("start run cmd=" + cmd);

            //处理InputStream的线程
            new Thread() {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null;

                    try {
                        while ((line = in.readLine()) != null) {
                            System.out.println("output: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line = null;

                    try {
                        while ((line = err.readLine()) != null) {
                            System.out.println("err: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            err.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            process.waitFor();
            System.out.println("finish run cmd=" + cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String fout_gamma() {
        return prefix + org + "/train_out/lda-seq/gam.dat";
    }

    private String fout_prob(int i) {
        String num = i + "";
        if (i < 10) num = "00" + num;
        else num = "0" + num;
        return prefix + org + "/train_out/lda-seq/topic-" + num + "-var-e-log-prob.dat";
    }

    private String fout_observation(int i) {
        String num = i + "";
        return prefix + "train_out/lda-seq/topic-" + num + "-var-obs.dat";
    }

    //write the proportion of each topic in documents to DB
    private void wirte_prob2DB() throws SQLException, IOException {
        DBConnection db = new DBConnection(database);
        String table = org + "_topicsDistribution";
        //db.statement.executeUpdate("create table test(id varchar(200),topic0 varchar(200))");
        db.createTable(TableOfdistribution(table), table);
        String sql = InsertTopicSQL(table);
        PreparedStatement preparedStatement = db.conn.prepareStatement(sql);


        BufferedReader in = new BufferedReader(new FileReader(fout_gamma()));
        String line = in.readLine();
        int num = 0;
        Double[] proportion = new Double[ntopics];
        while (line != null) {
            double sum = 0;
            for (int i = 0; i < ntopics; i++) {
                double p = Math.exp(Double.parseDouble(line));
                double val = new BigDecimal(p).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                sum += val;
                proportion[i] = val;
                num++;
                line = in.readLine();
            }
            //System.out.println(num/20);
            Long id = ids.get(num / 20 - 1).id;
            preparedStatement.setLong(1, id);
            preparedStatement.setInt(2, ids.get(num / 20 - 1).year);
            for (int j = 0; j < ntopics; j++) {
                preparedStatement.setDouble(j + 3, proportion[j] / sum);
                //proportion[j]=proportion[j]/sum;
                //System.out.println(proportion[j]);
            }
            preparedStatement.addBatch();
            if ((num / 20 + 1) % 500 == 0) {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }

        }
        preparedStatement.executeBatch();

    }

    private String TableOfdistribution(String table) {
        StringBuffer sql = new StringBuffer("create table " + table + "(paper_id varchar(200), paper_year int");
        for (int i = 0; i < ntopics; i++) {
            String index = i + "";
            sql.append(",topic" + index + " double(5,4)");
        }
        sql.append(")");
        System.out.println(sql);
        return sql.toString();
    }

    private String InsertTopicSQL(String table) {
        StringBuffer sql = new StringBuffer("insert into " + table);
        sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        return sql.toString();
    }

    private void writetopicInfo2DB() throws SQLException, IOException {
        DBConnection db = new DBConnection(database);
        String table = org + "_topicsEvolution";
        db.createTable("create table " + table + "(timesliceID int,topic_id int,word_id int,prob double(5,4))", table);
        String sql = InsertTopicSQL(table);
        PreparedStatement preparedStatement = db.conn.prepareStatement(sql);
        int timeNum = timeslices.size();
        int wordNum = dictionary.getDict().size();
        Queue<Word> queue = new PriorityQueue<>(new Comparator<Word>() {
            @Override
            public int compare(Word e1, Word e2) {
                if (e2.prob - e1.prob >= 0) return -1;
                else return 1;
            }
        });


        for (int topic = 0; topic < 20; topic++) {
            BufferedReader in = new BufferedReader(new FileReader(fout_prob(topic)));
            for (int time = 0; time < timeNum; time++) {
                for (int word = 0; word < wordNum; word++) {
                    String line = in.readLine();
                    double p = Math.exp(Double.parseDouble(line));
                    double prob = new BigDecimal(p).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (queue.size() < 50) {
                        queue.offer(new Word(word, prob));
                    } else if (queue.peek().prob < prob) {
                        queue.poll();
                        queue.offer(new Word(word, prob));
                    }
                }
                while (!queue.isEmpty()) {
                    preparedStatement.setInt(1, time);
                    preparedStatement.setInt(2, topic);
                    preparedStatement.setInt(3, queue.peek().id);
                    preparedStatement.setDouble(4, queue.peek().prob);
                    queue.poll();
                    preparedStatement.addBatch();
                }
            }
        }
        preparedStatement.executeBatch();
    }

    private void writeTimeInfo2DB() throws SQLException {
        DBConnection db = new DBConnection(database);
        String table = org + "_timeslices";
        db.createTable("create table " + table + "(timesliceID int,year int,paperNums int)", table);
        String sql = InsertTopicSQL(table);
        String sqlInsert = "insert into " + table + " values(?,?,?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(sqlInsert);
        int i = 0;

        for (Map.Entry<Integer, Integer> e : timeslices.entrySet()) {
            preparedStatement.setInt(1, i);
            preparedStatement.setInt(2, e.getKey());
            preparedStatement.setInt(3, e.getValue());
            preparedStatement.addBatch();
            i++;
        }
        preparedStatement.executeBatch();
    }

    private static class Word {
        public int id;
        public double prob;

        public Word(int i, double p) {
            this.id = i;
            this.prob = p;
        }
    }

    private static class Paper {
        public long id;
        public int year;

        public Paper(long id, int year) {
            this.id = id;
            this.year = year;
        }
    }
}






















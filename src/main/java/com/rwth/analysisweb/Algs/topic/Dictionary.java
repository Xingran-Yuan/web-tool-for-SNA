package com.rwth.analysisweb.Algs.topic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Dictionary {
    private String org;
    public ArrayList<String[]> rawdata;
    private HashMap<String,int[]> word2ids;
    private HashMap<String,Integer>word2id;
    private String database;


    Dictionary(ArrayList<String[]> data,String orgnization,String db)throws SQLException{
        this.database=db;
        this.org=orgnization;
        this.rawdata=data;
        this.word2id=new HashMap<>();
        this.word2ids=new HashMap<>();
        int cnt=0;
        int size=rawdata.size();
        for(int i=0;i<size;i++){
            String[] temp=rawdata.get(i);
            int len=temp.length;
            for(int j=0;j<len;j++){
                if(word2ids==null||!word2ids.containsKey(temp[j])){
                    int[] ids={cnt++,1};
                    word2ids.put(temp[j],ids);
                }
                else{
                    int []ids=word2ids.get(temp[j]);
                    ids[1]+=1;//freq+1;
                    word2ids.put(temp[j],ids);
                }
                //System.out.println(i);
            }
        }
        removeonce();
        writeDic2DB();

    }



    public HashMap<String,Integer>getDict(){
        return word2id;
    }
    //romove once appeared tokens
    public void removeonce(){
        if(word2ids!=null){
            word2ids.entrySet().removeIf(entry -> entry.getValue()[1]==1);
        }
    }

    public int length(){return word2id.size();}


    public void writeDic2DB()throws SQLException {
        DBConnection db=new DBConnection(database);
        String table=org+"_dictionary";
        //db.statement.executeUpdate("create table test(id varchar(200),topic0 varchar(200))");
        String buildtable="create table "+table+"(id int,word varchar(45))";
        db.createTable(buildtable,table);
        String sql="insert into "+table+" values(?,?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(sql);

        int i=0;
        for (Map.Entry<String, int[]> entry : word2ids.entrySet()){
            word2id.put(entry.getKey(),i);
            preparedStatement.setInt(1,i+1);
            preparedStatement.setString(2,entry.getKey());
            preparedStatement.addBatch();
            i++;

            if ((i+1) % 500 == 0) {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }
        }
        preparedStatement.executeBatch();
    }


    public static void main(String[] args) throws SQLException {

    }





}

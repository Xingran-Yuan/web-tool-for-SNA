package com.rwth.analysisweb.Entity.TopicEntity;

public class TopicDistributionEntity {
    private String paper_id;
    private int paper_year;
    private double topic0=0;
    private double topic1=0;
    private double topic2;
    private double topic3;
    private double topic4;
    private double topic5;
    private double topic6;
    private double topic7;
    private double topic8;
    private double topic9;
    private double topic10;
    private double topic11;
    private double topic12;
    private double topic13;
    private double topic14;
    private double topic15;
    private double topic16;
    private double topic17;
    private double topic18;
    private double topic19;

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paperId) {
        this.paper_id = paperId;
    }

    public int getPaper_year() {
        return paper_year;
    }

    public void setPaper_year(int year) {
        this.paper_year= year;
    }

    public double getTopic0() {
        return topic0;
    }

    public void setTopic0(double topic0) {
        this.topic0 = topic0;
    }

    public double getTopic1() {
        return topic1;
    }

    public void setTopic1(double topic1) {
        this.topic1 = topic1;
    }

    public double getTopic2() {
        return topic2;
    }

    public void setTopic2(double topic2) {
        this.topic2 = topic2;
    }

    public double getTopic3() {
        return topic3;
    }

    public void setTopic3(double topic3) {
        this.topic3 = topic3;
    }

    public double getTopic4() {
        return topic4;
    }

    public void setTopic4(double topic4) {
        this.topic4 = topic4;
    }

    public double getTopic5() {
        return topic5;
    }

    public void setTopic5(double topic5) {
        this.topic5 = topic5;
    }

    public double getTopic6() {
        return topic6;
    }

    public void setTopic6(double topic6) {
        this.topic6 = topic6;
    }

    public double getTopic7() {
        return topic7;
    }

    public void setTopic7(double topic7) {
        this.topic7 = topic7;
    }

    public double getTopic8() {
        return topic8;
    }

    public void setTopic8(double topic8) {
        this.topic8 = topic8;
    }

    public double getTopic9() {
        return topic9;
    }

    public void setTopic9(double topic9) {
        this.topic9 = topic9;
    }

    public double getTopic10() {
        return topic10;
    }

    public void setTopic10(double topic10) {
        this.topic10 = topic10;
    }

    public double getTopic11() {
        return topic11;
    }

    public void setTopic11(double topic11) {
        this.topic11 = topic11;
    }

    public double getTopic12() {
        return topic12;
    }

    public void setTopic12(double topic12) {
        this.topic12 = topic12;
    }

    public double getTopic13() {
        return topic13;
    }

    public void setTopic13(double topic13) {
        this.topic13 = topic13;
    }

    public double getTopic14() {
        return topic14;
    }

    public void setTopic14(double topic14) {
        this.topic14 = topic14;
    }

    public double getTopic15() {
        return topic15;
    }

    public void setTopic15(double topic15) {
        this.topic15 = topic15;
    }

    public double getTopic16() {
        return topic16;
    }

    public void setTopic16(double topic16) {
        this.topic16 = topic16;
    }

    public double getTopic17() {
        return topic17;
    }

    public void setTopic17(double topic17) {
        this.topic17 = topic17;
    }

    public double getTopic18() {
        return topic18;
    }

    public void setTopic18(double topic18) {
        this.topic18 = topic18;
    }

    public double getTopic19() {
        return topic19;
    }

    public void setTopic19(double topic19) {
        this.topic19 = topic19;
    }

    public double getTopic(int topicID){
        switch (topicID){
            case 0:
                return topic0;
            case 1:
                return topic1;
            case 2:
                return topic2;
            case 3:
                return topic3;
            case 4:
                return topic4;
            case 5:
                return topic5;
            case 6:
                return topic6;
            case 7:
                return topic7;
            case 8:
                return topic8;
            case 9:
                return topic9;
            case 10:
                return topic10;
            case 11:
                return topic11;
            case 12:
                return topic12;
            case 13:
                return topic13;
            case 14:
                return topic14;
            case 15:
                return topic15;
            case 16:
                return topic16;
            case 17:
                return topic17;
            case 18:
                return topic18;
            case 19:
                return topic19;
        }
        return 0;
    }
}

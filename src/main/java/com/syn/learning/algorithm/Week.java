package com.syn.learning.algorithm;

public enum Week {
    MONDAY("周一"),
    SUNDAY("周日");

    private String value;

    Week(String str){
        this.value=str;
    }

    public String getValue(){
        return this.value;
    }
}

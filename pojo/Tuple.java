package com.tanyongfeng.pojo;

//定义二元关系
public class Tuple {
    private int key;
    private int value;
    public Tuple(int key, int value) {
        this.key = key;
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
    public int getKey(){
        return  this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

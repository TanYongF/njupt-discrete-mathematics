package com.tanyongfeng.until;

import com.tanyongfeng.pojo.Tuple;

import java.util.*;

public class Experiment {
    //编程实现整除这一偏序关系的的所有盖住关系的求取,并判定对应偏序集是否为格子
    //实验要求,给定任意的正整数,利用整除关系求其所所有因子构成的格,判断其是否有补格
    ArrayList tuples = new ArrayList<Tuple>();//定义整除关系系的偏序集合
    ArrayList gaizhuTuple = new ArrayList<Tuple>();//定义盖住关系的偏序集合
/*    public static void main(String args[]){

        Scanner scanner = new Scanner(System.in);
        int root = scanner.nextInt();


    }*/
    //求取所有盖住关系
    public ArrayList<Tuple> tupleInit(int num){
        ArrayList<Integer> factors = new ArrayList<>();//存储元素集合
        //求出所有的因子
        for (int i = 1 ; i <= Math.sqrt(num) ; i++){
            if (num % i == 0 ){
                factors.add(i);
                if (num / i != i) factors.add(num / i);
            }
        }
        //将因子进行排序
        factors.sort((o1, o2) -> o1 - o2);
        //根据整除关系进行生成tuples
        LinkedList<Tuple> tuples = new LinkedList<>();//定义tuple集合
        int size = factors.size();
        for (int i = 0 ; i < size - 1  ; i++){
            for (int j = i+1 ; j < size ; j++){
                if (factors.get(j) % factors.get(i) == 0){
                    Tuple tuple = new Tuple(factors.get(i), factors.get(j));
                    tuples.add(tuple);//添加这一对偏序集关系
                }
            }
        }
        ArrayList<Tuple> resultTuples = new ArrayList<>();
        //查找满足盖住关系的集合
        for (Tuple temp : tuples){//对每个关系遍历
            boolean ifGaizhu = true;//定义是否存在c满足(a,c)和(c,b),默认不存在
            for (int j = temp.getKey()+1; j < temp.getValue() ; j++){//对关系(a,b)之间的元素进行遍历
                int count = 0;
                if (ifGaizhu) {
                    for (Tuple tempTwo : tuples) {//对偏序集遍历,查看是否有满足满足(a,c)和(c,b)的关系
                        if ((tempTwo.getKey() == temp.getKey() && tempTwo.getValue() == j) ||
                                (tempTwo.getKey() == j && tempTwo.getValue() == temp.getValue())) {
                            count++;
                            if (count == 2) {
                                ifGaizhu = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (ifGaizhu) resultTuples.add(temp);
        }
        return resultTuples;//返回所有满足盖住关系的ArrayList集合
    }
    //判断是否为有补格 思想 : 任意取两个因子 判断两个因子的最小公倍数为 输入整数 最大公约数为1
    public boolean ComplementedLattice(ArrayList<Tuple> tuples,int root){
        //遍历所有的tuple 求出所有元素
        HashMap<Integer, Integer> itemMap = new HashMap<>();
        for (Tuple tuple : tuples){
            itemMap.put(tuple.getKey(),1);
            itemMap.put(tuple.getValue(),1);
        }
        Integer[] objects1 = itemMap.keySet().toArray(new Integer[0]);
        int[] itemArray= Arrays.stream(objects1).mapToInt(Integer::valueOf).toArray();
//        Integer[] itemArray =(Integer []) itemMap.keySet().toArray();
        for (int i = 0 ; i < itemArray.length ; i++){
            for (int j = i+1  ; j < itemArray.length ; j++){
                if (root == lcm(i,j) && gcd(i,j) == 1){
                    continue;
                }
                return false;

            }
        }
        return true;

    }
   //求最大公约数
    public int gcd(int item1, int item2){
        while (item2 != 0){
            int t = item2;
            item2 = item1 % item2;
            item1 = t;
        }
        return item1;

    }
    //求最小公倍数
    public int lcm(int item1, int item2){
        return item1 * item2 / gcd(item1,item2);
    }



}

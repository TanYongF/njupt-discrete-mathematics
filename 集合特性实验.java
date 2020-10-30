import java.util.*;

/*输入时 可以考虑自动生成集合关系 然后自己选择后填写到表达式中
* 将0和1表示集合关系对应的存在与否
* 例如：对于数据元素 a b c
* 则{<a,b>,<a,c>,<b,c>}
* 上述的集合关系可以标识为 011001000
* */
public class shiyanTwi {
    int size =0;//定义元素个数
    String[] ALPHA = new String[size];
    /*将输入的值转换为集合Set*/
    public HashSet<Tuple> StringtoSet(String shizi){
        HashSet<Tuple> tuples = new HashSet<>();
        for (int i  =0 ;i<shizi.length();i++){
            Tuple tuple = new Tuple("0","0");
            if (shizi.charAt(i) == '1'){
                tuple.setKey(ALPHA[i/size]);
                tuple.setValue(ALPHA[i%size]);
                tuples.add(tuple);
            }
        }
        return tuples;
    }
    /*打印关系集合*/
    public void printTuples(Set<Tuple> tuples){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        for (Tuple tuple : tuples){
            stringBuilder.append('<'+tuple.getKey()+','+tuple.getValue()+'>'+',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append('}');
        System.out.println("二元关系组为："+stringBuilder.toString());
    }
    /*判断输入的集合大小的五大特性*/
    public Boolean[] Judge(Set<Tuple> set){
        Boolean[] booleans = new Boolean[5];
        Arrays.fill(booleans,true);

        //当集合为空的时候
        if (set.size() == 0){
            booleans[0] = true;
            booleans[1] = true;
            booleans[2] = true;
            booleans[3] = false;
            booleans[4] = false;
        }
        //当集合为全集时候
        if (set.size() == size*size){
            booleans[0] = true;
            booleans[1] = true;
            booleans[2] = true;
            booleans[3] = false;
            booleans[4] = false;
        }
        //判断传递性
        boolean temp = true;
        try{
            for (Tuple tuple : set){
                for (Tuple tuple1 : set){
                    if (tuple.getValue().equals(tuple1.getKey())){
                        temp = false;
                        for (Tuple tuple2 : set){
                            if ((tuple2.getValue().equals(tuple1.getValue()) && tuple2.getKey().equals(tuple.getKey()))){
                                temp = true;
                                break;
                            }
                        }
                    }
                if (!temp){
                        throw  new Exception();
                    }
                }
            }
        }catch (Exception e){
            booleans[0] = temp;
        }

        //判断自反性和反自反性
        int size =0;
        for (Tuple tuple : set){
            if (tuple.getKey() == tuple.getValue()){
                size++;
            }
        }
         if (size != ALPHA.length){
             booleans[1] = false;  //非自反性
             if (size != 0){
                 booleans[3] = false; //非反自反性
             }
         }else {
             booleans[3] = false;
         }

        //判断对称性
        boolean res = false;
        for (Tuple tuple1 : set){
            res = false;
            for (Tuple tuple2 : set){
                if (tuple1.getValue().equals(tuple2.getKey()) && tuple1.getKey().equals(tuple2.getValue())) {
                    if (booleans[4] && !tuple1.getKey().equals(tuple1.getValue())){//如果数组为真同时两值不相等
                        booleans[4] = false;
                    }
                    res  = true;
                    break;
                }
            }
            if (!res)  break; //如果res不为true直接退出循环返回
        }
        booleans[2] = res; //添加对称结果
        return booleans;
    }
    /*打印结果*/
    public void printReasult(Boolean[] booleans){
//        System.out.println("请输入数组元素的大小");
//        Scanner scanner = new Scanner(System.in);
//        size = scanner.nextInt();
        System.out.println("传递性\t自反性\t对称性\t反自反性\t反对称性");
        for (Boolean temp : booleans){
            System.out.print(temp+"\t");
        }

    }
}

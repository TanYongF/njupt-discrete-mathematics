import java.lang.*;
import java.util.*;
import java.util.regex.Pattern;
/*
 * @autor:Tanyongfeng
 * @time:2020.09.14
 * @title:求命题的析取范式和主合取范式
 *  **方法步骤**：
 * 1.首先输入一个合式   确定栈外优先级和栈内优先级  并且由中缀表达式转换成后缀表达式
 * 2.计算后缀表达式的值 求取合式命题的真假
 * 3.打印真值表
 * 4.打印最终结果
 * 备注：析取：“|”  合取：“&”  非："！"  单条件运算符：“->” 双条件运算：“<=>”
 */

public class Truetable {
    private static HashMap<Character, Boolean> map = new HashMap<>();//命题真假键值对
    private static List<Character> ALPHA = new ArrayList<>(); //命题变量字符
    private static int size = 0;//命题的个数
    private static char[] suffixSentence = new char[100];//后缀表达式
    private static String infixSentence; //中缀表达式
    private static Boolean res; //真值表是否
    private static List<Boolean[]> trueList = new ArrayList<>(); //真值序列
    private static List<Boolean[]> falseList = new ArrayList<>();//假值序列

    /*主函数*/
    public static void main(String[] args) {
        Scanner scanner;
        String statementReconversion;
        init();//初始化函数
        System.out.println("请输入命题个数");
        scanner = new Scanner(System.in);
        size = scanner.nextInt();
        System.out.println("请输入合式公式：其中析取：“|”  合取：“&”  非：\"！\"  单条件运算符：“->” 双条件运算：“<=>”");
        do {
            scanner.nextLine();
            statementReconversion = scanner.nextLine();
            scanner.close();
        } while (!Pattern.matches("^[A-Za-z-><!|&]+$", statementReconversion));//判断是是否
        StringBuilder stringBuilder = new StringBuilder(statementReconversion);
        infixSentence = optimizationExpression(statementReconversion);
        infixToSuffix(infixSentence);//调用中缀转换为后缀函数
        for (int i = 0; i < ALPHA.size(); i++) {
            System.out.print(ALPHA.get(i) + "\t");
        }
        System.out.println(statementReconversion);
        printTrueTable(0);//调用打印真值表函数
        System.out.println(Arrays.toString(trueList.get(1)));
        printAns();//调用打印结果函数
    }

    /*初始化操作*/
    public static void init() {
        map.clear();
        ALPHA.clear();
    }

    /*栈内运算优先级函数 */
    public static int isp(char a) {
        if (a == '#') return 0;
        if (a == '(') return 1;
        if (a == '!') return 11;
        if (a == '&') return 9;
        if (a == '|') return 7;
        if (a == '>') return 5;
        if (a == '<') return 3;
        if (a == ')') return 12;
        return 0;
    }

    /*栈外运算优先级函数*/
    public static int icp(char a) {
        if (a == '#') return 0;
        if (a == '(') return 12;
        if (a == '!') return 10;
        if (a == '&') return 8;
        if (a == '|') return 6;
        if (a == '>') return 4;
        if (a == '<') return 2;
        if (a == ')') return 1;
        return 0;
    }

    /*优化表达式函数*/
    public static String optimizationExpression(String Sentence) {
        Sentence = Sentence.replaceAll("->", ">")
                .replaceAll("<=>", "<").replaceAll(" ", "") + "#";//在其末尾追加一个#
        return Sentence;
    }

    /*中缀表达式转换为后缀表达式函数*/
    public static void infixToSuffix(String infixSentence) {
        int i = 0, j = 0;
        Stack<Character> s = new Stack<>();
        s.push('#');
        char ch, operator; //定义 和 操作符号
        char[] alphas = infixSentence.toCharArray();
        ch = alphas[j];
        while (ch != '#') {
            if (Character.isAlphabetic(ch)) {
                suffixSentence[i++] = ch;
                if (!ALPHA.contains(ch)) { // 如果set中不存在此变量的话 就吧这个变量放在set中
                    ALPHA.add(ch);
                }
            } else if (ch == ')') {
                for (operator = s.lastElement(), s.pop(); operator != '('; operator = s.lastElement(), s.pop()) {
                    suffixSentence[i++] = operator;
                }
            } else {
                for (operator = s.lastElement(), s.pop(); icp(ch) <= isp(operator); operator = s.lastElement(), s.pop()) {
                    suffixSentence[i++] = operator;
                }
                s.push(operator);
                s.push(ch);
            }
            ch = alphas[++j];

        }
        while (!s.isEmpty()) {
            operator = s.lastElement();
            s.pop();
            if (operator != '#') {
                suffixSentence[i++] = operator;
            }
        }
    }

    /* 利用堆栈来计算表达式的真假值函数 */
    public static void checkCal() {
        Stack<Boolean> s = new Stack<>();
        char ch;
        int j = 0;
        Boolean b1, b2;
        ch = suffixSentence[j];
        while (!(ch == '#') && (ch != 0)) {
            ch = suffixSentence[j++];
            if (Character.isAlphabetic(ch)) {
                s.push(map.get(ch));
            } else {
                if (ch == '!') { //取反运算
                    b1 = s.lastElement();
                    s.pop();
                    s.push(!b1);
                } else if (ch == '&') {  //合取运算
                    b1 = s.lastElement();
                    s.pop();
                    b2 = s.lastElement();
                    s.pop();
                    s.push(b1 && b2); //析取运算
                } else if (ch == '|') {
                    b1 = s.lastElement();
                    s.pop();
                    b2 = s.lastElement();
                    s.pop();
                    s.push(b1 || b2);
                } else if (ch == '>') {  //单条件运算
                    b2 = s.lastElement();
                    s.pop();
                    b1 = s.lastElement();
                    s.pop();
                    s.push(!b1 || b2);
                } else if (ch == '<') { //双条件运算
                    b1 = s.lastElement();
                    s.pop();
                    b2 = s.lastElement();
                    s.pop();
                    s.push((b1 && b2) || (!b1 && !b2));
                }
            }
        }
        res = s.lastElement();//取出栈顶的元素
    }

    /*打印真值表函数*/
    public static void printTrueTable(int cur) {
        Boolean[] bs = new Boolean[size];
        if (cur == ALPHA.size()) {
            checkCal(); //语句的真假值
            for (Character character : ALPHA) {
                if (map.get(character)) {
                    System.out.print("T" + "\t");
                } else {
                    System.out.print("F" + "\t");
                }
            }
            if (res) {//用来求主析取范式
                System.out.println("T");
                for (int i = 0; i < ALPHA.size(); i++) {
                    bs[i] = map.get(ALPHA.get(i));
                }
                trueList.add(bs);
            } else {//用来求主合取范式
                System.out.println("F");
                for (int i = 0; i < ALPHA.size(); i++) {
                    bs[i] = map.get(ALPHA.get(i));
                }
                falseList.add(bs);
            }
            return;
        }
        /* 使用递归思想来打印真值表*/
        map.put(ALPHA.get(cur), true);
        printTrueTable(cur + 1);
        map.put(ALPHA.get(cur), false);
        printTrueTable(cur + 1);
    }

    /*求主析取以及主合取范式*/
    public static void printAns() {
        int i = 0, j = 0;
        int k;
        String ans = "";

        for (Boolean[] truearray : trueList) {
            StringBuilder stringBuilder = new StringBuilder(ans);
            stringBuilder.append("(");
            for (i = 0; i < ALPHA.size(); i++) {
                if (i == ALPHA.size() - 1) {
                    if (truearray[i]) {
                        stringBuilder.append(ALPHA.get(i));
                    } else {
                        stringBuilder.append("¬").append(ALPHA.get(i));
                    }
                } else {
                    if (truearray[i]) {
                        stringBuilder.append(ALPHA.get(i)).append("∧");
                    } else {
                        stringBuilder.append("¬").append(ALPHA.get(i)).append("∧");
                    }
                }
            }
            stringBuilder.append(")").append("∨");
            ans = stringBuilder.toString();
        }
        ans = ans.substring(0, ans.length() - 1);
        System.out.println(ans);
    }
}
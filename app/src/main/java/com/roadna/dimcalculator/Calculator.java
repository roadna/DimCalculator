package com.roadna.dimcalculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Dim Gwolukze on 2016/8/17 0017.
 */

public class Calculator {

    private String expression;

    private Map<String, Integer> priority;//操作符优先级Map

    public Queue<String> suffixTemp;

    public Calculator(String expression) {
        this.expression = expression;
        initPriority();
    }

    public String getSuffix(){
        return String.valueOf(suffixTemp);
    }

    private void initPriority() {
        this.priority = new HashMap<>();
        this.priority.put("#", 0);//#代表操作符堆栈中的栈底元素
        this.priority.put("+", 1);
        this.priority.put("-", 1);
        this.priority.put("*", 2);
        this.priority.put("/", 2);
    }

    //得到操作符的优先级
    public int getPriority(String operator) {
        //这里括号进行特殊处理
        if (operator.matches("[()]")) {
            return -1;
        } else {
            return priority.get(operator);
        }
    }

    //判断优先级高低
    private boolean isPrior(String one, String another) {
        return getPriority(one) <= getPriority(another);
    }

    //得到栈顶元素
    private <T> T getTopEle(Stack<T> stack) {
        if (stack == null) {
            return null;
        } else {
            return stack.get(stack.size() - 1);
        }
    }

    //检查计算式是否有格式错误
    private boolean isSyntaxChecked(){
        int left_bracket = 0;
        int right_bracket = 0;
        String current = "";
        String current_left = "";
        String current_right = "";
        int length0 = expression.length();
        for (int i= 0; i<length0; i++) {
            current = String.valueOf(expression.charAt(i));
            if (i != 0) current_left = String.valueOf(expression.charAt(i-1));
            if (current.matches("[\\+\\-]") & (current_left.matches("\\(") | i == 0)) {
                if (i == 0) {
                    expression = "0" + expression;
                }
                else {
                    expression = expression.substring(0, i) + "0" + expression.substring(i);
                }
            }
        }
        int length1 = expression.length();
        for (int i = 0; i < length1; i++) {
            current = String.valueOf(expression.charAt(i));
            if (i != 0) current_left = String.valueOf(expression.charAt(i-1));
            if (i != length1-1) current_right = String.valueOf(expression.charAt(i+1));
            if (current.matches("[\\(]")) {
                if (current_left.matches("[\\d\\.\\)]") | current_right.matches("[\\+\\-\\*/]") | i == length1 - 1)
                    return false; //左括号“（”左边是否含有数字、小数点或右括号、右边是否含有操作符
                left_bracket++;
            } else if (current.matches("[\\)]")) {
                if (current_left.matches("[\\+\\-\\*/]") | current_right.matches("[\\d\\.\\(]") | i == 0)
                    return false; //右括号“）”左边是否含有操作数、右边是否含有数字、小数点或左括号
                right_bracket++;
            } else if (current.matches("[\\.]") & i == length1-1) { //末尾是否存在小数点
                return false;
            } else if (current.matches("[\\+\\-\\*/]")) {
                if (current_left.matches("[\\+\\-\\*/]") | current_right.matches("[\\+\\-\\*/]")
                        | i == length1 - 1 | length1 == 1)
                    return false; //是否连续使用操作符
            }
        }
        return left_bracket == right_bracket;
    }

    //将中缀表达式转换为后缀表达式
    public Queue<String> toSuffix() {
        Queue<String> operandQueue = new LinkedList<>();//操作数队列
        if (!isSyntaxChecked()) {
            operandQueue.add("#"); //表示格式错误。
            return operandQueue;
        }
        Stack<String> operatorStack = new Stack<>();//操作符堆栈
        operatorStack.push("#");

        String current = "";
        String operator = "";
        String number = "";
        int start = 0;
        int end = 0;
        for (int i = 0; i < expression.length(); i++) {
            current = String.valueOf(expression.charAt(i));
            // 如果是数字，末尾标记end++
            if (current.matches("[\\d\\.]")) {
                // 如果数字是最后一个字符，直接将其入队列
                if (i == expression.length() - 1) {
                    current = String.valueOf(expression.substring(start, end+1));
                    operandQueue.add("0" + current);//使例如“.2”默认表示“0.2”
                } else {
                    end++;
                }
            } else {
                // 如果是字符
                // 如果是左括号，将其入栈
                if (current.equals("(")) {
                    operatorStack.push(current);
                } else {
                    // 如果是右括号和其它运算符，先将前面的数字入队列
                    number = expression.substring(start, end);
                    if (!number.isEmpty()) {
                        operandQueue.add("0" + number);//使例如“.2”默认表示“0.2”
                    }
                    // 如果是右括号，执行出栈操作，并将出栈的元素入队列，直到弹出栈的是左括号，左括号直接出栈
                    if (current.equals(")")) {
                        while (!getTopEle(operatorStack).equals("(")) {
                            operandQueue.add(operatorStack.pop());
                        }
                        operatorStack.pop();
                    } else {
                        // 如果是其它运算符，弹出所有优先级大于或者等于该运算符的栈顶元素，然后将该运算符入栈
                        operator = current;
                        while (isPrior(operator, getTopEle(operatorStack))) {
                            operandQueue.add(operatorStack.pop());
                        }
                        operatorStack.push(operator);
                    }
                }
                // 将指向数字的首尾指针加1
                start = end = i + 1;
            }
        }

        for (int i = operatorStack.size() - 1; i > 0; i--) {
            operandQueue.add(operatorStack.pop());
        }

        return operandQueue;
    }



    //计算算术式的结果
    public String getResult() {
        Queue<String> suffixQueue = toSuffix();
        suffixTemp = new LinkedList<>(suffixQueue);
        if (suffixQueue.peek().equals("#")) return "格式错误";
        Stack<String> suffixStack = new Stack<String>();
        String current = "";
        BigDecimal frontOperand;
        BigDecimal backOperand;

        double value = 0;
        for (int i = suffixQueue.size(); i > 0; i--) {
            current = suffixQueue.poll();
            //如果是数字，入栈
            if (current.matches("^\\d+(\\.\\d+)*$")) {
                suffixStack.push(current);
            } else {
                backOperand = BigDecimal.valueOf(Double.valueOf(suffixStack.pop()));
                frontOperand = BigDecimal.valueOf(Double.valueOf(suffixStack.pop()));
                if (current.equals("+")) {
                    value = frontOperand.add(backOperand).doubleValue();
                } else if (current.equals("-")) {
                    value = frontOperand.subtract(backOperand).doubleValue();
                } else if (current.equals("*")) {
                    value = frontOperand.multiply(backOperand).doubleValue();
                } else if (current.equals("/")) {
                    if (backOperand.doubleValue() == 0) return "勿零除";
                    value = frontOperand.divide(backOperand,10, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                suffixStack.push(String.valueOf(value));
            }
        }
        return suffixStack.get(0);
    }

}
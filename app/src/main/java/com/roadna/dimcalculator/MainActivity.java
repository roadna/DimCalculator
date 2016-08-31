package com.roadna.dimcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button zero,one,two,three,four,five,six,seven,eight,nine;
    private Button delete,clear,divide,multiply,minus,plus,equal,dot;
    private String expression = "";
    private String result = "";
    private String suffix = "";
    private TextView expression_text;
    private TextView result_text;
    private TextView suffix_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expression_text = (TextView) findViewById(R.id.expression_text);
        result_text = (TextView) findViewById(R.id.result_text);
        suffix_text = (TextView) findViewById(R.id.suffix_text);
        zero = (Button) findViewById(R.id.zero);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        delete = (Button) findViewById(R.id.delete);
        clear = (Button) findViewById(R.id.clear);
        divide = (Button) findViewById(R.id.divide);
        multiply = (Button) findViewById(R.id.multiply);
        minus = (Button) findViewById(R.id.minus);
        plus = (Button) findViewById(R.id.plus);
        equal = (Button) findViewById(R.id.equal);
        dot = (Button) findViewById(R.id.dot);


    }

    public void add_left_bracket(View view){expression += "("; expression_text.setText(expression);}
    public void add_right_bracket(View view){expression += ")"; expression_text.setText(expression);}
    public void delete_string(View view){
        if (expression.length() != 0)
        expression = expression.substring(0,expression.length()-1);
        expression_text.setText(expression);
    }
    public void clear_string(View view){
        expression = "";
        expression_text.setText(expression);
        result = "";
        result_text.setText(result);
        suffix = "";
        suffix_text.setText(suffix);
    }
    public void add_seven(View view){expression += "7"; expression_text.setText(expression);}
    public void add_eight(View view){expression += "8"; expression_text.setText(expression);}
    public void add_nine(View view){expression += "9"; expression_text.setText(expression);}
    public void add_divide(View view){expression += "/"; expression_text.setText(expression);}
    public void add_four(View view){expression += "4"; expression_text.setText(expression);}
    public void add_five(View view){expression += "5"; expression_text.setText(expression);}
    public void add_six(View view){expression += "6"; expression_text.setText(expression);}
    public void add_multiply(View view){expression += "*"; expression_text.setText(expression);}
    public void add_one(View view){expression += "1"; expression_text.setText(expression);}
    public void add_two(View view){expression += "2"; expression_text.setText(expression);}
    public void add_three(View view){expression += "3"; expression_text.setText(expression);}
    public void add_minus(View view){expression += "-"; expression_text.setText(expression);}
    public void add_dot(View view){expression += "."; expression_text.setText(expression);}
    public void add_zero(View view){expression += "0"; expression_text.setText(expression);}
    public void cal_result(View view){
        if (expression.length() != 0) {
            Calculator cal = new Calculator(expression);
            result = cal.getResult();
            result_text.setText(result);
            suffix = cal.getSuffix();
            suffix_text.setText(suffix);
        }
    }
    public void add_plus(View view){expression += "+"; expression_text.setText(expression);}
}

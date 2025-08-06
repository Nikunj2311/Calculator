package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.utilities.SchemeExpressive

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var input=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        display=findViewById(R.id.display)

        val button=listOf(
            R.id.button0,R.id.button1,R.id.button2,R.id.button3,
            R.id.button4,R.id.button5,R.id.button6,R.id.button7,
            R.id.button8,R.id.button9,R.id.buttonsum,R.id.buttonmin,
            R.id.buttonmul,R.id.buttondivide,R.id.buttonclear,R.id.buttonequal)

        button.forEach {   // for each is used to access each button
            id->findViewById<Button>(id).setOnClickListener{handleInput((it as Button).text.toString())}
        }    // here it as Button takes the text of the button . the we converted into string
    }
    private fun handleInput(value:String){
        when(value){
            "C"-> {  //clear
                input = ""
                display.text = "0"
            }
            "="->{   // calculate
                calculcateResult()
            }
            else->{   //entering the value in display
                input+=value
                display.text=input
            }
        }
    }

    //calculating

    private fun calculcateResult(){
        try {       // try is used to avoid the chances of crashing
            val result=eval(input)
            display.text=result.toString()
            input=result.toString()
        }catch (e: Exception){
            display.text="Error"
            input=""
        }
    }
    private fun eval(expression: String): Double{
        val tokens=expression.split("(?<=[-=*/])|(?=[-=*/])".toRegex()).map{it.trim()}   //with this ?<=[-=*/]... we split the expression into different operators and operand
        var stack=mutableListOf<Double>()   // this is use to store the operator and operand sprately
        var currentOperator="+"  // this will hold operation to be proceed

        tokens.forEach{token->
            when{
                token.toDoubleOrNull() !=null->{  // this toDoubleOrNull help to identify it is int or not
                    val num=token.toDouble()
                    when(currentOperator){
                        "+"->stack.add(num)
                        "-"->stack.add(-num)
                        "*"->stack[stack.lastIndex]=stack.last()*num
                        "/"->stack[stack.lastIndex]=stack.last()/num
                    }
                }
                token in "-+*/"->currentOperator=token
            }
        }
        return stack.sum()      // finally it will sum the remaing values
    }
}
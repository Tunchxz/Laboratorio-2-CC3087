package uvg.edu.gt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Stack

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main) //Despues de esta llamada ya puedo usar los elementos

        // Variables generales
        val operacionString = StringBuilder("")
        val letterToNumberMap = mutableMapOf<Char, String>()
        var resultadoPressed = false

        // Variables de TextView
        val resultadoTextView = findViewById<TextView>(R.id.ResultadoTextView)
        val operacionTextView = findViewById<TextView>(R.id.OperacionTextView)

        // Botones de Operandos
        val num0Button = findViewById<Button>(R.id.Num0Button)
        val num1Button = findViewById<Button>(R.id.Num1Button)
        val num2Button = findViewById<Button>(R.id.Num2Button)
        val num3Button = findViewById<Button>(R.id.Num3Button)
        val num4Button = findViewById<Button>(R.id.Num4Button)
        val num5Button = findViewById<Button>(R.id.Num5Button)
        val num6Button = findViewById<Button>(R.id.Num6Button)
        val num7Button = findViewById<Button>(R.id.Num7Button)
        val num8Button = findViewById<Button>(R.id.Num8Button)
        val num9Button = findViewById<Button>(R.id.Num9Button)
        val dotButton = findViewById<Button>(R.id.DotButton)

        // Botones de Operadores
        val additionButton = findViewById<Button>(R.id.AdditionButton)
        val substractionButton = findViewById<Button>(R.id.SubstractionButton)
        val multiplicationButton = findViewById<Button>(R.id.MultiplicationButton)
        val divisionButton = findViewById<Button>(R.id.DivisionButton)
        val powerButton = findViewById<Button>(R.id.PowerButton)
        val squareRootButton = findViewById<Button>(R.id.SquareRootButton)
        val percentButton = findViewById<Button>(R.id.PercentButton)
        val openParenthesisButton = findViewById<Button>(R.id.OpenParenthesisButton)
        val closeParenthesisButton = findViewById<Button>(R.id.CloseParenthesisButton)

        // Botones de funcionalidad
        val cancelButton = findViewById<Button>(R.id.CancelButton)
        val cancelAllButton = findViewById<Button>(R.id.CancelAllButton)
        val equalsButton = findViewById<Button>(R.id.EqualsButton)

        fun actualizarString(palabra: String) {
            if (resultadoPressed){
                resultadoPressed = false
                operacionString.clear()
            }
            operacionString.append(palabra)
            operacionTextView.setText(operacionString)
        }

        num0Button.setOnClickListener() {
            actualizarString("0")
        }
        num1Button.setOnClickListener() {
            actualizarString("1")
        }
        num2Button.setOnClickListener() {
            actualizarString("2")
        }
        num3Button.setOnClickListener() {
            actualizarString("3")
        }
        num4Button.setOnClickListener() {
            actualizarString("4")
        }
        num5Button.setOnClickListener() {
            actualizarString("5")
        }
        num6Button.setOnClickListener() {
            actualizarString("6")
        }
        num7Button.setOnClickListener() {
            actualizarString("7")
        }
        num8Button.setOnClickListener() {
            actualizarString("8")
        }
        num9Button.setOnClickListener() {
            actualizarString("9")
        }
        dotButton.setOnClickListener() {
            actualizarString(".")
        }

        additionButton.setOnClickListener() {
            actualizarString("+")
        }

        substractionButton.setOnClickListener() {
            actualizarString("-")
        }

        multiplicationButton.setOnClickListener() {
            actualizarString("*")
        }

        divisionButton.setOnClickListener() {
            actualizarString("/")
        }

        powerButton.setOnClickListener() {
            actualizarString("^")
        }

        squareRootButton.setOnClickListener() {
            actualizarString("√")
        }

        openParenthesisButton.setOnClickListener() {
            actualizarString("(")
        }

        closeParenthesisButton.setOnClickListener() {
            actualizarString(")")
        }

        cancelButton.setOnClickListener() {
            if (operacionString.isNotEmpty()) {
                operacionString.deleteCharAt(operacionString.length - 1);
                operacionTextView.setText(operacionString)
            }
        }

        cancelAllButton.setOnClickListener() {
            operacionString.clear()
            operacionTextView.setText("0")
            resultadoTextView.setText("0")
        }

        equalsButton.setOnClickListener() {
            fun convertExpression(expression: String): String {
                val convertedExpression = StringBuilder()
                var currentLetter = 'a'

                // Separar la expresión por operadores y paréntesis
                val parts = expression.split("(?<=[-+*/^$()])|(?=[-+*/^$()])".toRegex())

                for (part in parts) {
                    if (part.matches("\\d+(\\.\\d+)?".toRegex())) { // Si la parte es un número (entero o decimal)
                        // Verificar si el número ya tiene una letra asignada
                        if (!letterToNumberMap.containsValue(part)) {
                            letterToNumberMap[currentLetter] = part
                            currentLetter++
                        }
                        // Agregar la letra correspondiente al resultado
                        for (key in letterToNumberMap.keys) {
                            if (letterToNumberMap[key] == part) {
                                convertedExpression.append(key)
                                break
                            }
                        }
                    } else {
                        // Si no es un número, agregar el operador tal como está
                        convertedExpression.append(part)
                    }
                }

                return convertedExpression.toString()
            }

            // Función para retornar la precedencia de los operadores
            fun prec(c: Char): Int {
                return when (c) {
                    '$' -> 4 // Raíz cuadrada
                    '^' -> 3 // Exponenciación
                    '/', '*' -> 2 // Multiplicación y División
                    '+', '-' -> 1 // Suma y Resta
                    else -> -1
                }
            }

            // Función para retornar la asociatividad de los operadores
            fun associativity(c: Char): Char {
                return if (c == '^') 'R' else 'L' // Right-associative para ^, Left-associative para los demás
            }

            // Función principal para convertir expresión infija a postfija
            fun infixToPostfix(s: String): String {
                val result = StringBuilder()
                val stack = Stack<Char>()

                for (i in s.indices) {
                    val c = s[i]

                    // Si el carácter escaneado es un operando, agregarlo a la cadena de resultado.
                    if (c.isLetterOrDigit()) {
                        result.append(c)
                    }
                    // Si el carácter escaneado es un ‘(‘, empújalo a la pila.
                    else if (c == '(') {
                        stack.push(c)
                    }
                    // Si el carácter escaneado es un ‘)’, sacar de la pila hasta encontrar ‘(‘.
                    else if (c == ')') {
                        while (!stack.isEmpty() && stack.peek() != '(') {
                            result.append(stack.pop())
                        }
                        stack.pop() // Sacar '('
                    }
                    // Si se escanea un operador
                    else if (c == '$' || c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
                        while (!stack.isEmpty() && prec(c) <= prec(stack.peek()) && associativity(c) == 'L') {
                            result.append(stack.pop())
                        }
                        stack.push(c)
                    }
                }

                // Sacar todos los elementos restantes de la pila
                while (!stack.isEmpty()) {
                    result.append(stack.pop())
                }

                return result.toString()
            }

            // Método para evaluar el valor de una expresión postfija
            fun evaluatePostfix(exp: String): Double? {
                val stack = Stack<Double>()

                // Escanear todos los caracteres uno por uno
                for (i in exp.indices) {
                    val c = exp[i]

                    // Si el carácter escaneado es un operando, empújalo a la pila.
                    if (c.isLetterOrDigit()) {
                        stack.push(letterToNumberMap[c]!!.toDouble())
                    } else {
                        if (c == '$') {
                            // Si es el operador raíz cuadrada, saca un elemento y aplica la raíz cuadrada
                            val val1 = stack.pop()
                            stack.push(Math.sqrt(val1))
                        } else {
                            // Para otros operadores
                            val val1 = stack.pop()
                            val val2 = stack.pop()

                            when (c) {
                                '+' -> stack.push(val2 + val1)
                                '-' -> stack.push(val2 - val1)
                                '/' -> if (val1 != 0.0) stack.push(val2 / val1) else return null
                                '*' -> stack.push(val2 * val1)
                                '^' -> stack.push(Math.pow(val2, val1))
                            }
                        }
                    }
                }
                return stack.pop()
            }

            fun expresionValida(expresion: String): Boolean {
                var parentesis = 0
                var operandos = 0
                var operadoresBinarios = 0
                var operadoresUnarios = 0
                var ultimoFueOperador = true // Para verificar si dos operadores están en fila

                for (c in expresion.toCharArray()) {
                    when (c) {
                        '(' -> parentesis++
                        ')' -> {
                            parentesis--
                            if (parentesis < 0) return false // Verificar paréntesis de cierre inválido
                        }
                        '+', '-', '*', '/', '^' -> {
                            if (ultimoFueOperador) return false // Dos operadores seguidos
                            operadoresBinarios++
                            ultimoFueOperador = true
                        }
                        '$' -> {
                            if (!ultimoFueOperador) return false // El operador unario debe seguir a otro operador o estar al inicio
                            operadoresUnarios++
                            ultimoFueOperador = true
                        }
                        else -> if (c.isDigit() || c.isLetter()) {
                            operandos++
                            ultimoFueOperador = false
                        } else {
                            return false // Caracter inválido
                        }
                    }
                }

                // Verificar si la estructura es válida, los paréntesis están balanceados y la relación operadores-operandos es correcta
                val estructuraValida = parentesis == 0 && !ultimoFueOperador

                // Cuando hay operadores unarios, asegurar una relación 1:1 entre operadores totales y operandos
                return if (operadoresUnarios > 0) {
                    estructuraValida && (operadoresBinarios + operadoresUnarios == operandos)
                } else {
                    // De lo contrario, verificar la relación n:n+1 para operadores binarios y operandos
                    estructuraValida && (operadoresBinarios == operandos - 1)
                }
            }

            val operacion =  convertExpression(operacionString.toString().replace('√', '$'))
            if (expresionValida(operacion)) {
                val resultado = evaluatePostfix(infixToPostfix(operacion))
                if (resultado == null){
                    resultadoTextView.setText("Division por 0")
                } else {
                    resultadoTextView.setText("$resultado")
                }
            } else {
                resultadoTextView.setText("Error de Sintaxis")
            }
            resultadoPressed = true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
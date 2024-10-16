package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView res;
    private StringBuilder currentInput = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = findViewById(R.id.res);

        Button addition = findViewById(R.id.addition);
        Button soustraction = findViewById(R.id.soustraction);
        Button multiplication = findViewById(R.id.multiplication);
        Button division = findViewById(R.id.division);
        Button modulo = findViewById(R.id.modulo);

        Button zero = findViewById(R.id.zero);
        Button one = findViewById(R.id.one);
        Button two = findViewById(R.id.two);
        Button three = findViewById(R.id.three);
        Button four = findViewById(R.id.four);
        Button five = findViewById(R.id.cinq);
        Button six = findViewById(R.id.six);
        Button sept = findViewById(R.id.sept);
        Button huit = findViewById(R.id.huit);
        Button nine = findViewById(R.id.nine);

        Button signe = findViewById(R.id.signe);
        Button virgule = findViewById(R.id.virgule);
        Button effacerTout = findViewById(R.id.clear);
        Button effacer = findViewById(R.id.effacer);
        Button calcul = findViewById(R.id.egale);

        setOnClickListenerForNumbers(zero, "0");
        setOnClickListenerForNumbers(one, "1");
        setOnClickListenerForNumbers(two, "2");
        setOnClickListenerForNumbers(three, "3");
        setOnClickListenerForNumbers(four, "4");
        setOnClickListenerForNumbers(five, "5");
        setOnClickListenerForNumbers(six, "6");
        setOnClickListenerForNumbers(sept, "7");
        setOnClickListenerForNumbers(huit, "8");
        setOnClickListenerForNumbers(nine, "9");

        setOnClickListenerForOperator(addition, "+");
        setOnClickListenerForOperator(soustraction, "-");
        setOnClickListenerForOperator(multiplication, "x");
        setOnClickListenerForOperator(division, "/");
        setOnClickListenerForOperator(modulo, "%");

        signe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input = currentInput.toString().trim();
                String[] tokens = input.split(" ");

                if (tokens.length == 0) {
                    currentInput.append("-");
                }
                else if (tokens.length > 0) {
                    String lastToken = tokens[tokens.length - 1];

                    if (lastToken.startsWith("-")) {
                        currentInput.setLength(currentInput.length() - lastToken.length());
                        currentInput.append(lastToken.substring(1));
                    }
                    else if (lastToken.matches("\\d+(\\.\\d+)?")) {
                        currentInput.setLength(currentInput.length() - lastToken.length());
                        currentInput.append("-").append(lastToken);
                    }
                }

                updateDisplay();
            }
        });

        virgule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = currentInput.toString().trim();
                String[] tokens = input.split(" ");

                if (tokens.length == 0 || tokens.length == 1) {
                    if (!input.contains(".")) {
                        currentInput.append(".");
                        updateDisplay();
                    }
                } else {
                    String lastOperand = tokens[tokens.length - 1];
                    if (!lastOperand.contains(".")) {
                        currentInput.append(".");
                        updateDisplay();
                    }
                }
            }
        });

        effacerTout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentInput.setLength(0);
                updateDisplay();
            }
        });

        effacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentInput.length() > 0) {
                    currentInput.deleteCharAt(currentInput.length() - 1);
                    updateDisplay();
                }
            }
        });

        calcul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double result = calculer(currentInput.toString());
                    if (result != null) {
                        res.setText(result.toString());
                        currentInput.setLength(0);
                        currentInput.append(result);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOnClickListenerForNumbers(Button button, String value) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInput.append(value);
                updateDisplay();
            }
        });
    }

    private void setOnClickListenerForOperator(Button button, String operator) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentInput.length() > 0 && !isLastCharOperator() && !currentInput.toString().contains(" + ") && !currentInput.toString().contains(" - ")&& !currentInput.toString().contains(" x ")&& !currentInput.toString().contains(" / ") && !currentInput.toString().contains(" % ")) {
                    currentInput.append(" ").append(operator).append(" ");
                    updateDisplay();
                }
                if (currentInput.toString().contains(" + ") || currentInput.toString().contains(" - ")||currentInput.toString().contains(" x ") ||currentInput.toString().contains(" / ") || currentInput.toString().contains(" % ")){
                    try {
                        Double result = calculer(currentInput.toString());
                        if (result != null) {
                            res.setText(result.toString());
                            currentInput.setLength(0);
                            currentInput.append(result);
                            currentInput.append(" ").append(operator).append(" ");
                            updateDisplay();
                        }
                    } catch (Exception e) {
                        /**/
                    }
                }
            }
        });
    }

    private void updateDisplay() {
        res.setText(currentInput.toString());
    }

    private boolean isLastCharOperator() {
        char lastChar = currentInput.charAt(currentInput.length() - 1);
        return lastChar == '+' || lastChar == '-' || lastChar == 'x' || lastChar == '/' || lastChar == '%';
    }

    private Double calculer(String input) throws Exception {
        String[] tokens = input.trim().split(" ");

        if (tokens.length < 3) {
            throw new Exception("Erreur de format");
        }

        Double num1 = Double.parseDouble(tokens[0]);
        Double num2 = Double.parseDouble(tokens[2]);
        String operator = tokens[1];
        Double result;

        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "x":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    throw new Exception("Erreur: division par zéro!");
                }
                result = num1 / num2;
                break;
            case "%":
                if (num2 == 0) {
                    throw new Exception("Erreur: division par zéro!");
                }
                result = num1 % num2;
                break;
            default:
                throw new Exception("Opérateur non valide");
        }
        return result;
    }
}

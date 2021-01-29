package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private Map<String, Integer> variables;
    private Map<String, BigInteger> bigNumbers;
    private BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
    private int total = 0;

    public Main() {
        variables = new HashMap<>();
        bigNumbers = new HashMap<>();
    }

    public static void main(String[] args) {
        Main calculator = new Main();
        calculator.UI();

    }

    public void UI() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            if(input.isEmpty()) {
                continue;
            }
            if(input.matches("\\/.*")) {
                if(input.equals("/exit") ) {
                    System.out.println("Bye!");
                    break;
                } else if(input.equals("/help")) {
                    System.out.println("The program calculates the equation");
                    continue;
                }
                else {
                    System.out.println("Unknown command");
                    continue;
                }
            }
            Pattern pattern = Pattern.compile("\\s*");
            Matcher matcher = pattern.matcher(input);
            String input2 = matcher.replaceAll("");
            if(checkInput(input2)) {
                String[] parts = input2.split("(?<=[-+*/=])|(?=[-+*/=])");
                if(input2.contains("=")) {
                    String[] parts2 = input2.split("=");
                    addVariables(parts2);
                } else if(input2.matches("^((\\(*(\\d+|[a-zA-Z]+)\\)*)[-+/*]+)+((\\(*(\\d+|[a-zA-Z]+)\\)*)+)$")) {
                    ArrayList<String> p = makePostfix(input2);
                    for(int i = 0; i < p.size(); i++) {
                        if(p.get(i).matches("\\d+|[a-zA-Z]+")) {
                            BigInteger k;
                            if(p.get(i).matches("\\d+")) {
                                k = new BigInteger(p.get(i));
                                if(k.compareTo(maxInt) > 0 || bigNumbers.get(p.get(i)) != null) {
                                    System.out.println(solveBigPostfix(p));
                                    break;
                                }
                            } else if(p.get(i).matches("[a-zA-Z]+")) {
                                if(bigNumbers.get(p.get(i)) != null) {
                                    System.out.println(solveBigPostfix(p));
                                    break;
                                }
                            }
                        }
                        if(i == p.size() - 1) {
                            System.out.println(solvePostfix(p));
                            break;
                        }

                    }
                }
            }
        }
    }

    public boolean checkInput(String input) {
        if(input.matches("^[a-zA-Z]+")) {
            if(variables.get(input) == null && bigNumbers.get(input) == null) {
                System.out.println("Unknown variable");
                return false;
            } else if(!(variables.get(input) == null)) {
                System.out.println(variables.get(input));
            } else if(!(bigNumbers.get(input) == null)) {
                System.out.println(bigNumbers.get(input));
            }
            return true;
        }
        if(input.matches("^((\\(*(\\d+|[a-zA-Z]+)\\)*)[-+/*]+)+((\\(*(\\d+|[a-zA-Z]+)\\)*)+)$")) {
            int count1 = 0;
            int count2 =0;
            for(int i = 0; i < input.length(); i++) {
                if(input.charAt(i) == ')') {
                    count1++;
                }
                if(input.charAt(i) == '(') {
                    count2++;
                }
            }
            if(!(count1 == count2)) {
                System.out.println("Invalid expression");
                System.out.println(count1 + count2);
                return false;
            } else if(input.matches(".*(\\*{2,}|/{2,}|(/\\*)+|(\\*/)+).*")) {
                System.out.println("Invalid expression");
                return false;
            }
            return true;
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z]+\\s*[=]([-+]*)");
        Matcher matcher = pattern.matcher(input);
        if(!matcher.find()) {
            System.out.println("Invalid identifier");
            return false;
        } else if(!input.matches("^[a-zA-Z]+\\s*[=]\\s*([-+]*)\\s*(([a-zA-Z]+|\\d+)\\s*)$")) {
            System.out.println("Invalid assignment");
            return false;
        } else if(input.matches("^[a-zA-Z]+\\s*[=]\\s*[-+]*([a-zA-Z]+|\\d+)$")) {
            return true;
        }
        return false;
    }

    public void solve(String[] parts) {
        String[] parts2 = parts;
        int negCounter = 1;
        if(parts2[0].matches("\\d+")) {
            total += Integer.valueOf(parts2[0]);
        } else {
            total += variables.get(parts[0]);
        }
        for(int i = 1; i < parts2.length; i++) {
            if(parts2[i].equals("+")) {
                if(parts2[i+1].matches("[a-zA-Z]+")) {
                    total += variables.get(parts2[i+1]);
                } else if (parts2[i+1].equals("+")) {
                    continue;
                } else {
                    total += Integer.valueOf(parts2[i+1]);
                }
            }
            if(parts2[i].equals("-")) {
                if(parts2[i+1].matches("[a-zA-Z]+")) {
                    if(negCounter % 2 == 0) {
                        total += variables.get(parts2[i+1]);
                    } else {
                        total -= variables.get(parts2[i+1]);
                    }
                } else if(parts2[i+1].equals("-")) {
                    negCounter++;
                    continue;
                } else {
                    if(negCounter % 2 == 0) {
                        total += Integer.valueOf(parts2[i+1]);
                    } else {
                        total -= Integer.valueOf(parts2[i+1]);
                    }
                }
            }
            if(parts2[i].matches("\\d+|[a-zA-Z]+")) {
                negCounter = 1;
                continue;
            }
        }
    }

    public void addBigVariables(ArrayList<BigInteger> bigParts) {

    }

    public void addVariables(String[] parts) {
        if((variables.get(parts[1]) == null && bigNumbers.get(parts[1]) == null)&& !parts[1].matches("[-+]*\\d+")) {
            System.out.println("Invalid assignment");
            return;
        }
        if(parts[0].matches("[a-zA-Z]+") && parts[1].matches("[-+]*[a-zA-Z]+") && (variables.get(parts[1]) != null) || bigNumbers.get(parts[1]) != null) {
            if(bigNumbers.get(parts[1]) != null) {
                bigNumbers.put(parts[0], bigNumbers.get(parts[1]));
            } else {
                variables.put(parts[0], variables.get(parts[1]));
            }
        } else {
            BigInteger value = new BigInteger(parts[1]);
            if(value.compareTo(maxInt) > 0) {
                bigNumbers.put(parts[0], value);
            } else {
                variables.put(parts[0], Integer.valueOf(parts[1]));
            }
        }
    }

    public int precedence(String c) {
        switch (c) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
        }
        return -1;
    }

    public ArrayList<String> makePostfix(String input2) {
        Deque<String> Infix = new ArrayDeque<>();
        ArrayList<String> Postfix = new ArrayList<>();
        String[] parts = input2.split("(?<=[-+*/=()^])|(?=[-+*/=()^])");
        int counter = 0;
        for(int i = 0; i < parts.length; i++) {
            String c = parts[i];
            if(c.equals("-") && parts[i + 1].equals("-")) {
                counter++;
                continue;
            }
            if(c.equals("-") && !parts[i + 1].equals("-")) {
                if(counter % 2 == 0 && counter > 1) {
                    c = "+";
                } else {
                    c = "-";
                }
            }
            if(c.equals("+") && parts[i + 1].equals("+")) {
                continue;
            }
            if(precedence(c) > 0) {
                while(!Infix.isEmpty() && precedence(Infix.peekLast()) >= precedence(c)) {
                    Postfix.add(Infix.pollLast());
                }
                Infix.addLast(c);
            } else if(c.equals(")")) {
                String x = Infix.pollLast();
                while(!x.equals("(")) {
                    Postfix.add(x);
                    x = Infix.pollLast();
                }
            } else if(c.equals("(")) {
                Infix.addLast(c);
            } else {
                Postfix.add(c);
            }
        }
        for(int i = 0; i <= Infix.size(); i++) {
            Postfix.add(Infix.pollLast());
        }
        return Postfix;
    }

    public BigInteger solveBigPostfix(ArrayList<String> expression) {
        Deque<String> solve = new ArrayDeque<>();
        int counter =0;
        for(int p = 0; p < expression.size(); p++) {
            String i = expression.get(p);
            if(expression.get(p).matches("[a-zA-Z]+")) {
                i = String.valueOf(bigNumbers.get(expression.get(p)));
            }
            if(precedence(i) < 0) {
                solve.addLast(i);
            } if(precedence(i) > 0) {
                BigInteger j = new BigInteger(solve.pollLast());
                BigInteger k = new BigInteger(solve.pollLast());
                if(i.equals("*")) {
                    solve.addLast(String.valueOf(k.multiply(j)));
                } else if(i.equals("/")) {
                    solve.addLast(String.valueOf(k.divide(j)));
                } else if(i.equals("-")) {
                    solve.addLast(String.valueOf(k.subtract(j)));
                } else if(i.equals("+")) {
                    solve.addLast(String.valueOf(k.add(j)));
                } else if(i.equals("^")) {
                    BigInteger o = k;
                    for(BigInteger s = new BigInteger("1"); s.compareTo(j) < 0; s = s.add(BigInteger.ONE)) {
                        o = o.multiply(k);
                    }
                    solve.addLast(String.valueOf(o));
                }
            }
        }
        BigInteger a = new BigInteger(solve.peekLast());
        return a;
    }

    public int solvePostfix(ArrayList<String> expression) {
        Deque<String> solve = new ArrayDeque<>();
        int counter =0;
        for(int p = 0; p < expression.size(); p++) {
            String i = expression.get(p);
            if(expression.get(p).matches("[a-zA-Z]+")) {
                i = String.valueOf(variables.get(expression.get(p)));
            }
            if(precedence(i) < 0) {
                solve.addLast(i);
            } if(precedence(i) > 0) {
                int j = Integer.valueOf(solve.pollLast());
                int k = Integer.valueOf(solve.pollLast());
                if(i.equals("*")) {
                    solve.addLast(String.valueOf(k * j));
                } else if(i.equals("/")) {
                    solve.addLast(String.valueOf(k / j));
                } else if(i.equals("-")) {
                    solve.addLast(String.valueOf(k - j));
                } else if(i.equals("+")) {
                    solve.addLast(String.valueOf(k + j));
                } else if(i.equals("^")) {
                    int o = k;
                    for(int s = 1; s < j; s++) {
                        o = o * k;
                    }
                    solve.addLast(String.valueOf(o));
                }
            }
        }
        return Integer.valueOf(solve.peekLast());
    }
}

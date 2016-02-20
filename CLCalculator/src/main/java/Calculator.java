
import ca.clcalculator.exception.CLException;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Viswanathan Manickam
 */
public class Calculator {

    private static final Logger logger = Logger.getLogger(Calculator.class);

    private Map<String, Integer> commands = null;

    public Calculator() {
        commands = new HashMap<>();
        commands.put("add", 2);
        commands.put("sub", 2);
        commands.put("mult", 2);
        commands.put("div", 2);
        commands.put("let", 3);
    }

    private Boolean commandValid(String command) {
        if (command == null) {
            return Boolean.FALSE;
        }

//        command = command.substring(0, command.indexOf("("));
        if (commands.keySet().contains(command)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean checkParantheses(String command) {

        return Boolean.TRUE;
    }

    private String getOperation(String command) throws CLException {
        if (command == null) {
            throw new CLException(CLException.COMMAND_ERROR);
        }

        command =  StringUtils.substringBefore(command, "(");
        if (commandValid(command)) {
            return command;
        } else {
            throw new CLException(CLException.COMMAND_ERROR);
        }
    }

    private String[] getParameters(String command) throws CLException {
        String[] result = null;
        if (command == null) {
            throw new CLException(CLException.COMMAND_ERROR);
        }

        String params = command.substring(command.indexOf("(") + 1, command.indexOf(")"));
        if (params.contains(",")) {
            result = params.split(",");
        } else {
            throw new CLException(CLException.COMMAND_ERROR);
        }
        return result;
    }

    private String[] getOperands(String input, Integer operandCount) {
        String[] result = null;
        int count = StringUtils.countMatches(input, ",");
        if (count > operandCount - 1) {
            int brackets = 0;
            int foundMiddle = -1;
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '(') {
                    brackets++;
                } else if (input.charAt(i) == ')') {
                    brackets--;
                } else if (input.charAt(i) == ',' && brackets == 0) {
                    foundMiddle = i;
                    break;
                }
            }
            //System.out.println(foundMiddle);
            result = new String[2];
            result[0] = input.substring(0, foundMiddle);
            result[1] = input.substring(foundMiddle + 1, input.length());
            System.out.println(result[0]);
            System.out.println(result[1]);
        } else {
            result = input.split(",");
            /* result = new String[2];
            result[0] = input.split(",")[0];
            result[1] = input.split(",")[1];*/
        }
        return result;
    }
    
    

    private Integer evaluate(String input) throws CLException {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering evaluate method for input :" + input);
        }
        String operation = getOperation(input);
        if (logger.isInfoEnabled()) {
            logger.info("Found operation:" + operation);
        }
        String[] operands = null;
        Integer[] operandValue = new Integer[2];
        operands = getOperands(StringUtils.substring(StringUtils.substringAfter(input, "("), 0, StringUtils.substringAfter(input, "(").length() - 1), commands.get(operation));
        if (operands != null) {
            for (int i = 0; i < operands.length; i++) {
                if (isFunction(operands[i])) {
                    operandValue[i] = evaluate(operands[i]);
                } else {
                    operandValue[i] = Integer.parseInt(operands[i]);
                }
            }
        }
        switch (operation) {
            case "add":
                return operandValue[0] + operandValue[1];
            case "sub":
                return operandValue[0] - operandValue[1];
            case "mult":
                return operandValue[0] * operandValue[1];
            case "div":
                try{
                    return operandValue[0] / operandValue[1];
                }catch(ArithmeticException e){
                    throw new CLException(CLException.DIVIDE_ZERO_ERROR);
                }
                        
                        
                        
        }
        return 0;
    }

    private static Boolean isFunction(String operand) {
        if (operand.contains("(")) {
            return true;
        }
        return false;
    }

    public Integer process(String command) throws CLException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started processing command");
        }
        if (checkParantheses(command)) {
            return evaluate(command);
        } else {
            throw new CLException(CLException.PARANTHESIS_ERROR);
        }
    }
}

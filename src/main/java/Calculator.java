
import ca.clcalculator.exception.CLException;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

    private Map<String, Integer> variable = null;

    public Calculator() {
        commands = new HashMap<>();
        commands.put("add", 2);
        commands.put("sub", 2);
        commands.put("mult", 2);
        commands.put("div", 2);
        commands.put("let", 3);
    }

    private Boolean commandValid(String command) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering command valid check method");
        }
        if (command == null) {
            return Boolean.FALSE;
        }
        if (commands.keySet().contains(command)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean checkParantheses(String command) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering paranthesis check method");
        }

        int count = 0;
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == '(') {
                count++;
            } else if (command.charAt(i) == ')') {
                count--;
            }
            if (count < 0) {
                return Boolean.FALSE;
            }
        }
        if (count == 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private String getOperation(String command) throws CLException {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering find operation method");
        }
        if (command == null) {
            throw new CLException(CLException.COMMAND_ERROR);
        }
        if (logger.isInfoEnabled()) {
            logger.info(command);
        }
        command = StringUtils.substringBefore(command, "(");
        if (commandValid(command)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exiting paranthesis check method");
            }
            return command;
        } else {
            throw new CLException(CLException.COMMAND_ERROR);
        }
    }

    private String[] getOperands(String input, Integer operandCount) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering finding operands method");
        }
        if (input == null || (input != null && input.equals(""))) {
            return null;
        }
        String[] result = null;
        if (logger.isInfoEnabled()) {
            logger.info("Trying to find operands from the input" + input);
        }

        int count = StringUtils.countMatches(input, ",");
        if (count > operandCount - 1) {
            result = new String[operandCount];
            int brackets = 0;
            int index = 0;
            int foundMiddle = -1;
            int prevIndex = -1;
            while (index <= operandCount - 1) {
                for (int i = foundMiddle + 1; i < input.length(); i++) {
                    if (input.charAt(i) == '(') {
                        brackets++;
                    } else if (input.charAt(i) == ')') {
                        brackets--;
                    } else if (input.charAt(i) == ',' && brackets == 0) {
                        foundMiddle = i;
                        break;
                    }
                }
                if (index == 0) {
                    result[index] = input.substring(0, foundMiddle);
                } else if (index < operandCount - 1) {
                    result[index] = input.substring(prevIndex + 1, foundMiddle);
                } else {
                    result[index] = input.substring(foundMiddle + 1, input.length());
                }
                prevIndex = foundMiddle;
                index++;
            }
        } else {
            result = input.split(",");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting finding operands method");
        }
        return result;
    }

    private Integer evaluate(String input) throws CLException {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering evaluate method");
        }
        String operation = getOperation(input);
        if (logger.isInfoEnabled()) {
            logger.info("Found operation:" + operation);
        }
        String[] operands = null;
        Integer[] operandValue = null;
        operands = getOperands(StringUtils.substring(StringUtils.substringAfter(input, "("), 0, StringUtils.substringAfter(input, "(").length() - 1), commands.get(operation));
        if (operands != null) {
            operandValue = new Integer[operands.length];
            if (operation.equalsIgnoreCase("let")) {
                if (operands[0] != null && operands[1] != null) {
                    if (isFunction(operands[1])) {
                        variable.put(operands[0], evaluate(operands[1]));
                    } else if (StringUtils.isNumeric(operands[1])) {
                        variable.put(operands[0], Integer.parseInt(operands[1]));
                    } else {
                        throw new CLException(CLException.INTEGER_NUMBER_ERROR);
                    }
                }
            }

            for (int i = 0; i < operands.length; i++) {
                if (isFunction(operands[i])) {
                    operandValue[i] = evaluate(operands[i]);
                } else if (StringUtils.isNumeric(operands[i])) {
                    operandValue[i] = Integer.parseInt(operands[i]);
                } else if (variable.keySet().contains(operands[i])) {
                    operandValue[i] = variable.get(operands[i]);
                } else {
                    throw new CLException(CLException.INTEGER_NUMBER_ERROR);
                }
            }
        } else {
            throw new CLException(CLException.MISSING_PARAMETERS_ERROR);
        }
        if (logger.isInfoEnabled()) {
            for (Integer value : operandValue) {
                logger.info("Found operands value:" + value);
            }
        }
        try {
            switch (operation) {
                case "add":
                    return operandValue[0] + operandValue[1];
                case "sub":
                    return operandValue[0] - operandValue[1];
                case "mult":
                    return operandValue[0] * operandValue[1];
                case "div":
                    try {
                        return operandValue[0] / operandValue[1];
                    } catch (ArithmeticException e) {
                        throw new CLException(CLException.DIVIDE_ZERO_ERROR);
                    }
                case "let":
                    return operandValue[2];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CLException(CLException.MISSING_PARAMETERS_ERROR);
        }
        return 0;
    }

    private Boolean isFunction(String operand) {
        if (logger.isDebugEnabled()) {
            logger.debug("Checking whether the operand is a function");
        }
        if (operand != null && (operand.contains("(") || (commands.keySet().contains(operand)))) {
            return true;
        }
        return false;
    }

    public Integer process(String command) throws CLException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started processing command");
        }
        command = command.replaceAll("\\s", "");
        if (checkParantheses(command)) {
            variable = new HashMap<>();
            return evaluate(command);
        } else {
            throw new CLException(CLException.PARANTHESIS_ERROR);
        }

    }
}

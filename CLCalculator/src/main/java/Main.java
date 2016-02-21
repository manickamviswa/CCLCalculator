
import ca.clcalculator.exception.CLException;
import java.util.Scanner;
import org.apache.log4j.Level;
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
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void printHelp() {
        StringBuilder builder = new StringBuilder();

        builder.append("\t Commands: \n\n");
        builder.append("\t add(a,b): Add two integers\n");
        builder.append("\t sub(a,b): Subtract two integers\n");
        builder.append("\t mult(a,b): Multiply two integers \n");
        builder.append("\t div(a,b): Divide two integers\n");
        builder.append("\t let(a,value, operation): Assign variable with a value and execute the operation\n");
        builder.append("\t\t loglevel error : Set the log level either to Error, Debug or Info. By default it will be Error");
        printOutput(builder.toString());
    }

    public static void changeLogger(String level) {

        if (level != null) {
            switch (level.toLowerCase()) {
                case "error":
                    Logger.getRootLogger().setLevel(Level.ERROR);
                    break;
                case "debug":
                    Logger.getRootLogger().setLevel(Level.DEBUG);
                    break;
                case "info":
                    Logger.getRootLogger().setLevel(Level.INFO);
                    break;
                default:
                    Logger.getRootLogger().setLevel(Level.ERROR);
                    break;
            }
            printOutput("Log level changed to " + level.toUpperCase());
        }
    }

    public static void printOutput(String output) {
        System.out.println("********************************************************************************");
        System.out.println(output);
        System.out.println("********************************************************************************");
    }

    public static void main(String[] args) {

        if (logger.isDebugEnabled()) {
            logger.debug("Starting CL Calculator application.....");
        }
        
        System.out.println("********************************************************************************");
        System.out.println("Command Line Calculator");
        System.out.println("Type 'h' or 'help' for help");
        System.out.println("********************************************************************************");
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println(">");
            String command = in.nextLine();
            Calculator c = new Calculator();
            try {
                if (command.contains("loglevel")) {
                    try {
                        String level = command.split(" ")[1];
                        changeLogger(level);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new CLException(CLException.MISSING_PARAMETERS_ERROR);
                    }

                } else if (command.toLowerCase().equals("help") || command.toLowerCase().equals("h")) {
                    printHelp();
                } else {
                    Long result = c.process(command);
                    printOutput(command + " = " + result);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());                
            }
        }

    }

}

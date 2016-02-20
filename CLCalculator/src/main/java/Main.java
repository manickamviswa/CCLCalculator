
import java.util.Scanner;
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

    public static void main(String[] args) {

        logger.debug("Starting CL Calculator application.....");
        System.out.println("********************************************************************************");
        System.out.println("Command Line Calculator");
        System.out.println("Type 'h' for help");
        System.out.println("********************************************************************************");
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println(">");
            String command = in.next();
            Calculator c = new Calculator();
            try {
                int result = c.process(command);
                System.out.println("********************************************************************************");
                System.out.println(command + " = " + result);
                System.out.println("********************************************************************************");
            } catch (Exception e) {
                logger.error(e);
            }
        }

    }

}

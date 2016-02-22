/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ca.clcalculator.main.Calculator;
import ca.clcalculator.exception.CLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Viswanathan Manickam
 */
public class CalculatorTest {

    private Calculator calculator;

    @Rule
    public ExpectedException clException = ExpectedException.none();

    public CalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        calculator = new Calculator();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class Calculator.
     */
    @Test
    public void addTest() throws Exception {

        String command = "add(10,2)";
        Long result = 12L;
        assertEquals(calculator.process(command), result);
    }

    @Test
    public void subTest() throws Exception {

        String command = "sub(10,2)";
        Long result = 8L;
        assertEquals(calculator.process(command), result);
    }

    @Test
    public void addWithOperationTest() throws Exception {

        String command = "add(1, mult(2, 3))";
        Long result = 7L;
        assertEquals(calculator.process(command), result);
    }

    @Test
    public void multiWithOperationTest() throws Exception {

        String command = "mult(add(2, 2), div(9, 3))";
        Long result = 12L;
        assertEquals(calculator.process(command), result);
    }

    @Test
    public void assignmentOperationTest() throws Exception {

        String command = "let(a, 5, add(a, a))";
        Long result = 10L;
        assertEquals(calculator.process(command), result);
    }

    @Test
    public void assignmentWithOperationTest() throws Exception {

        String command = "let(a, 5, let(b, mult(a, 10), add(b, a)))";
        Long result = 55L;
        assertEquals(calculator.process(command), result);
    }

    @Test
    public void mixOperationTest() throws Exception {

        String command = "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))";
        Long result = 40L;
        assertEquals(calculator.process(command), result);
    }

    @Test
    public void paranthesisMissingTest() throws Exception {

        String command = "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))";
        clException.expect(CLException.class);
        clException.expectMessage(CLException.PARANTHESIS_ERROR);
        calculator.process(command);
    }
    
    @Test
    public void commandNotValidTest() throws Exception {

        String command = "addi(1,2)";
        clException.expect(CLException.class);
        clException.expectMessage(CLException.COMMAND_ERROR);
        calculator.process(command);
    }
    
    @Test
    public void missingParamtersTest() throws Exception {

        String command = "add";
        clException.expect(CLException.class);
        clException.expectMessage(CLException.MISSING_PARAMETERS_ERROR);
        calculator.process(command);
    }
    
    @Test
    public void missingParamtersTest2() throws Exception {

        String command = "add(10)";
        clException.expect(CLException.class);
        clException.expectMessage(CLException.MISSING_PARAMETERS_ERROR);
        calculator.process(command);
    }
    
    @Test
    public void divideByZeroError() throws Exception {

        String command = "div(10,0)";
        clException.expect(CLException.class);
        clException.expectMessage(CLException.DIVIDE_ZERO_ERROR);
        calculator.process(command);
    }
    
    @Test
    public void checkFloatValueTest() throws Exception {

        String command = "div(10.4,5)";
        clException.expect(CLException.class);
        clException.expectMessage(CLException.INTEGER_NUMBER_ERROR);
        calculator.process(command);
    }

}

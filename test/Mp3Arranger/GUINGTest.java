/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Mp3Arranger;

import java.awt.event.ActionEvent;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author toffe boy Aamir
 */
public class GUINGTest {
    
    public GUINGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of actionPerformed method, of class GUI.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        ActionEvent e = null;
        GUI instance = new GUI();
        instance.actionPerformed(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showGUI method, of class GUI.
     */
    @Test
    public void testShowGUI() {
        System.out.println("showGUI");
        GUI.showGUI();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initWait method, of class GUI.
     */
    @Test
    public void testInitWait() {
        System.out.println("initWait");
        GUI instance = new GUI();
        instance.initWait();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class GUI.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        GUI.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reEnableButtons method, of class GUI.
     */
    @Test
    public void testReEnableButtons() {
        System.out.println("reEnableButtons");
        GUI instance = new GUI();
        instance.reEnableButtons();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

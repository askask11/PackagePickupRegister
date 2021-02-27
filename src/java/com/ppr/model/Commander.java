/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ppr.model;

//import static CmdExec.s;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jianqing
 */
public class Commander {

    public static String executeCommand(String command) throws InterruptedException, IOException {
        //s = new Scanner(System.in);
        //System.out.print("$ ");
        //String cmd = s.nextLine();
        final Process p = Runtime.getRuntime().exec(command);
        String wholeOutput = "";
        
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            
                while ((line = input.readLine()) != null) {
                    wholeOutput += line;
                }
          
        

        p.waitFor();
        return wholeOutput;
    }
    
    public static void main(String[] args) {
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;

/**
 *
 * @author mzietara
 */
public class CompPlayer {
    int score = 0;
    SosGameView sGV;
    Random rnd = new Random();
    
    CompPlayer (SosGameView sGV) {this.sGV = sGV;}
    
    public JButton randomButton() {

        List<JButton> buttons = new ArrayList();
        
        for (JButton[] buttonRow: sGV.buttonGrid) {
            for (JButton jB: buttonRow) {
                if (jB.getText().equals("")) {buttons.add(jB);
                }
            }
        }
        
        return buttons.get(rnd.nextInt(buttons.size()));
    }
    
    public String randomChoice() {
        String[] choices = {sGV.x[0], sGV.x[1]};
        return choices[rnd.nextInt(2)];
    }
}

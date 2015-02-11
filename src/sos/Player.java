/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sos;

/**
 *
 * @author Mark
 */
public class Player {
    private int score=0;
    private String name;
    
    public Player(String p) {this.name = p;}
    
    public int getScore() {return score;}
    public void setScore(int sc) {score = sc;}
    
    public String getName() {return name;}
    

    
    
    
    public Player copyOf() {
        return new Player(this.getName());
    }
    
    public void incScore() {score += 1;}
    
    
    
    
    
}

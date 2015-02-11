/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SosGame.java
 *
 * Created on 26-Jul-2011, 2:35:03 PM
 */
package sos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 *
 * @author Mark
 */
public class SosGameView extends javax.swing.JFrame {
    JButton[][] buttonGrid;
    private int gridSize;
    private Player p1, p2;
    private Player playerTurn;
    String[] x = {"S", "O"};
    int counter;
    int time;
    CompPlayer comp = new CompPlayer(this);
    

    /** Creates new form SosGame */
    
    public SosGameView(Player p1, Player p2, Player playerTurn, JButton[][] 
            buttonGrid) {
        this.p1 = p1;
        this.p2 = p2;
        this.buttonGrid = buttonGrid.clone();
        this.gridSize = buttonGrid.length;
    }
    
    public SosGameView(String p1, String p2, int gridSize, int time) {
        initComponents();
        this.p1 = new Player(p1);
        this.p2= new Player(p2);
        this.gridSize = gridSize;
        playerTurn = this.p1;
        this.time = time;
        if (time <= 0) {jLabel4.setVisible(false);}
        
        jLabel1.setText("Score");
        jLabel2.setText(p1);
        jLabel3.setText(p2);
        updateScores();
        
        playerTurnLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        MBox.setSelected(true);
        MBox.setText(x[0]);
        andBox.setText(x[1]);
        andBox.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        
        sosPanel.setLayout(new java.awt.GridLayout(gridSize, gridSize));
        createGrid();
        
        if (time > 0) {
            counter = time;
            p.setText(String.valueOf(counter));
            t.start();
        }
        
    }
    
    
    private void createGrid() {
        buttonGrid = new JButton[gridSize ][gridSize];
        int b=16;
        
        if (gridSize <= 14) {b = 16;}
        else if (gridSize == 15) {b = 13;}
        else if (gridSize == 16) {b = 10;}
        else if (gridSize == 17) {b = 7;}
        
        playerTurnLabel.setText(playerTurn.getName() + "'s Turn!");
        for (int i=0; i < gridSize; i++) {
            for (int j=0; j < gridSize; j++) {
                final JButton jB = new JButton();
                buttonGrid[i][j] = jB;
                jB.setText("");
                jB.setName( + i + "," + j);
                jB.setFont(new Font("Comic Sans MS", Font.BOLD,b));
                jB.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    makeChoice(jB, true);
                }
                });
                sosPanel.add(buttonGrid[i][j]);
            }
        }
    }
    
    javax.swing.Timer t = new javax.swing.Timer(1000, new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              if (counter > 0) {
                  if (counter != 0) {counter--;}
                  p.setText(String.valueOf(counter));
              }
              
              else {
                  p.setText(String.valueOf(counter));
                  t.stop();
                  JButton temp = comp.randomButton();
                  
                  int ii = Integer.parseInt(temp.getName().split(",")[0]);
                  int jj = Integer.parseInt(temp.getName().split(",")[1]);
                  buttonGrid[ii][jj].setText(comp.randomChoice());
                  makeChoice(buttonGrid[ii][jj], false);
              }
              
              
          }
       });
    
    public void makeChoice(JButton jB, boolean isPlayer) {
        

        
        if (jB.getText().equals("") || !isPlayer){
            
            if (isPlayer) {
                if (MBox.isSelected()) {jB.setText(x[0]);}
                else {jB.setText(x[1]);}
            }

            checkForPoints(jB);
                        
            if (gameEnd()) {
                System.out.println("Game Over!");
                t.stop();
                new EndGameDialog(playerWinning()).setVisible(true);
            }
            
            else if (time > 0) {
                counter = time;
                p.setText(String.valueOf(counter));
                t.restart();
            }     
        }
    }
    
    
    
    public void updateScores() {
        p1ScoreText.setText(String.valueOf(p1.getScore()));
        p2ScoreText.setText(String.valueOf(p2.getScore()));
    }

    
    
    /* ___________________THIS IS THE ACTUAL GAME METHODS!!!!____________
     * _______________*******************************************________
     * ___________***************************************************____
     *________***********************************************************
     * 
     */
    
    public boolean gameEnd() {
        
        
        //Check to see if there are any non-played spots in the grid.
        for (int i=0; i < gridSize; i++) {
            for (int j=0; j < gridSize; j++) {
                if (buttonGrid[i][j].getText().equals("")) {return false;}
            }
        }
        return true;
    }

    
    
    public String playerWinning() {
        if (p1.getScore() > p2.getScore()) {return p1.getName();}
        else if (p1.getScore() < p2.getScore()) {return p2.getName();}
        else {return "Tie";}        
    }


    public void checkForPoints(JButton jB) {
        boolean gotPts = false;
        int i = Integer.parseInt(jB.getName().split(",")[0]);
        int j = Integer.parseInt(jB.getName().split(",")[1]);
        String cur = jB.getText();
        
        
        if (cur.equals(x[1])) {
            
            if (((i - 1) >= 0) && ((i + 1) < gridSize)) {
                if (((j - 1) >= 0) && ((j + 1) < gridSize)) {
                    if (buttonGrid[i - 1][j - 1].getText().equals(x[0]) &&
                        buttonGrid[i+1][j + 1].getText().equals(x[0])) {
                            playerTurn.incScore();gotPts = true;
                            buttonGrid[i-1][j-1].setForeground(Color.red);
                            buttonGrid[i+1][j+1].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                    }
                    if (buttonGrid[i - 1][j + 1].getText().equals(x[0]) &&
                        buttonGrid[i + 1][j - 1].getText().equals(x[0])) {
                            playerTurn.incScore();gotPts = true;
                            buttonGrid[i-1][j+1].setForeground(Color.red);
                            buttonGrid[i+1][j-1].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                    }  
                }
                
                if (buttonGrid[i - 1][j].getText().equals(x[0]) &&
                        buttonGrid[i+1][j].getText().equals(x[0])) {
                            playerTurn.incScore();gotPts = true;
                            buttonGrid[i-1][j].setForeground(Color.red);
                            buttonGrid[i+1][j].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                }    
            }
            
            if (((j - 1) >= 0) && ((j + 1) < gridSize)) {
                if (buttonGrid[i][j - 1].getText().equals(x[0]) &&
                        buttonGrid[i][j + 1].getText().equals(x[0])) {
                            playerTurn.incScore();gotPts = true;   
                            buttonGrid[i][j-1].setForeground(Color.red);
                            buttonGrid[i][j+1].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                }
            }
        }
        
        else if (cur.equals(x[0])) {
            
            if ((i - 2) >= 0) {
                
                if (((j - 2) >= 0)) {
                    if (buttonGrid[i - 2][j - 2].getText().equals(x[0]) &&
                            buttonGrid[i - 1][j - 1].getText().equals(x[1])) {
                            playerTurn.incScore();gotPts = true;
                            buttonGrid[i-2][j-2].setForeground(Color.red);
                            buttonGrid[i-1][j-1].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                    }
                }
                
                if ((j + 2) < gridSize) {
                    if (buttonGrid[i - 2][j + 2].getText().equals(x[0]) &&
                            buttonGrid[i - 1][j + 1].getText().equals(x[1])) {
                            playerTurn.incScore();gotPts = true;  
                            buttonGrid[i-2][j+2].setForeground(Color.red);
                            buttonGrid[i-1][j+1].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                    }                    
                }
                if (buttonGrid[i - 2][j].getText().equals(x[0]) &&
                        buttonGrid[i - 1][j].getText().equals(x[1])) {
                        playerTurn.incScore();gotPts = true;
                        buttonGrid[i-2][j].setForeground(Color.red);
                        buttonGrid[i-1][j].setForeground(Color.red);
                        buttonGrid[i][j].setForeground(Color.red);
                }
            }    
            
            if ((i + 2) < gridSize) {
                
                if (((j - 2) >= 0)) {
                    if (buttonGrid[i + 2][j - 2].getText().equals(x[0]) &&
                            buttonGrid[i + 1][j - 1].getText().equals(x[1])) {
                            playerTurn.incScore();gotPts = true;
                            buttonGrid[i+2][j-2].setForeground(Color.red);
                            buttonGrid[i+1][j-1].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                    }
                }
                
                if ((j + 2) < gridSize) {
                    if (buttonGrid[i + 2][j + 2].getText().equals(x[0]) &&
                            buttonGrid[i + 1][j + 1].getText().equals(x[1])) {
                            playerTurn.incScore();gotPts = true;    
                            buttonGrid[i+2][j+2].setForeground(Color.red);
                            buttonGrid[i+1][j+1].setForeground(Color.red);
                            buttonGrid[i][j].setForeground(Color.red);
                    }                    
                }
                if (buttonGrid[i + 2][j].getText().equals(x[0]) &&
                        buttonGrid[i + 1][j].getText().equals(x[1])) {
                        playerTurn.incScore();gotPts = true;
                        buttonGrid[i+2][j].setForeground(Color.red);
                        buttonGrid[i+1][j].setForeground(Color.red);
                        buttonGrid[i][j].setForeground(Color.red);
                }
            }
            
            if ((j - 2) >= 0) {
                if (buttonGrid[i][j - 2].getText().equals(x[0]) &&
                        buttonGrid[i][j - 1].getText().equals(x[1])) {
                        playerTurn.incScore();gotPts = true;
                        buttonGrid[i][j-2].setForeground(Color.red);
                        buttonGrid[i][j-1].setForeground(Color.red);
                        buttonGrid[i][j].setForeground(Color.red);
                }                
            }
            
            if ((j + 2) < gridSize) {
                if (buttonGrid[i][j + 2].getText().equals(x[0]) &&
                        buttonGrid[i][j + 1].getText().equals(x[1])) {
                        playerTurn.incScore();gotPts = true;
                        buttonGrid[i][j+2].setForeground(Color.red);
                        buttonGrid[i][j+1].setForeground(Color.red);
                        buttonGrid[i][j].setForeground(Color.red);
                }                
            }
        }
        
        
        //If the player didn't gain a point, make it the next player's turn.
        if (!gotPts) {
            if (playerTurn.equals(p1)) {playerTurn = p2;}
                else {playerTurn = p1;}
                playerTurnLabel.setText(playerTurn.getName() + "'s Turn!");
                }
        
        updateScores();
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sosPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        andBox = new javax.swing.JCheckBox();
        MBox = new javax.swing.JCheckBox();
        playerTurnLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        p1ScoreText = new javax.swing.JLabel();
        p2ScoreText = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        p = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        sosPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sosPanel.setName("sosPanel"); // NOI18N
        sosPanel.setPreferredSize(new java.awt.Dimension(660, 660));

        javax.swing.GroupLayout sosPanelLayout = new javax.swing.GroupLayout(sosPanel);
        sosPanel.setLayout(sosPanelLayout);
        sosPanelLayout.setHorizontalGroup(
            sosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );
        sosPanelLayout.setVerticalGroup(
            sosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sos.SosApp.class).getContext().getResourceMap(SosGameView.class);
        andBox.setText(resourceMap.getString("andBox.text")); // NOI18N
        andBox.setName("andBox"); // NOI18N
        andBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                andBoxActionPerformed(evt);
            }
        });

        MBox.setFont(resourceMap.getFont("MBox.font")); // NOI18N
        MBox.setText(resourceMap.getString("MBox.text")); // NOI18N
        MBox.setName("MBox"); // NOI18N
        MBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MBox, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(andBox, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                        .addGap(6, 6, 6))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(MBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(andBox))
        );

        playerTurnLabel.setName("playerTurnLabel"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setName("jLabel3"); // NOI18N

        p1ScoreText.setName("p1ScoreText"); // NOI18N

        p2ScoreText.setName("p2ScoreText"); // NOI18N

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setName("jSeparator2"); // NOI18N

        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(p2ScoreText)
                                    .addComponent(p1ScoreText)))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(p1ScoreText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(p2ScoreText)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addContainerGap())
        );

        p.setFont(resourceMap.getFont("p.font")); // NOI18N
        p.setForeground(resourceMap.getColor("p.foreground")); // NOI18N
        p.setText(resourceMap.getString("p.text")); // NOI18N
        p.setName("p"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(p, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(playerTurnLabel)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(playerTurnLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(p, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(307, 307, 307))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MBoxActionPerformed
        andBox.setSelected(!MBox.isSelected());
    }//GEN-LAST:event_MBoxActionPerformed

    private void andBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_andBoxActionPerformed
        MBox.setSelected(!andBox.isSelected());
    }//GEN-LAST:event_andBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SosGameView("p1", "p2", 10, -1).setVisible(true);
            }
        });
    }
    
    public JButton[][] getButtonGrid() {return buttonGrid;}
    public Player getP1() {return p1;}
    public Player getP2() {return p2;}
    public int getGridSize() {return gridSize;}
    public Player getCurrentPlayer() {return playerTurn;}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox MBox;
    private javax.swing.JCheckBox andBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel p;
    private javax.swing.JLabel p1ScoreText;
    private javax.swing.JLabel p2ScoreText;
    private javax.swing.JLabel playerTurnLabel;
    private javax.swing.JPanel sosPanel;
    // End of variables declaration//GEN-END:variables
}

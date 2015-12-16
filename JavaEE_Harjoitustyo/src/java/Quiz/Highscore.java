/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Quiz;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;


/**
 *
 * @author Jani
 */
@ManagedBean
@ApplicationScoped
public class Highscore {
    int highscore1;
    String highname1;
    int highscore2;
    String highname2;
    
    
    public Highscore(){
        //TODO Read from storage
        this.highscore1 = 0;
        this.highname1 = "no highscores";
        
        this.highscore2 = 0;
        this.highname2 = "";
    }
    
    public void addToHighscore(int newScore, String newName){
        
        //Check that valid
        if (this.highscore1 < newScore){
            //Add to list
            this.highscore2 = this.highscore1;
            this.highname2 = this.highname1;
            
            this.highscore1 = newScore;
            this.highname1 = newName;
        }else if (this.highscore2 < newScore){
            //Add to list
            this.highscore2 = newScore;
            this.highname2 = newName;
        }
        
    }
    
    public String getHighscore(){
        String newStr = "";
        newStr += this.highscore1 +"";
        newStr += " - ";
        newStr += this.highname1;
        newStr += "\n";
        newStr += this.highscore2 +"";
        newStr += " - ";
        newStr += this.highname2;
        
        return newStr;
    }
    
}

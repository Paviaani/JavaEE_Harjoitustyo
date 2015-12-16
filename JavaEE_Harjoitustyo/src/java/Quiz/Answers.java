/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Quiz;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 *
 * @author Jani
 */
@ManagedBean
@SessionScoped
public class Answers {

    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String question;
    private String correct;
    private String result;
    private String name;

    private boolean showNext = false;   //To hide/show next-button
    private int questionCount = 10;
    private int answerCount = 0;
    private int correctAnswerCount = 0;
    
    private Highscore hs = new Highscore();

    private Random rnd = new Random();  //Random seed

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getQuestion() {
        return question;
    }

    public String qetResult() {
        return result;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String new_name){
        this.name = new_name;
    }
    
    public String getshowNext() {
        if (this.showNext) {
            return "";
        } else {
            return "visibility:hidden;";
        }
    }
    
    public String getQuestionCount() {
        return this.questionCount +"";
    }

    public String getAnswerCount() {
        return this.answerCount +"";
    }
    
    public String getCorrectAnswerCount() {
        return this.correctAnswerCount +"";
    }
    
    
    public String getHighscore(){
        return this.hs.getHighscore();
    }
            

    public String next() {
        if (this.answerCount >= this.questionCount) {
            this.hs.addToHighscore(this.correctAnswerCount, this.name);
            clearValues();
            return "index";
        }
        setAnswer2();

        //Hide next button
        this.showNext = false;
        this.result = "";
        
        return "#";
    }
    
    private void clearValues(){
        this.answer1 = "";
        this.answer2 = "";
        this.answer3 = "";
        this.answer4 = "";
        this.question = "";
        this.correct = "";
        this.result = "";

        this.showNext = false;
        this.questionCount = 10;
        this.answerCount = 0;
        this.correctAnswerCount = 0;

    }

    public String startGame() {
        
        clearValues();

        //Make next question
        next();

        //Change page
        return "play";
    }
    

    public String checkAns(String asw) {

        //Use as next button is answered
        if (showNext) {
            if (this.answerCount >= this.questionCount) {
                this.hs.addToHighscore(this.correctAnswerCount, Calendar.DATE+"/"+Calendar.HOUR +":"+Calendar.MINUTE);
                clearValues();
                return "index";
            } else {
                return "#";
            }

        }

        //Check and show correct answer
        if (asw.equals(this.correct)) {
            this.result = "Correct";
            correctAnswerCount++;
        } else {
            this.result = "Wrong";
        }

        this.answerCount++;

        //Show next button
        this.showNext = true;

        return "#";
    }

    //Function to generate random number
    private int randomNumber(int min, int max) {
        //from 0 to max
        return rnd.nextInt(max - min) + min;
    }

    private void setAnswer2() {
        try {

            //Read files
            URL url1 = getClass().getResource("Answers.xml");
            URL url2 = getClass().getResource("Questions.xml");
            SAXReader readerA = new SAXReader();
            SAXReader readerQ = new SAXReader();
            Document documentA = readerA.read(url1.getFile());
            Document documentQ = readerQ.read(url2.getFile());
            List<Node> nodesA = documentA.selectNodes("/questions/answers");
            List<Node> nodesQ = documentQ.selectNodes("/questions/question");

            //Random number and find index
            int place = randomNumber(1, nodesA.size());
            int placeIndex = findNode(place, nodesA);
            List<Node> subNodesA = nodesA.get(placeIndex).selectNodes("answer");
            
            List<Integer> numbers = new ArrayList<>();
            int newNum = 0;
            int itIsLoop = 0;
            
            while(numbers.size() < 5 && itIsLoop < 1000)
            {
                newNum = randomNumber(0,7);
                itIsLoop++;

                if ( !numbers.contains(newNum))  //If not all ready exist
                {
                    if (numbers.size() == 3)   //if last round check for correct asnwer
                    {
                        if (numbers.contains(0)){
                            numbers.add(newNum);
                        }
                        else {
                            newNum = 0;
                            numbers.add(newNum);
                        }
                    }
                    else
                    {
                        numbers.add(newNum);  //add new number
                    }                    
                }
            }            
            
            //pollute UI
            this.answer1 = subNodesA.get(numbers.get(0)).getText();
            this.answer2 = subNodesA.get(numbers.get(1)).getText();
            this.answer3 = subNodesA.get(numbers.get(2)).getText();
            this.answer4 = subNodesA.get(numbers.get(3)).getText();
            this.correct = subNodesA.get(0).getText();
            this.question = nodesQ.get(placeIndex).getText();

        } catch (DocumentException e) {
            this.question = e.getMessage();
        }

    }

    //Function to find XMLNode index with given ID
    private int findNode(int index, List<Node> nodes) {
        int count = nodes.size();
        for (int i = 0; i < count; i++) {
            if (nodes.get(i).valueOf("@ID").equals(index + "")) {
                return i;
            }
        }
        return -1;
    }
}

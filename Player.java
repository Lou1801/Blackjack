import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Player implements ActionListener {

    int xPos = 1000;
    int xPos1 = 1000;
    int xPos2 = 1000;

    int xPos3 = 1000;
    int xPos4 = 1000;
    int xPos5 = 1000;
    int xPos6 = 1000;
    int xPos7 = 1000;
    int xPos8 = 1000;
    int xPos9 = 1000;
    int xPos10 = 1000;

    int score;
    boolean hit1 = false;
    boolean hit2 = false;
    boolean hit3 = false;
    boolean hit4 = false;
    boolean revealDealersHand = false;
    boolean dealerHit1 = false;
    boolean dealerHit2 = false;
    boolean dealerHit3 = false;
    boolean reshuffle = false;

    Color vDarkGreen = new Color(0, 102, 0);

    Timer timer;

    public Player() {
        score = 0;

    }

    public void dealDraw(Graphics2D g2D, Image card1, Image card2, Image card3, Image card4, Image hitCard1, Image hitCard2, Image hitCard3, Image hitCard4,
                         Image dealerCardHit1, Image dealerCardHit2, Image dealerCardHit3) {

        timer = new Timer(1200, this);
        timer.start();



            g2D.setPaint(Color.BLACK);
            g2D.drawImage(card1, xPos, 500, null);
            g2D.drawImage(card2, xPos1, 200, null);
            g2D.drawImage(card3, xPos2, 500, null);
            g2D.drawImage(card4, xPos3, 200, null);


        if (hitCard1 != null) {
            hit1 = true;
            g2D.drawImage(hitCard1, xPos4, 500, null);
        }  if (hitCard2 != null) {
            hit2 = true;
            g2D.drawImage(hitCard2, xPos5, 500, null);
        }  if (hitCard3 != null) {
            hit3 = true;
            g2D.drawImage(hitCard3, xPos6, 500, null);
        } if (hitCard4 != null) {
            hit4 = true;

            xPos = 100;
            xPos2 = 250;
            xPos4 = 400;
            xPos5 = 550;
            xPos6 = 700;

            g2D.drawImage(hitCard4, xPos10, 500, null);
        }

        if (!revealDealersHand) {
            g2D.fillRect(xPos3, 200, 128, 176);
        }

        if (dealerCardHit1 != null) {
            dealerHit1 = true;
            g2D.drawImage(dealerCardHit1, xPos7, 200, null);
        }  if (dealerCardHit2 != null) {
            dealerHit2 = true;
            g2D.drawImage(dealerCardHit2, xPos8, 200, null);
        }  if (dealerCardHit3 != null) {
            dealerHit3 = true;
            g2D.drawImage(dealerCardHit3, xPos9, 200, null);
        }

    }

    public void split(Graphics2D g2D){
        timer.stop();

    }

    public void showDealerHand(){
        revealDealersHand = true;
    }

    public void hideDealerHand(){
        revealDealersHand = false;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (xPos > 200) {
            xPos = xPos - 10;
        } else if (xPos1 > 200 && xPos == 200) {
            xPos1 = xPos1 - 10;
        } else if (xPos2 > 350 && xPos1 == 200) {
            xPos2 = xPos2 - 10;
        } else if (xPos3 > 350 && xPos2 == 350) {
            xPos3 = xPos3 - 10;
        }
        else if (hit1 && xPos4 > 500 && xPos3 == 350) {
            xPos4 = xPos4 - 10;
        } else if (hit2 && xPos5 > 650 && xPos4 == 500) {
            xPos5 = xPos5 - 5;
        } else if(hit3 && xPos6 > 800 && xPos5 == 650){
            xPos6 = xPos6 - 5;
        }
        else if(dealerHit1 && xPos7 > 500 && xPos3 == 350 ){
            xPos7 = xPos7 -5;
        }else if(dealerHit2 && xPos8 > 650 && xPos7 == 500 ){
            xPos8 = xPos8 -5;
        }else if(dealerHit3 && xPos9 > 800 && xPos8 == 650 ){
            xPos9 = xPos9 -5;
        }

    }
}


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


// Apparently implementing the listeners as follows is bad practice

public class MyPanel extends JLayeredPane implements KeyListener, MouseListener, Runnable {
    public static ArrayList<BufferedImage> deck = new ArrayList();
    List<Integer> dealtCards = new ArrayList<>();
    Image image;

    Boolean gameStart = false;
    Boolean deal = false;
    Boolean showDealerHand = false;
    BigDecimal credit = new BigDecimal(10.00);
    BigDecimal betMultiplier = new BigDecimal(2);


    public Player p1;

    File folder = new File("C:\\Users\\Louis\\IdeaProjects\\Blackjack1\\Cards");

    BigDecimal bet = new BigDecimal(0.10);
    BigDecimal lowLimit = new BigDecimal(0.10);

    Thread thread;

    int [] xPointsUp = {50, 75, 100};
    int [] yPointsUp = {650, 575, 650};

    int [] xPointsDown = {50, 75, 100};
    int [] yPointsDown = {700, 775, 700};

    private Polygon upTriangle, downTriangle;

    Image card1;
    Image card2;
    Image card3;
    Image card4;
    Image hitCard1 = null;
    Image hitCard2 = null;
    Image hitCard3 = null;
    Image hitCard4 = null;
    Image dealearCardHit1 = null;
    Image dealearCardHit2 = null;
    Image dealearCardHit3 = null;
    int playerScore = 0;
    int dealerScore = 0;
    int firstCards = 0;

    Color vDarkGreen = new Color(0, 102, 0);

    List <BufferedImage> chosenCard = new ArrayList<>();

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();



    MyPanel(){
        image = new ImageIcon("C:\\Users\\Louis\\IdeaProjects\\Blackjack1\\gambler.jpg").getImage();

        setPreferredSize(new Dimension (1000, 850));
        addKeyListener(this);
        addMouseListener(this);
        // KeyListener and MouseListener need to be registered for every JComponent including frame and panel
        // adding them to 'this' adds them to the panel, but the frame calls the panel and main calls the frame so its like russian dolls

        upTriangle = new Polygon(xPointsUp, yPointsUp, 3);
        downTriangle = new Polygon(xPointsDown, yPointsDown, 3);

        p1 = new Player();
        thread = new Thread(this);
        thread.start();

        File [] files = folder.listFiles();
        for (File file : files){
            try {
                BufferedImage image = ImageIO.read(file);
                deck.add(image);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void paintComponent(Graphics g){

        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\Louis\\IdeaProjects\\Blackjack\\src\\TEXASTANGO EXTRA ROTH PERSONAL USE.ttf"));
            ge.registerFont(customFont);
            Font biggerFont = customFont.deriveFont(Font.TRUETYPE_FONT, 75f);


            Graphics2D g2D = (Graphics2D) g;
            g2D.setPaint(Color.BLACK);
            g2D.setFont(biggerFont);



            if (!gameStart) {
                g2D.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                g2D.drawString("BlackJack", 350, 75);
                g2D.drawString("Start", 200, 600);
                g2D.drawString("Quit", 650, 600);
            }
            else if (gameStart){
                // p1.dealDraw(g2D);
                betDraw(g2D);
                if (deal) {

                    p1.dealDraw(g2D, card1, card2, card3, card4, hitCard1, hitCard2, hitCard3, hitCard4,dealearCardHit1, dealearCardHit2, dealearCardHit3);
                    //p1.split(g2D);

                    if (showDealerHand){
                        p1.showDealerHand();
                    }
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }

    }
    public void run() {
        while (true) {
            if (gameStart) {
                // Perform game-related logic here
                // Call repaint whenever the visual state needs to be updated
                repaint();
            }
            try {
                Thread.sleep(10); // Add a small delay to control the update rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public void betDraw(Graphics2D g2D) {
        g2D.setPaint(vDarkGreen);
        g2D.fillRect(0,0,1000,850);

        g2D.setPaint(Color.white);
        //g2D.drawPolygon(xPoints, yPoints, 3);

        g2D.fillPolygon(upTriangle);
        g2D.fillPolygon(downTriangle);

        g2D.setFont(new Font("TimesRoman", Font.PLAIN, 18));

        String formattedBet = String.format("%.02f", bet);
        g2D.drawString("£ " + formattedBet, 50, 680);

        g2D.setPaint(Color.black);

        g2D.drawRect(800, 720, 100, 80);
        // double
        g2D.drawRect(680, 720, 100, 80);
        // split
        g2D.drawRect(560, 720, 100, 80);
        // stand
        g2D.drawRect(440, 720, 100, 80);
        // hit
        g2D.drawRect(200, 720, 100, 80);
        // deal

        g2D.drawString("Hit", 480, 765);
        g2D.drawString("Stand", 590, 765);
        g2D.drawString("Split",715, 765 );
        g2D.drawString("Double", 825, 765);

        g2D.drawString("Deal", 230, 765);

        String formattedCredit = String.format("%.02f", credit);
        g2D.drawString(formattedCredit, 325, 765 );

    }

    private Image getImage() {
        Image image = createTexture(getRandomImage(chosenCard));
        return image;
    }

    private Image createTexture(BufferedImage image) {
        BufferedImage resizedImage = resizeImage(image, 128, 176);
        return resizedImage;
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        double scaleX = (double) newWidth / originalImage.getWidth();
        double scaleY = (double) newHeight / originalImage.getHeight();

        AffineTransform transform = AffineTransform.getScaleInstance(scaleX, scaleY);

        // Create the AffineTransformOp and apply the transformation to the original image
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        op.filter(originalImage, resizedImage);

        return resizedImage;
    }


    private  BufferedImage getRandomImage(List<BufferedImage> chosenCards){
        Random random = new Random();

       // System.out.println(chosenCards.length);
        // Determine the new size of the array

        BufferedImage getCard = deck.get(random.nextInt(deck.size()));
        String filename = "";

        if (!chosenCards.contains(getCard)) {
                chosenCards.add(getCard);
                // prevents duplicates
            // Find the filename corresponding to the BufferedImage
            File [] files = folder.listFiles();
            for (File file : files) {
                try {
                    BufferedImage image = ImageIO.read(file);
                    if (areImagesEqual(image, getCard)) {
                        filename = file.getName();
                        //System.out.println(filename);
                        cardCount(filename);
                        break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
                return getCard;

        }
        else { return getRandomImage(chosenCards);
            // calls itself again to pick another card that isn't a duplicate
        }

    }


    // Helper method to compare BufferedImage objects for equality based on pixel data
    private boolean areImagesEqual(BufferedImage image1, BufferedImage image2) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            return false;
        }

        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getWidth(); x++) {
                if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void cardCount(String filename){
        System.out.println(filename);
        boolean value2 = filename.matches("^\\d{2}.*");
        boolean value1 = filename.matches("^\\d.*");
        int cardValue = 0;

        int i = filename.indexOf('_');
        String faceCard = filename.substring(0, i);

        if (value1){
            String  substring = filename.length() < 1 ? filename : filename.substring(0, 1);
            if (value2){
                substring = filename.length() < 2 ? filename : filename.substring(0, 2);

            }
            // : filename  This is the expression to evaluate if the condition is false. In this case, it simply assigns the original string filename to the variable substring.
            System.out.println(substring);
            cardValue = Integer.parseInt(substring);
            dealtCards.add(cardValue);
        }
        else if (faceCard.equals("king") || faceCard.equals("queen")){
            dealtCards.add(10);

        }

        else if (faceCard.equals("ace")){
            int checkAce = playerScore + 11;
            if (checkAce > 21){
                dealtCards.add(1);
            }
            else {
                dealtCards.add(11);
            }
        }
            System.out.println(dealtCards);
        System.out.println(dealtCards.get(dealtCards.size() - 1));
    }

    public void updateScore(){

         if (firstCards < 3 && dealtCards.size() % 2 != 0) {
            playerScore += dealtCards.get(dealtCards.size() - 1);
            firstCards++;
        } else if (firstCards < 4 && dealtCards.size() % 2 == 0){
            dealerScore += dealtCards.get(dealtCards.size() - 1);
            firstCards++;
         }
         else if (showDealerHand){
             dealerScore += dealtCards.get(dealtCards.size() - 1);
         }

        else if(hitCard1 != null ||  hitCard2 != null){
            playerScore += dealtCards.get(dealtCards.size() - 1);
        }


        System.out.println("Dealer = " + dealerScore + " Player = " + playerScore);

        checkBust();
        blackJack();
    }

    public void blackJack(){

        if (playerScore == 21 && hitCard4 != null){
            // maybe have an object that stores the value of the bet times by 2? Current code does not use this method this is just a suggestion.
            System.out.println("Player has blackjack");
            showDealerHand = true;
            credit = credit.add(bet);
            credit = credit.add(bet);
            // this is how we add BigDecimal
            scheduleResetTable();
        }
        if (dealerScore == 21 && showDealerHand){
            System.out.println("Dealer has blackjack");
            credit = credit.subtract(bet);
            scheduleResetTable();
        }
        if (showDealerHand && dealerScore > playerScore && dealerScore >= 17 &&  dealerScore < 21){
            System.out.println("Dealer wins hand");
            credit = credit.subtract(bet);
            scheduleResetTable();
        }
        if (showDealerHand && playerScore > dealerScore && playerScore < 21){
            System.out.println("Player wins hand");
            credit = credit.add(bet);
            credit = credit.add(bet);
            scheduleResetTable();
        }
        if (showDealerHand && dealerScore == playerScore && dealerScore >= 17 && dealerScore < 22){
            // we need dealerScore to be checked if it is below 17 as if when the cards are dealt and the player and the
            // dealer have the same card value total, when the player stands if the player and dealer are still below 17
            // the dealer won't hit another card because scheduleResetTable will be called which will interrupt the hand
            // we need showDealerHand to be in the equation as it ensures the table isn't reset before the dealer reveals their hand
            System.out.println("Push");
            scheduleResetTable();
        }

    }

    public void checkBust(){
        if(playerScore > 21) {
            System.out.println("Player bust");
            showDealerHand = true;
            credit = credit.subtract(bet);
            scheduleResetTable();
        }
        else if (showDealerHand && dealerScore > 21 && playerScore <= 21){
            System.out.println("Dealer bust");
            credit = credit.add(bet);
            credit = credit.add(bet);
            scheduleResetTable();
        }

    }


    private void scheduleResetTable(){
        executorService.schedule(this::resetTable, 5, TimeUnit.SECONDS);
    }

    public void resetTable(){

        card1 = null;
        card2 = null;
        card3 = null;
        card4 = null;
        hitCard1 = null;
        hitCard2 = null;
        hitCard3 = null;
        dealearCardHit1 = null;
        dealearCardHit2 = null;
        dealearCardHit3 = null;

        deck.clear();
        dealtCards.clear();

        deal = false;
        showDealerHand = false;

        playerScore = 0;
        dealerScore = 0;
        firstCards = 0;

        p1.hideDealerHand();
        new MyPanel();
        // new panel gets another singleThread
        //executorService.shutdown();



    }

private void dealerHand(){
    if (dealerScore < 17 && dealearCardHit1 == null) {
        dealearCardHit1= getImage();
        updateScore();
    }
    else if (dealerScore < 17 && dealearCardHit2 == null) {
        dealearCardHit2 = getImage();
        updateScore();
    }
    else if (dealerScore < 17 && dealearCardHit3 == null) {
        dealearCardHit3 = getImage();
        updateScore();
    }

    if (dealerScore < 17){dealerHand();}

    //else blackJack();
    // we might not need to call it here because its already called every time through updateScore.
}



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (upTriangle.contains(e.getX(), e.getY()) && gameStart) {
            System.out.println("bet increased");
            bet = bet.add(new BigDecimal(0.10));
           
        }

        if (downTriangle.contains(e.getX(), e.getY()) && !(bet.compareTo(lowLimit) == 0) &&  gameStart) {
            System.out.println("bet decreased");
            bet = bet.subtract(new BigDecimal(0.10));

        }


        if (e.getX() >= 200 && e.getX() <= 380 && e.getY() >= 545 && e.getY() <= 600 && !gameStart){
            System.out.println("start button pressed");
            gameStart = true;

        }
        if (e.getX() >= 650 && e.getX() <= 775 && e.getY() >= 545 && e.getY() <= 605 && !gameStart){
            System.out.println("Quit button pressed");
            System.exit(0);
        }

        if (e.getX() >= 440 && e.getX() <= 540 && e.getY() >= 720 && e.getY() <= 800 && gameStart){
            System.out.println("Hit button pressed");
            if (hitCard1 == null) {
                hitCard1 = getImage();
                updateScore();
            }
             else if (hitCard2 == null) {
                hitCard2 = getImage();
                updateScore();
            }
             else if (hitCard3 == null){
                hitCard3 = getImage();
                updateScore();
            }
            else if (hitCard4 == null){
                hitCard4 = getImage();
                updateScore();
            }
        }

        if (e.getX() >= 560 && e.getX() <= 6660 && e.getY() >= 720 && e.getY() <= 800 && gameStart){
            System.out.println("Stand button pressed");
            showDealerHand = true;
            dealerHand();
        }

        if (e.getX() >= 200 && e.getX() <= 300 && e.getY() >= 720 && e.getY() <= 800 && gameStart){
            System.out.println("Deal pressed");
            deal = true;
            card1 = getImage();
            updateScore();
            card2 = getImage();
            updateScore();
            card3 = getImage();
            updateScore();
            card4 = getImage();
            updateScore();

        }

    }




    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println(MouseInfo.getPointerInfo().getLocation());

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}

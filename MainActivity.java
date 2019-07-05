package com.saurabhtiwari.noughtsandcross;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static boolean gameIsActive = true;
    private static int playerTurn = 0; // 0 will be yellow
    private static int[] positions = {2,2,2,2,2,2,2,2,2};
    private static int[][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resetGame();
    }

    public void takePosition(View view) {
        Log.i("Info", "Reached the method");
        ImageView image = (ImageView) view;
        int tappedPosition = Integer.parseInt(image.getTag().toString());

        // check if the position is vacant
        if(positions[tappedPosition] == 2 && gameIsActive) {
            //move the image view to top before dropping
            image.setTranslationY(-1000f);

            // mark this position now as taken by the current player
            positions[tappedPosition] = playerTurn;

            if (playerTurn == 0) {
                // lets start with yellow player
                //set the relevant color piece on the image view
                image.setImageResource(R.drawable.yellow);
                playerTurn = 1;
            } else {
                image.setImageResource(R.drawable.red);
                playerTurn = 0;
            }

            // drop the image now to original position
            // may be rotate a bit on drop for fanciness
            image.animate().translationYBy(1000f).rotation(360f).setDuration(100);


            checkForTheWinner();
        } else {
            // display a toast in case the user taps a occupied position
            if(gameIsActive) {
                Toast.makeText(MainActivity.this, "Uh-Oh !! That position is taken", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Game Over. Play Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkForTheWinner() {
        for(int[] winningPosition : winningPositions) {
            if(positions[winningPosition[0]] == positions[winningPosition[1]] &&
                    positions[winningPosition[1]] == positions[winningPosition[2]] &&
                    positions[winningPosition[0]] != 2) {
                String winner = positions[winningPosition[0]] == 0 ? "Yellow" : "Red";

                TextView winnerText = (TextView) findViewById(R.id.winnerText);
                winnerText.setText(winner + " player wins");

                // find the result layout
                LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
                resultLayout.setVisibility(View.VISIBLE);
                resultLayout.animate().alpha(1f).setDuration(500);
                gameIsActive = false;
            } else {
                boolean gameIsOver = true;
                for(int i= 0; i < positions.length; i++) {
                    if(positions[i] == 2) {
                        gameIsOver = false;
                    }
                }

                // if game is over and we are in else part, that means its a draw
                if(gameIsOver) {
                    TextView winnerText = (TextView) findViewById(R.id.winnerText);
                    winnerText.setText("Game Drawn");

                    // find the result layout
                    LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
                    resultLayout.setVisibility(View.VISIBLE);
                    resultLayout.animate().alpha(1f).setDuration(500);
                }
            }
        }
    }

    public void playAgain(View view) {
        resetGame();
    }

    public void resetGame() {
        // find the result layout and hide it
        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
        resultLayout.setVisibility(View.INVISIBLE);
        resultLayout.animate().alpha(0f).setDuration(50);

        // reset the victory text
        TextView winnerText = (TextView) findViewById(R.id.winnerText);
        winnerText.setText("");

        // reset all the meta data to their original values
        playerTurn = 0;

        for(int i =0; i < positions.length; i++) {
            positions[i] = 2;
        }

        // find the board and reset all the images to blank
        GridLayout boardLayout = (GridLayout) findViewById(R.id.board);
        for(int i = 0; i < boardLayout.getChildCount(); i++) {
            // setting src to 0 blanks the image
            ((ImageView) boardLayout.getChildAt(i)).setImageResource(0);
        }
        gameIsActive = true;
    }
}

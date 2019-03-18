package androidapplication.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Two dimensional array
    private Button[][] buttons = new Button[3][3];

    //start of the game player1 will start
    private boolean player1Turn = true;

    //Count number of rounds
    private int roundCount;

    //To keep the track of the players
    private  int PointsForPlayer1;
    private int PointsForPlayer2;

    //To display the points of the player
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the view for player
        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        //Button array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //looping through all button ids dynamically
                String buttonID = "button_" + i + j;
                //pass resId to findViewbyId
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(MainActivity.this);
            }
        }
        //reset button
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();

            }
        });
    }

    @Override
    public void onClick(View v) {
        //Check if button is equal to an empty String
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
         //Check for player1 and player2 turn
        if (player1Turn) {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.parseColor("#20639B"));

        } else {
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.parseColor("#3CAEA3"));
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }

    }

    //return true or false if someone has won or not
    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //Going through all the buttons and saving it in
                // the field string array
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        //String array to go through all the rows and columns
        for (int i = 0; i < 3; i++) {

            //Comparing  three columns fields next to each other
            //Last part is to make sure its not three times an empty field
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        //Comparing  three rows fields next to each other
        //Last part is to make sure its not three times an empty fields
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }


        //For diagonals
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }
    private void player1Wins() {

        PointsForPlayer1++;
        Toast.makeText(MainActivity.this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        resetBoard();
                    }
                });
            }
        };
        thread.start(); //start the thread

    }

    private void player2Wins() {
        PointsForPlayer2++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        resetBoard();
                    }
                });
            }
        };
        thread.start(); //start the thread
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        resetBoard();
                    }
                });
            }
        };
        thread.start(); //start the thread
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + PointsForPlayer1);
        textViewPlayer2.setText("Player 2: " + PointsForPlayer2);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundResource(android.R.drawable.btn_default);

            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        PointsForPlayer1 = 0;
        PointsForPlayer2 = 0;
        updatePointsText();
        resetBoard();
    }

    //On rotate game state will be saved
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("PointsForPlayer1", PointsForPlayer1);
        outState.putInt("PointsForPlayer2", PointsForPlayer2);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        PointsForPlayer1 = savedInstanceState.getInt("PointsForPlayer1");
        PointsForPlayer2 = savedInstanceState.getInt("PointsForPlayer2");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}

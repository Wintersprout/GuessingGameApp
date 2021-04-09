package com.example.guessinggame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.function.Predicate;

public class MainActivity extends AppCompatActivity {

    private EditText txtGuess;
    private Button btnGuess;
    private TextView lblOutput;
    private TextView lblRange;
    private int theNumber, numberOfTries;
    private int range = 100;
    private boolean gameOver;

    public void checkGuess() {
        String guessTxt = txtGuess.getText().toString();
        String message = "";

        try {
            int guess = Integer.parseInt(guessTxt);
            numberOfTries--;

            if (guess < theNumber)
                message = guessTxt + " is too low. " +
                        numberOfTries + " tries left.";
            else if (guess > theNumber)
                message = guessTxt + " is too high. " +
                        numberOfTries + " tries left.";
            else {
                message = "You win!\n" +
                        "Tries left: " + numberOfTries;
                btnGuess.setText("Play Again");
                gameOver = true;
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                //Save the victory in the shared preferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("gamesWon", gamesWon);
                editor.apply();
            } //Win block
        } catch(Exception e) {
            message = "Please enter a whole number between 1 and " + range;
        } finally {
            if ((numberOfTries < 1) && (!gameOver)) {
                gameOver = true;
                message = "You lose!\n" + "The number was " + theNumber;
                btnGuess.setText("Play Again");
                lblOutput.setText(message);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesLost = preferences.getInt("gamesLost", 0) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("gamesLost", gamesLost);
                editor.apply();
            } //Lose block
            else {
                lblOutput.setText(message);
                txtGuess.requestFocus();
                txtGuess.selectAll();
            }
        }
    }
    public void newGame() {
        gameOver = false;
        lblOutput.setText("");
        lblRange.setText("Enter a number between 1 and " + range + ":");
        txtGuess.setText("");
        txtGuess.requestFocus();
        btnGuess.setText("Guess");
        theNumber = (int)(Math.random() * range + 1);

        int i = range;
        numberOfTries = 1;
        while (i > 1) {
            i /= 2;
            numberOfTries++;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView) findViewById(R.id.lblRange);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);
        newGame();
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver)
                    checkGuess();
                else
                    newGame();
            }
        });
        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkGuess();
                return true;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                final CharSequence[] items = {"1 to 10", "1 to 100", "1 to 1000"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select the range:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                range = 10;
                                break;
                            case 2:
                                range = 1000;
                                break;
                            case 1:
                            default:
                                range = 100;
                                break;
                        }
                        storeRange(range);
                        newGame();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing Game");
                aboutDialog.setMessage("(c) 2021 Reno Facundo");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aboutDialog.show();
                return true;
            case R.id.action_game_stats:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0);
                int gamesLost = preferences.getInt("gamesLost", 0);
                int totalGames = gamesWon + gamesLost;

                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Statistics");
                statDialog.setMessage("Games played: " + totalGames + "\nWins: " + gamesWon +
                        "\nWin rate: " + (int)(100 * gamesWon/totalGames) + "%");
                statDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                statDialog.show();
                return true;
            default:
                //return super.onOptionsItemSelected(item);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    public void storeRange(int newRange) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.apply();
    }
}
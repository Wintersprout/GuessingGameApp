package com.example.guessinggame;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtGuess;
    private Button btnGuess;
    private TextView lblOutput;
    private int theNumber, numberOfTries;
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
                        "Number of Tries: " + (7 - numberOfTries);
                btnGuess.setText("Play Again");
                gameOver = true;
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            } //Win block
        } catch(Exception e) {
            message = "Please enter a whole number between 1 and 100.";
        } finally {
            if ((numberOfTries < 1) && (!gameOver)) {
                gameOver = true;
                message = "You lose!\n" +
                        "The number was " + theNumber;
                btnGuess.setText("Play Again");
                lblOutput.setText(message);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
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
        txtGuess.requestFocus();
        theNumber = (int)(Math.random() * 100 + 1);
        //theNumber = 99;
        numberOfTries = 7;
        btnGuess.setText("Guess");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
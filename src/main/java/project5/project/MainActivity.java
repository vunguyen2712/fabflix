package project5.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.text.DecimalFormat;

//Change some text here
public class MainActivity extends ActionBarActivity {

    static SharedPreferences settings = null;
    static SharedPreferences.Editor editor = null;

    public void addTakeQuizListener() {
        Button takeQuizButton = (Button) findViewById(R.id.takeQuiz);
        takeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TakeQuiz.class);
                startActivity(intent);
            }
        });
    }

    public void addShowStatsListener() {
        Button showStatsButton = (Button) findViewById(R.id.showStats);
        showStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setTitle("Statistics");

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                int totalQuizzes, totalCorrectAnswers, totalQuestions;
                if (settings != null && editor != null && settings.getInt("totalQuestions", 0) != 0) {
                    totalQuizzes = settings.getInt("totalQuizzes", 0);
                    totalCorrectAnswers = settings.getInt("totalCorrectAnswers", 0);
                    totalQuestions = settings.getInt("totalQuestions", 0);
                    alertDialog.setMessage("Quizzes taken: " + totalQuizzes + "\n" +
                            "Score: " + totalCorrectAnswers + " / " + totalQuestions +
                            " (" + df.format(((float) totalCorrectAnswers / totalQuestions) * 100) + "%)" + "\n" +
                            "Average time per question: " + df.format((float) (totalQuizzes * TakeQuiz.TIME) / totalQuestions) + " secs");
                }
                else {
                    alertDialog.setMessage("Quizzes taken: 0\n" +
                            "Score: 0 / 0" + " (0%)" + "\n" +
                            "Average time per question: 0 secs");
                }
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Reset", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putInt("totalQuizzes", 0);
                        editor.putInt("totalCorrectAnswers", 0);
                        editor.putInt("totalQuestions", 0);
                        editor.commit();

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);  // restrict user from tapping outside of the dialog
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            settings = getSharedPreferences("StatsPref", 0);
            editor = settings.edit();
        } catch (Exception e) {
            // oh no!
        }

        addTakeQuizListener(); // add listener for take quiz button
        addShowStatsListener(); // add listener for statistics button
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package project5.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.CountDownTimer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import project5.domain.*;
import project5.quiz.*;

public class TakeQuiz extends Activity {

    public static final int TIME = 180; // 180 for 3 mins

    private DataHolder dataHolder;

    // timer
    private CountDownTimer countDownTimer;
    private int currentTimeRemains;

    private TextView scoreView;
    private Button submitButton;
    private Button pauseButton;

    // Radio button choices
    public RadioGroup radioAnswersGroup;
    public RadioButton buttonA;
    public RadioButton buttonB;
    public RadioButton buttonC;
    public RadioButton buttonD;
    public RadioButton chosen;

    public Question question;

    // stats for current game
    public int numOfQuestions = 0;
    public int numOfCorrect = 0;

    // stats of all games
    static int totalQuizzes;
    static int totalCorrectAnswers;
    static int totalQuestions;  // incorrectAnswers = totalQuestions - totalCorrectAnswers
    //  totalTime = TIME/1000 * totalQuizzes;  // in sec

    // SharedPreferences is used to store static data in phone memory
    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    // toggle to true if an answer is submitted, false if user advances to next question
    private boolean submitted = false;

    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashMap<Integer, Movie> movies = CSVLoader.loadMovies(this, "movies.csv");
        HashMap<Integer, Star> stars = CSVLoader.loadStars(this, "stars.csv");
        HashMap<Integer, Star_In_Movie> star_in_movies = CSVLoader.loadStarInMovies(this, "stars_in_movies.csv");
        dataHolder = new DataHolder(movies, stars, star_in_movies);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        // for storing stats data // should be before initTimer
        settings = getSharedPreferences("StatsPref", 0);
        // init stats data
        totalQuizzes = settings.getInt("totalQuizzes", 0);
        totalCorrectAnswers = settings.getInt("totalCorrectAnswers", 0);
        totalQuestions = settings.getInt("totalQuestions", 0);

        editor = settings.edit();

        initButtons();

        resetGame();
    }

    // life cycle fix! upon leaving (but not exiting) the app, taking a phone call etc.,
    // barring any automatic garbage disposal, the game will pause!
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        pauseGame();
    }

    // destroy!
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // cancel the current timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void initButtons(){
        radioAnswersGroup = (RadioGroup) findViewById(R.id.answersGroup);
        buttonA = (RadioButton) findViewById(R.id.buttonA);
        buttonB = (RadioButton) findViewById(R.id.buttonB);
        buttonC = (RadioButton) findViewById(R.id.buttonC);
        buttonD = (RadioButton) findViewById(R.id.buttonD);
        scoreView = (TextView) findViewById(R.id.scoreView);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                determineSubmitAction();
            }});

        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pauseGame();
            }});
    }

    private void initTimer(int time) {
        // create a timer for 180 seconds (3 mins)
        final TextView timer = (TextView)findViewById(R.id.timer);  // create a timer
        countDownTimer = new CountDownTimer(time * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Time left:\n" + String.format("%02d", (millisUntilFinished / 1000/60)) +
                        ":" + String.format("%02d", (millisUntilFinished / 1000)%60));
                --currentTimeRemains;
            }

            public void onFinish() {
                timer.setText("Time's up!");

                // count another game
                ++totalQuizzes;
                // set the static stats values after every game
                totalCorrectAnswers += numOfCorrect;
                totalQuestions += numOfQuestions;
                // store stats data
                editor.putInt("totalQuizzes", totalQuizzes);
                editor.putInt("totalCorrectAnswers", totalCorrectAnswers);
                editor.putInt("totalQuestions", totalQuestions);
                editor.commit();
                // done storing data

                // destroy timer
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }

                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(TakeQuiz.this).create();
                alertDialog.setTitle("Score");

                setAlertDialogMessage(alertDialog);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Play Again", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Main Menu", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {
                        // go back to main menu
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);  // restrict user from tapping outside of the dialog

            }
        }.start();
    }

    private void determineSubmitAction() {
        if (!submitted) {
            submitAnswer();
        }
        else {
            showQuestion();
        }
        submitted = !submitted;
    }

    private void showQuestion() {
        // create a new question
        question = new Question(dataHolder);
        ArrayList<String> list = question.getAnswersInList();

        // set new question
        ((TextView) findViewById(R.id.questionTextView)).setText(question.getQuestion());
        buttonA.setText(list.get(0));
        buttonB.setText(list.get(1));
        buttonC.setText(list.get(2));
        buttonD.setText(list.get(3));

        // set text color to black
        buttonA.setTextColor(getResources().getColor(R.color.black));
        buttonB.setTextColor(getResources().getColor(R.color.black));
        buttonC.setTextColor(getResources().getColor(R.color.black));
        buttonD.setTextColor(getResources().getColor(R.color.black));

        // TODO: if user selects same answer in consecutive questions, radio button is unclickable
        // reset all radio buttons
        /*
        if (buttonA.isSelected()) buttonA.toggle();
        else if (buttonB.isChecked()) buttonB.toggle();
        else if (buttonC.isChecked()) buttonC.toggle();
        else if (buttonD.isChecked()) buttonD.toggle();
        */

        radioAnswersGroup.clearCheck();

        buttonA.setChecked(false);
        buttonB.setChecked(false);
        buttonC.setChecked(false);
        buttonD.setChecked(false);

        buttonA.setClickable(true);
        buttonB.setClickable(true);
        buttonC.setClickable(true);
        buttonD.setClickable(true);

        submitButton.setText("Submit Answer");
    }

    private void submitAnswer() {
        // if nothing selected, update toggle and return
        if (!isRadioButtonSelected()) {
            submitted = !submitted;
            return;
        }

        radioAnswersGroup = (RadioGroup) findViewById(R.id.answersGroup);
        int selectedId = radioAnswersGroup.getCheckedRadioButtonId();
        chosen = (RadioButton) findViewById(selectedId);
        checkAnswerFunction(question); // check answer when button is clicked

        // set answers to red if wrong, green if correct (lazy coding... but who cares)
        buttonA.setTextColor(getResources().getColor(R.color.wrong));
        buttonB.setTextColor(getResources().getColor(R.color.wrong));
        buttonC.setTextColor(getResources().getColor(R.color.wrong));
        buttonD.setTextColor(getResources().getColor(R.color.wrong));

        if (question.checkAnswer(buttonA.getText().toString())) {
            buttonA.setTextColor(getResources().getColor(R.color.correct));
        }
        else if (question.checkAnswer(buttonB.getText().toString())) {
            buttonB.setTextColor(getResources().getColor(R.color.correct));
        }
        else if (question.checkAnswer(buttonC.getText().toString())) {
            buttonC.setTextColor(getResources().getColor(R.color.correct));
        }
        else if (question.checkAnswer(buttonD.getText().toString())) {
            buttonD.setTextColor(getResources().getColor(R.color.correct));
        }
    }

    private void checkAnswerFunction(Question q) {
        ++numOfQuestions;
        if (q.checkAnswer(chosen.getText().toString())) {
            submitButton.setText("Correct! Next Question!");
            ++numOfCorrect;
        } else {
            submitButton.setText("Wrong! Next Question!");
        }
        updateScoreView();

        // disallow changing of answer after submitted
        buttonA.setClickable(false);
        buttonB.setClickable(false);
        buttonC.setClickable(false);
        buttonD.setClickable(false);
    }

    private void updateScoreView() {
        scoreView.setText("Score:\n" + numOfCorrect + "/" + numOfQuestions);
    }

    private boolean isRadioButtonSelected() {
        return buttonA.isChecked() || buttonB.isChecked() || buttonC.isChecked() || buttonD.isChecked();
    }

    private void pauseGame() {
        if (isPaused) return;

        isPaused = true;
        // cancel the current timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(TakeQuiz.this).create();
        alertDialog.setTitle("Paused!");

        setAlertDialogMessage(alertDialog);

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Restart", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                isPaused = false;
                resetGame();
            }});

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Resume", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                isPaused = false;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                initTimer(currentTimeRemains);
            }});

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Main Menu", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                isPaused = false;
                finish(); // this will reset the score
            }
        });

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);  // restrict user from tapping outside of the dialog
    }

    private void resetGame() {
        // reset submitted toggle
        submitted = false;

        // reset score
        numOfCorrect = 0;
        numOfQuestions = 0;

        // reset game
        currentTimeRemains = TIME;
        initTimer(TIME);
        updateScoreView();
        showQuestion();
    }

    private void setAlertDialogMessage(AlertDialog alertDialog) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if (numOfQuestions == 0) {
            alertDialog.setMessage("0/0\nAverage time per question: 0 secs");
        }
        else {
            alertDialog.setMessage(numOfCorrect + " / " + numOfQuestions + " (" +
                    df.format(((float) numOfCorrect / numOfQuestions) * 100) + "%)" + "\n" +
                    "Average time per question: " + df.format((float) (TIME-currentTimeRemains) / numOfQuestions) + " secs");
        }
    }
}
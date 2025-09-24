package com.example.quizapp_3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private int sec;
    private int score;
    private boolean counting;
    private boolean addBonus;

    private String[][] qna={
            {"Ποια ειναι η Πρωτευσουσα της Γαλλια;","Παρίσι","Λιόν","Γκρενομπολ","Νίκαια"},
            {"Ποια ΔΕΝ ειναι Γαλλικη Ομαδα Ποδοσφαιρου;","Παρι σεν ζερμεν","Τουλουζ","Μπαρτσελονα","Μονακό"},
            {"Ποιος ειναι ο προεδρος της Γαλλιας;","Σαρκοζι","Ρουγιαλ","Λαγκάρντ","Μακρόν"}
    };

    private int [] CorrectAnswers={1,3,4};
    private int CurrentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(savedInstanceState!=null){
            sec = savedInstanceState.getInt("sec");
            score = savedInstanceState.getInt("score");
            counting = savedInstanceState.getBoolean("counting");
            addBonus = savedInstanceState.getBoolean("addBonus");
            CurrentQuestion = savedInstanceState.getInt("currentQuestion");
        }else{
            sec=0;
            score=0;
            CurrentQuestion=0;
            counting=true;
            addBonus=true;
        }
        TextView scoreView = findViewById(R.id.textViewScoreValue);
        scoreView.setText(""+ score);
        nextQuestion();
        showTime();
    }
    private void nextQuestion(){
        if(CurrentQuestion<qna.length){
            TextView question = findViewById(R.id.TextViewQuestionText);
            question.setText(qna[CurrentQuestion][0]);
            RadioButton rb1=findViewById(R.id.radioButton1);
            rb1.setText(qna[CurrentQuestion][1]);
            RadioButton rb2=findViewById(R.id.radioButton2);
            rb2.setText(qna[CurrentQuestion][2]);
            RadioButton rb3=findViewById(R.id.radioButton3);
            rb3.setText(qna[CurrentQuestion][3]);
            RadioButton rb4=findViewById(R.id.radioButton4);
            rb4.setText(qna[CurrentQuestion][4]);

        }
    }
    private void showTime(){
        final TextView timerView= findViewById(R.id.TextViewTimeRemaining);
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(sec<=120 && counting){
                    int minutes= sec/60;
                    int seconds= sec % 60;
                    String time=String.format("%02d:%02d",minutes,seconds);
                    timerView.setText(time);
                    sec++;
                    handler.postDelayed(this,1000);
                }else{
                    String noBonusMessage= getString(R.string.noBonus);
                    timerView.setText(noBonusMessage);
                       addBonus=false;
                }
            }
        });
    }
    public int getSelectedAnswers(){
        RadioGroup radioButtonGroup=findViewById(R.id.radioGroup);
        int radioButtonID=radioButtonGroup.getCheckedRadioButtonId();
        View radioButton= radioButtonGroup.findViewById(radioButtonID);
        int idx=radioButtonGroup.indexOfChild(radioButton);
        return idx+1;
    }

    public void onClickAnswer(View view) {
        int correctanswer=CorrectAnswers[CurrentQuestion];
        int selectedAnswer= getSelectedAnswers();

        if(selectedAnswer==correctanswer){
            if(addBonus){
                score+=10;
            }else{
                score+=5;
            }
        }

        TextView scoreText=findViewById(R.id.textViewScoreValue);
        scoreText.setText(""+score);
        CurrentQuestion++;

       if(CurrentQuestion==3){
            counting=false;
            Intent intent= new Intent(this,EndGameActivity.class);
            intent.putExtra("score",score);
            startActivity(intent);

        }else {
           nextQuestion();
           sec = 0;
           counting = true;
           addBonus = true;
           showTime();
       }
    }
}
package com.cambrian.jav1001Lab4Lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Team firstTeam = new Team(0, "Team A", 0);
    Team secondTeam = new Team(1,"Team B", 0);
    Team selectedTeam = firstTeam;
    int currentPoints = 2;
    int maximumPoints = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addActions();
    }

    void addActions() {
        plusAction();
        minusAction();
        switchAction();
        teamNameListener();
    }

    void teamNameListener() {
        final TextView firstTeamName = (TextView) findViewById(R.id.teamNameA);
        final TextView secondTeamName = (TextView) findViewById(R.id.teamNameB);
        firstTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstTeam.name = editable.toString();
                showTeamNameToast(firstTeamName);
            }
        });

        secondTeamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                secondTeam.name = editable.toString();
                showTeamNameToast(secondTeamName);
            }
        });
    }

    void plusAction() {
        ImageButton plusButton = (ImageButton) findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTeam.score += getCurrentRadioPoint();
                updateTeamPoints();
            }
        });
    }

    void minusAction() {
        ImageButton minusButton = (ImageButton) findViewById(R.id.minusButton);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int score = selectedTeam.score - getCurrentRadioPoint();
                if (score >= 0) {
                    selectedTeam.score -= getCurrentRadioPoint();
                    updateTeamPoints();
                }
            }
        });
    }

    void switchAction() {
        Switch switchTeam = (Switch) findViewById(R.id.teamSwitch);
        switchTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    selectedTeam = secondTeam;
                } else {
                    selectedTeam = firstTeam;
                }
                Log.v("Radio", selectedTeam.name);
            }
        });
    }

    int getCurrentRadioPoint() {
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        switch (group.getCheckedRadioButtonId()) {
            case R.id.twoPoint:
                return 2;
            case R.id.fourPoint:
                return 4;
            case R.id.sixPoint:
                return 6;
            case R.id.eightPoint:
                return 8;
        }
        return 0;
    }

    void radioButtonAction() {
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case 2:
                        currentPoints = 2;
                        break;
                    case 4:
                        currentPoints = 4;
                        break;
                    case 6:
                        currentPoints = 6;
                        break;
                    case 8:
                        currentPoints = 8;
                        break;
                }
            }
        });
    }

    void updateTeamPoints() {
        TextView teamView = (TextView) findViewById(selectedTeam.id == 0 ? R.id.teamScoreA : R.id.teamScoreB);
        teamView.setText(""+selectedTeam.score);
        if (firstTeam.score >= maximumPoints || secondTeam.score >= maximumPoints) {
            showSnackBar("Hurray!!!", firstTeam.score > secondTeam.score ? firstTeam.name : secondTeam.name, true);
        }
    }

    void showSnackBar(String title, String msg, boolean showReset) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg + " is the winner.");
        if (showReset) {
            builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    reset();
                }
            });
        }
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    void reset() {
        currentPoints = 2;
        firstTeam.score = 0;
        secondTeam.score = 0;

        TextView firstTeamScoreView = (TextView) findViewById(R.id.teamScoreA);
        TextView secondTeamScoreView = (TextView) findViewById(R.id.teamScoreB);
        firstTeamScoreView.setText("0");
        secondTeamScoreView.setText("0");
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.check(R.id.twoPoint);
    }

    private void showTeamNameToast(TextView teamNameTextView) {
        if (firstTeam.name.equals(secondTeam.name)) {
            hideKeyboard(teamNameTextView);
            Toast.makeText(MainActivity.this, "Team name can not be same.", Toast.LENGTH_SHORT).show();
            teamNameTextView.setText(teamNameTextView.getId() == R.id.teamNameA ? "Team A" : "Team B");
        }
    }

    private void hideKeyboard(View view) {
        view.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
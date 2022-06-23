package com.cambrian.jav1001Lab4Lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    // Create two teams Team A and Team B by default
    // Set default scores to 0
    // Random team unique ids are 0 and 1
    Team firstTeam = new Team(0, "Team A", 0);
    Team secondTeam = new Team(1,"Team B", 0);

    // Default selected team is Team A
    Team selectedTeam = firstTeam;

    // Current points: First option of Radio button, Or Default points for increment
    int currentPoints = 2;
    // Maximum points: To track which team is the winner when anyone hits this score
    int maximumPoints = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Add listeners to the text views, radio buttons, buttons and switch
        addActions();
        toggleTeamHighlight();
    }

    // Add listeners to the text views, radio buttons, buttons and switch
    void addActions() {
        plusAction();
        minusAction();
        switchAction();
        teamNameListener();
    }

    // Updates edit text outlined color according to switch
    void toggleTeamHighlight() {
        Switch teamSwitch = (Switch) findViewById(R.id.teamSwitch);
        EditText firstTeamNameText = (EditText) findViewById(R.id.teamNameA);
        firstTeamNameText.setBackgroundTintList(ColorStateList.valueOf(teamSwitch.isChecked() ? Color.TRANSPARENT : Color.RED));
        EditText secondTeamNameText = (EditText) findViewById(R.id.teamNameB);
        secondTeamNameText.setBackgroundTintList(ColorStateList.valueOf(teamSwitch.isChecked() ? Color.RED : Color.TRANSPARENT));
    }

    // This methods adds listeners to Team name Edit text
    // Checks whether team name is same or not
    // Set team name to team class object if name not same
    void teamNameListener() {
        final EditText firstTeamName = (EditText) findViewById(R.id.teamNameA);
        final EditText secondTeamName = (EditText) findViewById(R.id.teamNameB);
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
                // Shows toast if team name is same
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
                // Shows toast if team name is same
                showTeamNameToast(secondTeamName);
            }
        });
    }

    // Adds action to plus button
    // Increment scores according to current point selection and team selection
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

    // Adds action to minus button
    // Decrement scores according to current point selection and team selection
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

    // Adds listener to radio button
    // Switches team according to radio button on/off
    // Off : shows Team A
    // On: shows Team B
    void switchAction() {
        Switch switchTeam = (Switch) findViewById(R.id.teamSwitch);
        switchTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                selectedTeam = isChecked ? secondTeam : firstTeam;
                toggleTeamHighlight();
            }
        });
    }

    // This method returns current point according to radio button selection
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

    // Adds listener to group
    // Set current points according to radio button selection
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

    // This method updates team points
    // checks which team's score needs to be updated
    // Shows snack bar if either team crosses desired points
    void updateTeamPoints() {
        TextView teamView = (TextView) findViewById(selectedTeam.id == 0 ? R.id.teamScoreA : R.id.teamScoreB);
        teamView.setText(""+selectedTeam.score);
        if (firstTeam.score >= maximumPoints || secondTeam.score >= maximumPoints) {
            showSnackBar("Hurray!!!", firstTeam.score > secondTeam.score ? firstTeam.name : secondTeam.name, true);
        }
    }

    // Shows message to user whichever team is winner
    // title: To show title
    // msg: To show description
    // showReset: Whether to show reset button or not
    void showSnackBar(String title, String msg, boolean showReset) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg + " is the winner.");
        if (showReset) {
            builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                // On click rest button
                // Clears everything
                public void onClick(DialogInterface dialog, int id) {
                    reset();
                }
            });
        }
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    // Reset everything except team name
    // Reset current points, team scores, radio group selection
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

    // Show toast if team name is same
    // Reset team name to original one if team name entered same
    // Hide keyboard automatically
    private void showTeamNameToast(EditText teamNameTextView) {
        if (firstTeam.name.equals(secondTeam.name)) {
            hideKeyboard(teamNameTextView);
            Toast.makeText(MainActivity.this, "Team name can not be same.", Toast.LENGTH_SHORT).show();
            teamNameTextView.setText(teamNameTextView.getId() == R.id.teamNameA ? "Team A" : "Team B");
        }
    }

    // Function to remove current focus from the selected view
    // Hides keyboard
    private void hideKeyboard(View view) {
        view.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
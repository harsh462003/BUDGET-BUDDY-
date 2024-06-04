package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText nameEditText, newUsernameEditText, newPasswordEditText;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameEditText = findViewById(R.id.nameEditText);
        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        Button submitButton = findViewById(R.id.submitButton);
        Button return1=findViewById(R.id.returnbutton);
        return1.setOnClickListener(v->{

            Intent i=new Intent(CreateAccountActivity.this, MainActivity.class);
            startActivity(i);
        });

        dbHandler = new DBHandler(CreateAccountActivity.this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String newUsername = newUsernameEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                if (!name.isEmpty() && !newUsername.isEmpty() && !newPassword.isEmpty()) {
                    if (dbHandler.doesUsernameExist(newUsername)) {
                        Toast.makeText(CreateAccountActivity.this, "Username is taken", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHandler.addNewUser(name, newUsername, newPassword);
                        Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package br.pucpr.appdev.rentalboardgames.controller;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.pucpr.appdev.rentalboardgames.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void btnConfirmSignInOnClick(View v) {
        EditText txtEmail = findViewById(R.id.txtEmailSignIn);
        EditText txtPassword = findViewById(R.id.txtPasswordSignIn);
        EditText txtPasswordConfirmation = findViewById(R.id.txtPasswordConfirmationSignIn);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        LinearLayout buttons = findViewById(R.id.linearLayoutButtons);

        progressBar.setVisibility(View.VISIBLE);
        buttons.setVisibility(View.GONE);

        if (txtPassword.getText().toString().equals(txtPasswordConfirmation.getText().toString())) {
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("SIGNIN-SUCCESS", "Sucesso ao criar o usuário");
                        Toast.makeText(SignUpActivity.this, "Usuário criado com sucesso!", Toast.LENGTH_LONG).show();
                        auth.signOut();
                        SignUpActivity.this.setResult(RESULT_OK);
                        SignUpActivity.this.finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Falhou", Toast.LENGTH_LONG).show();
                        Log.d("SIGNIN-ERROR", "onComplete: " + task.getException());
                    }
                }
            });
        } else {
            Log.d("SIGNIN-ERROR", "Senha e Confirmação de Senha são diferentes");
            Toast.makeText(this, "A confirmação de senha não é igual a senha", Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.GONE);
            buttons.setVisibility(View.VISIBLE);
        }
    }

    public void btnCancelSignInOnClick(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

}

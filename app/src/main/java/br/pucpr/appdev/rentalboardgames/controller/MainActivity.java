package br.pucpr.appdev.rentalboardgames.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.pucpr.appdev.rentalboardgames.CustomApplication;
import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.User;

public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_ACTIVITY = 1;

    private static final String TAG = "BOARD-LOGIN";

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbUser = fbAuth.getCurrentUser();

        if (fbUser != null) {
            Log.d(TAG, "onStart: Usuário já logado");
            findUser();
        }
    }

    private void goToListRent() {
        Intent i = new Intent(this, ListBoardgameToRentActivity.class);
        startActivity(i);
        finish();
    }

    public void btnSignInOnClick(View v) {
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPassword = findViewById(R.id.txtPassword);

        // TODO: Colocar uma tela de carregando quando clicar em entrar

        fbAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOGIN-SUCCESS", "User logged");
                            findUser();
                        } else {
                            Log.d("LOGIN-ERROR", "Wrong information");
                            Toast.makeText(MainActivity.this, "Falhou", Toast.LENGTH_LONG).show();
                            Log.d("LOGIN-ERROR", "onComplete: " + task.getException());
                        }
                    }
                });
    }

    public void btnSignUpOnClick(View v) {
        Intent intentSignUp = new Intent(this, SignUpActivity.class);
        startActivityForResult(intentSignUp, SIGN_IN_ACTIVITY);
    }

    public void btnSignInGoogleOnClick(View v) {

    }

    private void findUser() {
        FirebaseFirestore.getInstance().collection("users")
                .document(fbUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            user.setId(fbUser.getUid());
                            ((CustomApplication) MainActivity.this.getApplicationContext()).setUser(user);
                            goToListRent();
                        } else {
                            fbAuth.signOut();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_ACTIVITY && resultCode == RESULT_OK) {
            Toast.makeText(this, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();
        }
    }
}

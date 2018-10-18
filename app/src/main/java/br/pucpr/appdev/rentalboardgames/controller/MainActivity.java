package br.pucpr.appdev.rentalboardgames.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.pucpr.appdev.rentalboardgames.CustomApplication;
import br.pucpr.appdev.rentalboardgames.ListBoardgames;
import br.pucpr.appdev.rentalboardgames.R;
import br.pucpr.appdev.rentalboardgames.model.Lending;
import br.pucpr.appdev.rentalboardgames.model.User;

public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_ACTIVITY = 1;

    private static final String TAG = "BOARD-LOGIN";

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private boolean isGoogleSignIn;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbAuth = FirebaseAuth.getInstance();
        isGoogleSignIn = false;
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
        Intent i = new Intent(this, ListBoardgames.class);
        startActivity(i);
        finish();
    }

    public void btnSignInOnClick(View v) {
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPassword = findViewById(R.id.txtPassword);
        isGoogleSignIn = false;

        // TODO: Colocar uma tela de carregando quando clicar em entrar

        fbAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOGIN-SUCCESS", "User logged");
                            fbUser = fbAuth.getCurrentUser();
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
        GoogleSignInOptions gso  = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        final GoogleSignInClient signInClient = GoogleSignIn.getClient(this, gso);
        signInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                isGoogleSignIn = true;
                Intent signInIntent = signInClient.getSignInIntent();
                startActivityForResult(signInIntent, 9001);
            }
        });
    }

    private void findUser() {
        FirebaseFirestore.getInstance().collection("users")
                .document(fbUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            if (user == null && isGoogleSignIn) {
                                List<Lending> lendingList = new ArrayList<>(2);
                                lendingList.add(null);
                                lendingList.add(null);

                                user = new User();
                                user.setId(fbUser.getUid());
                                user.setName(account.getDisplayName());
                                user.setEmail(account.getEmail());
                                user.setLendings(lendingList);

                                List<Map<String, Object>> listLendingsDB = new ArrayList<>();

                                Map<String, Object> newUserDB = new HashMap<>();
                                newUserDB.put("name", user.getName());
                                newUserDB.put("email", user.getEmail());
                                for (Lending l : lendingList) {
                                    Map<String, Object> newUserLendingsDB = new HashMap<>();
                                    newUserLendingsDB.put("boardgame", null);
                                    newUserLendingsDB.put("startDate", null);
                                    newUserLendingsDB.put("endDate", null);
                                    newUserLendingsDB.put("totalRentValue", null);
                                    listLendingsDB.add(newUserLendingsDB);
                                }
                                newUserDB.put("lendings", listLendingsDB);
                                FirebaseFirestore.getInstance().collection("users").document(fbUser.getUid()).set(newUserDB);
                            } else {
                                user.setId(fbUser.getUid());
                            }

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
        } else if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);

                AuthCredential cred = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                fbAuth.signInWithCredential(cred).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            fbUser = fbAuth.getCurrentUser();
                            findUser();
                        } else {
                            Log.d(TAG, "onComplete: ERROR", task.getException());
                        }
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}

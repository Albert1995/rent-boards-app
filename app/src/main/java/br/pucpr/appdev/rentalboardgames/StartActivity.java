package br.pucpr.appdev.rentalboardgames;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

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
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import br.pucpr.appdev.rentalboardgames.model.User;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "BOARD-START";
    private static final int GOOGLE_LOGIN = 9001;

    // Application Context
    private CustomApplication appContext;

    // Firebase
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private FirebaseFirestore db;

    // UI Elements
    private Button btnSignIn, btnSignInGoogle;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Get Application Context
        appContext = (CustomApplication) StartActivity.this.getApplicationContext();

        // Initialize Firebase
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Initialize UI Elements
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignInGoogle = findViewById(R.id.btnSignInGoogle);
        progressBar = findViewById(R.id.progressBar);

        // Set settings in db
        FirebaseFirestoreSettings dbSettings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(dbSettings);

        // Prepare the Button Listeners
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)) // TODO: Trocar o request id token
                        .requestEmail()
                        .build();
                final GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(StartActivity.this, gso);


                googleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, GOOGLE_LOGIN);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserLogged();
    }

    /**
     * Valida se o usuário já estava logado anteriormente
     */
    private void checkUserLogged() {
        if (fbUser != null) {
            // Buscar o usuário no banco de dados
            progressBar.setVisibility(View.VISIBLE);
            getUserInDB();
        } else {
            // Show buttons
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignInGoogle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Retorno da seleção da conta Google
        if (requestCode == GOOGLE_LOGIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class); // Conta Google
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null); // Credencial para acesso com a conta
                fbAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // Conta logada com sucesso
                            fbUser = fbAuth.getCurrentUser();
                            if (fbUser != null) {
                                getUserInDB();
                            }
                        } else { // Erro ao conectar com a conta
                            Log.d(TAG, "onComplete: erro ao conectar o usuario via conta do google", task.getException());
                        }
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Busca as informações do usuário no banco de dados.
     */
    private void getUserInDB() {
        db.collection("users").document(fbUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentUser = task.getResult();
                    User user = new User(documentUser);
                    appContext.setUser(user);
                    sendToUserArea();
                } else {
                    Log.d(TAG, "onComplete: Usuário sem registro na base de dados ou problemas ao encontrar o usuário", task.getException());
                }
            }
        });
    }

    /**
     * Envia o usuário logado para a área restrita
     */
    private void sendToUserArea() {
        Intent i = new Intent(StartActivity.this, ListBoardgames.class);
        startActivity(i);
        finish();
    }
}

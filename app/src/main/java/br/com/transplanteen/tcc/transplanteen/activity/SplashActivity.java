package br.com.transplanteen.tcc.transplanteen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.transplanteen.tcc.transplanteen.R;
import br.com.transplanteen.tcc.transplanteen.helper.ConfiguracaoFirebase;
import br.com.transplanteen.tcc.transplanteen.model.Enfermeiro;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (usuarioLogado()) {
            abrirHome();
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    abrirAutenticacao();
                }
            }, 3000);

        }
    }

    public boolean usuarioLogado() {
        FirebaseUser usuario = autenticacao.getCurrentUser();
        if (usuario != null) {
            return true;
        }

        return false;
    }

    private void abrirAutenticacao() {
        Intent i = new Intent(SplashActivity.this, AutenticacaoActivity.class);
        startActivity(i);
        finish();
    }

    public void abrirHome() {
        Intent i = new Intent(SplashActivity.this, EnfermeiroActivity.class);
        startActivity(i);
        finish();
    }

}

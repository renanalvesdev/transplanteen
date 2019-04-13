package br.com.transplanteen.tcc.transplanteen.activity;

import android.support.v7.app.AppCompatActivity;
import br.com.transplanteen.tcc.transplanteen.R;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class ConsultaActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Renan da Silva Alves");
        toolbar.setTitle("Consulta");
        toolbar.setNavigationIcon(R.drawable.ic_close_24dp);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_consulta, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

package br.com.transplanteen.tcc.transplanteen.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.View;

import br.com.transplanteen.tcc.transplanteen.model.IngestaoLiquido;
import dmax.dialog.SpotsDialog;

public class DialogUtil {


    public static boolean dialogConfirmacao(Context context, String texto) {

        final boolean[] retorno = {false};

        new AlertDialog.Builder(context)
                .setMessage(texto)
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        retorno[0] = true;
                    }
                })
                .setNegativeButton("Não", null)
                .show();

        return retorno[0];
    }

    public static AlertDialog dialogProcessando(Context context) {

        AlertDialog processando = new SpotsDialog
                .Builder()
                .setContext(context)
                .setMessage("Carregando")
                .setCancelable(false).build();

        return processando;
    }

}

package br.com.transplanteen.tcc.transplanteen.util;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import br.com.transplanteen.tcc.transplanteen.activity.AutenticacaoActivity;
import dmax.dialog.SpotsDialog;

public class MessagesUtil {

    public static void toastInfo(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog dialogProcessando(Context context, String mensagem) {
        android.app.AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(mensagem)
                .setCancelable(false).build();
        return dialog;
    }


}

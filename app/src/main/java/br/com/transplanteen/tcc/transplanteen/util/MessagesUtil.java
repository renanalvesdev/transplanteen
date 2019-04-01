package br.com.transplanteen.tcc.transplanteen.util;

import android.content.Context;
import android.widget.Toast;

public class MessagesUtil {

    public static void toastInfo(Context context, String mensagem){
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

}

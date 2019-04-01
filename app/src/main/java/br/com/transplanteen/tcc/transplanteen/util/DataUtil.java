package br.com.transplanteen.tcc.transplanteen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DataUtil {

    public static String stringParaTimestamp(String dataString){
        String retorno = null;
        try {

            Date dataAux = new SimpleDateFormat("dd/MM/yyyy").parse(dataString);
            retorno =  String.valueOf(dataAux.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  retorno;
    }

    public static String  calculaIdade(String timestamp){

        long retorno = 0;
        long msDiff = Calendar.getInstance().getTimeInMillis() - Long.valueOf(timestamp);
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

        retorno = daysDiff/365;

        return String.valueOf(retorno);

    }


}

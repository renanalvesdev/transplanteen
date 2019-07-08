package br.com.transplanteen.tcc.transplanteen.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DataUtil {

    static final java.util.Locale localeBr = new java.util.Locale( "tr", "TR" );
    static final DateTimeZone timeZoneBr = DateTimeZone.forID( "Europe/Istanbul" );  // Assuming this time zone (not specified in Question).
    static final DateTimeFormatter formatterBr = DateTimeFormat.forPattern( "dd.MMM.yyyy" ).withLocale( localeBr ).withZone( timeZoneBr );


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

    public static String timestampParaData(String timestamp){
        long dataTimestamp = Long.parseLong(timestamp);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dataTimestamp);
        return format.format(calendar.getTime());

    }

    public static String  calculaIdade(String dataNascimento){

        long retorno = 0;
        long msDiff = Calendar.getInstance().getTimeInMillis() - Long.valueOf(dataNascimento);
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

        retorno = daysDiff/365;

        return String.valueOf(retorno);

    }

    public static String somenteHoras(long dataTimestamp){

        Date date = new Date(dataTimestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Fortaleza"));
        String formattedDate = sdf.format(date);

        return formattedDate;
    }


    public static long comecoDia(DateTime data){
        return data.withTimeAtStartOfDay().getMillis();
    }


    public static long finalDia(DateTime data){
        return data.plusDays(1).withTimeAtStartOfDay().getMillis();
    }


    public static String dataAtual(){
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(now);
    }

    public static String hojeTimestamp(){
        DateTime dataAtualFortaleza = DateTime.now( DateTimeZone.forID( "America/Fortaleza" ));
        return String.valueOf(comecoDia(dataAtualFortaleza));
    }

    public static String textoIntervalo (DateTime dataInicial, DateTime dataFinal){
        DateTime dataInicioBrasil = dataInicial.withZone(DateTimeZone.forID( "America/Fortaleza" ));
        DateTime dataFinalBrasil = dataFinal.withZone(DateTimeZone.forID( "America/Fortaleza" ));

        String retorno =  formatoSimples(dataInicioBrasil)+ " ~ " + formatoSimples(dataFinalBrasil);
        return retorno;
    }

    public static String formatoSimples(DateTime date){

        String dataFormatada = date.toString("dd/MM/yyyy");
        String dia = date.dayOfWeek().getAsShortText();
        return dia + ", " + dataFormatada;
    }

    public static String diaSemana(String timestamp){
        long millis = Long.valueOf(timestamp);
        DateTime data = new DateTime(millis);
        return data.dayOfWeek().getAsShortText().toUpperCase();
    }

    public static String dataAgora(){
        return String.valueOf(DateTime.now().getMillis());
    }

}

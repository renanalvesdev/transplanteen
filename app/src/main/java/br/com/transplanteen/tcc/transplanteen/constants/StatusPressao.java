

package br.com.transplanteen.tcc.transplanteen.constants;
import android.support.annotation.IntDef;

        import java.lang.annotation.Retention;
        import java.lang.annotation.RetentionPolicy;

public class StatusPressao{

    public static final int NORMAL= 0;
    public static final int ELEVADA= 1;
    public static final int HIPERTENSAO_ESTAGIO_1 = 2;
    public static final int HIPERTENSAO_ESTAGIO_2 = 3;
    public static final int CRISE_HIPERTENSIVA = 4;

    public StatusPressao(@Status int status) {
        System.out.println("Status :" + status);
    }

    @IntDef({NORMAL, ELEVADA, HIPERTENSAO_ESTAGIO_1, HIPERTENSAO_ESTAGIO_2, CRISE_HIPERTENSIVA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

}
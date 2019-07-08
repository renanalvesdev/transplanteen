package br.com.transplanteen.tcc.transplanteen.fragment;

import br.com.transplanteen.tcc.transplanteen.notifications.MyResponse;
import br.com.transplanteen.tcc.transplanteen.notifications.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1d-YW64:APA91bHA08-xuWM_yHQJAC58urTe2BCF0dla0fDJ2cXP9KXl17CXDA7FraQIvhTcRhsW-yGo73lZ8E3Tib2omZ-ln9zOlm3hueK900ZsyM0XnErPd1jx0G14NPUxqzsTthvkL3cewwDx"

            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}

package br.com.smartzu.acolherapp.api;

import br.com.smartzu.acolherapp.model.NotificacaoDados;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificacaoService {

    @Headers({
            "Authorization:key=AAAA81vE2SY:APA91bGvmusG0c52w6-rVMlbu-EW5oT-reGa492qm0LX6Ah1_YrQ0ngsznFN_3X3KrxZPWR25cW1aVmRY3UHvaUxu3Z9faCpkGp0k-iJhaQRGM6Hju5bgoh-Z-4ZMGfdNu-HPfT5CtaX",
            "Content-Type:application/json"
    })

    @POST("send")
    Call<NotificacaoDados> salvarNotificacao(@Body NotificacaoDados notificacaoDados);
}

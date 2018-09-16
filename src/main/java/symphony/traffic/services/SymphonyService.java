package symphony.traffic.services;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.Client;
import io.micronaut.http.client.RxHttpClient;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

@Singleton
public class SymphonyService {

    private static final Logger LOG = LoggerFactory.getLogger(HereService.class);

    @Client("https://corporate.symphony.com/integration/v1/whi/simpleWebHookIntegration/") @Inject
    RxHttpClient httpClient;

    private static final String MESSAGEML_TEMPLATE = "<messageML>\n" +
            "  Going from %s to %s will now take you <b>%s</b>\n" +
            "</messageML>";

    public void sendTrafficMessage(long durationMinutes, String symphonyWebHookId){
        String messageML = String.format(MESSAGEML_TEMPLATE, "Sophia-Antipolis", "Nice", Duration.ofSeconds(durationMinutes));
        Flowable<HttpResponse<String>> call = httpClient.exchange(
                HttpRequest.POST(symphonyWebHookId, messageML), String.class
        );
        HttpResponse<String> response = call.blockingFirst();
        LOG.info("Symphony webhook returned {}", response.code());

    }

}

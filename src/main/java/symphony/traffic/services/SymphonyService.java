package symphony.traffic.services;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.Client;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.multipart.MultipartBody;
import io.reactivex.Flowable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;

@Singleton
public class SymphonyService {

    private static final Logger LOG = LoggerFactory.getLogger(HereService.class);

    @Client("https://corporate.symphony.com/integration/v1/whi/simpleWebHookIntegration/") @Inject
    RxHttpClient httpClient;

    private String messageTemplate ;

    private static final String MESSAGEML_TEMPLATE = "<messageML>\n" +
            "  Going from %s to %s will now take you <b>%s minutes</b>\n" +
            "</messageML>";

    public void sendTrafficMessage(long durationSeconds, String symphonyWebHookId) throws IOException {
        //String messageML = String.format(MESSAGEML_TEMPLATE, "Sophia-Antipolis", "Nice", Long.toString(durationSeconds / 60));
        String template = getMessageTemplate();
        long durationMinutes = durationSeconds / 60;
        String color = "green";
        if(durationMinutes>60){
            color = "red";
        }else if(durationMinutes>45){
            color = "orange";
        }
        String messageML = String.format(template, "Sophia-Antipolis", "Nice", color, Long.toString(durationMinutes));
        LOG.info("messageML = {}", messageML);


        MultipartBody requestBody = MultipartBody.builder()
                .addPart(
                        "message",
                        messageML
                ).build();

        MutableHttpRequest<MultipartBody> multipartBodyMutableHttpRequest = HttpRequest.POST(symphonyWebHookId, requestBody)
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE);

        Flowable<HttpResponse<String>> call = httpClient.exchange(
                multipartBodyMutableHttpRequest, String.class
        );
        HttpResponse<String> response = call.blockingFirst();
        LOG.info("Symphony webhook returned {}", response.code());

    }

    private String getMessageTemplate() throws IOException {
        if(messageTemplate == null || messageTemplate.trim().length()==0){
            InputStream stream = SymphonyService.class.getResourceAsStream("/message.xml");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                IOUtils.copy(stream, bos);
            return new String(bos.toByteArray(), "UTF-8");
            } finally {
                IOUtils.closeQuietly(stream);
                IOUtils.closeQuietly(bos);
            }
        }
        return messageTemplate ;
    }

}

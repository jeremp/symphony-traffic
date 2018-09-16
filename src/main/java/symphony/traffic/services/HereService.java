package symphony.traffic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.client.Client;
import io.micronaut.http.client.RxHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symphony.traffic.services.here.dto.HereResponseDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class HereService {

    private static final Logger LOG = LoggerFactory.getLogger(HereService.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${myapp.webhook.id:notSet}")
    private String webHookID;

    @Value("${myapp.here.app.code:notSet}")
    private String hereAppCode;

    @Value("${myapp.here.app.id:notSet}")
    private String hereAppID;

    @Client("https://route.api.here.com/routing/7.2") @Inject
    RxHttpClient httpClient;

    private final SymphonyService symphonyService;

    public HereService(SymphonyService symphonyService) {
        this.symphonyService = symphonyService;
    }

    public void checkTraffic() throws IOException {
        if(checkProperties()==false){
            System.exit(0);
            return ;
        }
        long duration = retrieveData();
        symphonyService.sendTrafficMessage(duration, webHookID);
    }

    private long retrieveData(){
        LOG.info("webHookID="+webHookID);
        String data = makeHereRestCall();
        LOG.info("Data = {}", data);
        HereResponseDto hereResponseDto ;
        try {
            hereResponseDto = OBJECT_MAPPER.readValue(data, HereResponseDto.class);
        } catch (IOException e) {
            LOG.error("Error parsing here response body", e);
            return 0;
        }
        long trafficTime = hereResponseDto.getResponse().getRoute().get(0).getSummary().getTrafficTime();
        LOG.info("traffic time = {}", trafficTime);
        return trafficTime;
    }

    private String makeHereRestCall(){
        String path = "/calculateroute.json?waypoint0=43.6168%2C7.0498&waypoint1=43.6994%2C7.2749&mode=fastest%3Bcar%3Btraffic%3Aenabled&app_id="+hereAppID+"&app_code="+hereAppCode+"&departure=now";

        return httpClient.toBlocking().retrieve(path);
    }

    private boolean checkProperties(){
        if("notSet".equalsIgnoreCase(webHookID)){
            LOG.error("property 'myapp.webhook.id' not set, program won't work");
            return false;
        }
        if("notSet".equalsIgnoreCase(hereAppCode)){
            LOG.error("property 'myapp.here.app.code' not set, program won't work");
            return false;
        }
        if("notSet".equalsIgnoreCase(hereAppID)){
            LOG.error("property 'myapp.here.app.id' not set, program won't work");
            return false;
        }
        return true ;
    }


}

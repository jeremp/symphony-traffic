package symphony.traffic.jobs;

import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symphony.traffic.services.HereService;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class TrafficJob {

    private static final Logger LOG = LoggerFactory.getLogger(TrafficJob.class);

    final HereService hereService;

    public TrafficJob(HereService hereService) {
        this.hereService = hereService;
    }

    @Scheduled(fixedDelay = "45s", initialDelay = "2s")
    void executeEveryFourtyFive() {
        try {
            LOG.info("Simple Job every 45 seconds :{}", new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()));
            hereService.checkTraffic();
        }catch (Exception ex){
            LOG.error("failure", ex);
        }
    }

}

package datastore.group_api.route;

import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.web.reactive.function.client.WebClient;

import datastore.group_api.database.GroupRepository;
import datastore.group_api.map.GroupURL;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import reactor.core.publisher.Mono;

@QuarkusMain
public class Server implements QuarkusApplication {

    @Inject
    GroupRepository groupRepo;
    
    @Inject
    GroupURL groups;

    @ConfigProperty(name = "URL")
    String URL;

    @ConfigProperty(name = "hubURL")
    String hubURL;

    /**
     * @param se
     * @throws MalformedURLException
     
    @Transactional
    public void init(@Observes StartupEvent se) throws MalformedURLException {
        groups.group = groupRepo.findAll().firstResult();
        if (groups.group != null) {
            groups.group.url = new URL("https://" + URL);
        }
    }

    @Override
    public int run(String... args) throws Exception {
        if (groups.group != null) {
            WebClient webClient = WebClient.create("https://" + hubURL);
            webClient.put()
                    .uri("/hub/" + groups.group.id)
                    .body(Mono.just(groups.group.url), URL.class)
                    .retrieve()
                    .bodyToMono(String.class).block();
        }
        Quarkus.waitForExit();
        return 0;
    }
}
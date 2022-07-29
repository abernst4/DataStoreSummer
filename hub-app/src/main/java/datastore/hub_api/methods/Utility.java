package datastore.hub_api.methods;

import java.net.URL;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import datastore.hub_api.entity.GroupURL;
import datastore.hub_api.repository.HubRepository;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import reactor.core.publisher.Mono;


@Singleton
public class Utility{
    @Inject
    HubRepository hubRepo; 

    private String live = "/q/health/live";

    @Scheduled(every = "35s")
    @Transactional
    public void checkURL(){ 
        for(long id: this.hubRepo.getGroupUrls().keySet()){
            if(!isConnected(id)){
                URL url = this.hubRepo.getGroupUrls().get(id);
                System.out.println("The server with url of: " + url + " has been terminated");
                deleteFromHub(id);
            }
        }
        this.serveURLs();
    }

    @Transactional
    private void deleteFromHub(long id){
        this.hubRepo.deleteById(id);
    }


    private boolean isConnected(long id){
        try{
            WebClient client = WebClient.create(this.hubRepo.getGroupUrls().get(id).toString());
            client.get().uri(live).retrieve().bodyToMono(String.class).block(); //in case we need logic
            return true; 
        }catch(WebClientRequestException e){
            return false; 
        }
    }


     /**
     * WebClient is an interface representing the main entry point for performing web requests.
     * is a non-blocking, reactive client for performing HTTP requests with Reactive Streams back pressure.
     * create - Create a new WebClient with Reactor Netty by default.
     * post - Start building an HTTP POST request
     * uri - Specify the URI starting with a URI template and finishing off with a UriBuilder created from the template.
     * body - We can call .body() with a Flux (including a Mono), which can stream content asynchronously to build the request body.
     * bodyHoldMono - The WebClient class uses reactive features, in the form of a Mono to hold the content of the message
     */
    public void serveURLs(){
        for (GroupURL groupURL : this.hubRepo.listAll()) {
            URL url = groupURL.url;
            try {

                WebClient client = WebClient.create(url.toString());
                client
                    .post()
                    .uri("/groups/updateMap")                
                    .body(Mono.just(hubRepo.getGroupUrls()), Map.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            } catch (WebClientResponseException e) {
                Log.info(e);
            } 
    }
}
}
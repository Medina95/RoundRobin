package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class LoadBalancerController {

    private int currentIndex = 0;
    private final List<String> logServiceUrls = Arrays.asList(
            "http://springdockercompose1:6000/api/log",
            "http://springdockercompose2:6000/api/log",
            "http://springdockercompose3:6000/api/log"
    );
    private String lastLogServiceUrl = ""; // Variable para almacenar el último servicio de log utilizado

    @GetMapping("/index")
    public String index() {
        return "index"; // nombre del archivo en src/main/resources/templates (sin la extensión .html)
    }

    @PostMapping
    public ResponseEntity<Object> forwardMessage(@RequestBody String messageContent) {
        // Limpia el contenido del mensaje eliminando los saltos de línea y retorno de carro
        String cleanedMessageContent = messageContent.replaceAll("[\\r\\n]+", " ").trim();

        // Selecciona la URL del servicio en base al índice actual
        String logServiceUrl = logServiceUrls.get(currentIndex);

        // Envía el mensaje al servicio de log
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.postForEntity(logServiceUrl, cleanedMessageContent, Object.class);

        // Actualiza el índice para el próximo servicio
        currentIndex = (currentIndex + 1) % logServiceUrls.size();
        lastLogServiceUrl = logServiceUrl;

        // Retorna la respuesta del servicio de log, incluyendo el servicio que atendió la solicitud
        return ResponseEntity.ok(Map.of(
                "response", response.getBody(),
                "handledBy", lastLogServiceUrl
        ));
    }



}

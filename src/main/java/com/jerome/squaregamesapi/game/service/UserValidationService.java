package com.jerome.squaregamesapi.game.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

// Créer une classe UserValidationService annotée @Service
@Service
public class UserValidationService {

    private final RestClient restClient;

    // Injecter l'URL du service utilisateurs via @Value("${users.service.url}")
    // Créer un bean RestClient dans une méthode ou via le constructeur

    public UserValidationService(@Value("${users.service.url}") String usersServiceUrl) {
        this.restClient = RestClient.builder().baseUrl(usersServiceUrl).build();
    }

    // Méthode validateUser(UUID userId) :
    public boolean validateUser(UUID uuid) {
    //   → appeler GET {url}/api/v1/users/{userId}/valid via RestClient
        String result = this.restClient.get()
                .uri("/{id}/valid", uuid)
    //   → si la réponse est false ou que l'utilisateur n'existe pas → lancer une ResponseStatusException 401
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED ,"Client Error" + res.getStatusCode());
                })
                .body(String.class);
    //   → sinon → ne rien faire (void)

        assert result != null;
        return result.equals("true");
    }
}

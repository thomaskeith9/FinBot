import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;

public class RobinhoodClient {
  private static final Logger LOG = LoggerFactory.getLogger(RobinhoodClient.class);
  private static final String BASE = "http://api.robinhood.com";
  private static final String AUTH = "/api-token-auth";

  private static final JsonNode AUTH_PAYLOAD = {
          'client_id': 'c82SH0WZOsabOXGP2sxqcj34FxkvfnWRZBKlBjFS',
          'expires_in': expiresIn,
          'grant_type': 'password',
          'password': password,
          'scope': scope,
          'username': username,
          'challenge_type': "sms",
          'device_token': device_token
};

  private HttpClient client;

  private String username;
  private String password;

  RobinhoodClient() {
    this(System.getProperty("robinhood.username"), System.getProperty("robinhood.pass"));
  }

  RobinhoodClient(String username, String password) {
    LOG.info("Initializing Robinhood client...");
    this.username = username;
    this.password = password;
    this.client = HttpClient.newHttpClient();
  }

  public String getToken() {
    HttpRequest request = HttpRequest
            .newBuilder()
            .timeout(Duration.ofSeconds(90))
            .uri(URI.create(BASE + AUTH))
            .setHeader("content-type", "application/json")
            .POST(getCreds())
            .build();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      LOG.info("Response: {}", response);
      return response.body();
    } catch (Exception e) {
      String err = String.format("Failed to get token for user %s", username);
      LOG.error(err, e);
      throw new RuntimeException(err);
    }
  }

  private HttpRequest.BodyPublisher getCreds() {
    return HttpRequest.BodyPublishers.ofString(String.format(
            "username=%s&password=%s",
            this.username,
            this.password
    ));
  }
}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobinhoodTicker {
  private static final Logger LOG = LoggerFactory.getLogger(RobinhoodTicker.class);

  public static void main(String[] args) {
    RobinhoodClient client = new RobinhoodClient();
    LOG.info("Got token: {}", client.getToken());
  }
}

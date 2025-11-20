package app.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class viewMain implements Initializable {

    @FXML
    private TextField txtUrl;

    @FXML
    private TextField txtEvery;

    @FXML
    private Button btnStart;

    private ScheduledExecutorService scheduler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStart.setOnAction(e -> startPinging());
    }

    private void startPinging() {
        String link = txtUrl.getText().trim();
        int seconds;

        try {
            seconds = Integer.parseInt(txtEvery.getText().trim());
        } catch (Exception ex) {
            System.out.println("Interval not valid bruh");
            return;
        }

        HttpClient client = HttpClient.newHttpClient();

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(link))
                        .GET()
                        .build();

                client.send(request, HttpResponse.BodyHandlers.discarding());
                System.out.println("Hit " + link);

            } catch (Exception ex) {
                System.out.println("Request failed: " + ex.getMessage());
            }

        }, 0, seconds, TimeUnit.SECONDS);

        System.out.println("Started job");
    }
}

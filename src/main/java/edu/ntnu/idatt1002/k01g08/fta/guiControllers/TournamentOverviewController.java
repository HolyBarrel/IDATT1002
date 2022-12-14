package edu.ntnu.idatt1002.k01g08.fta.guiControllers;

import edu.ntnu.idatt1002.k01g08.fta.Main;
import edu.ntnu.idatt1002.k01g08.fta.util.SceneManager;
import edu.ntnu.idatt1002.k01g08.fta.controllers.Admin;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for the tournament-overview page
 *
 * @author johnfb, teodorbi
 */
public class TournamentOverviewController {
    @FXML
    private BorderPane root;
    @FXML
    private Label errorLabel;
    @FXML
    private Label registeredTeamsLabel;

    ArrayList<String> teamsAdded = new ArrayList<>();
    @FXML
    private Button nextMatchButton;
    @FXML
    private Label tournamentNameLabel;
    @FXML
    private VBox historyContainer;
    @FXML
    private Label nextMatchLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label teamsLeftLabel;
    @FXML
    private ImageView reportButton;
    @FXML
    private ImageView settingsButton;
    @FXML
    private ImageView backButton;
    @FXML
    private ImageView homeButton;

    private boolean tournamentDone = false;

    /**
     * Initializes the view on load.
     */
    @Deprecated
    public void initialize() {
        root.getStylesheets().add(Main.class.getResource(Admin.getActiveStyle()).toExternalForm());
        Tooltip.install(reportButton, new Tooltip("Go to report page"));
        Tooltip.install(settingsButton, new Tooltip("Go to settings"));
        Tooltip.install(backButton, new Tooltip("Go back to last page"));
        Tooltip.install(homeButton, new Tooltip("Go to home page"));
        tournamentNameLabel.setText(Admin.getActiveTournament().getTournamentName());
        try {
            nextMatchLabel.setText(Admin.getNextMatch().getHomeTeam().getName() + " vs " + Admin.getNextMatch().getAwayTeam().getName());
        } catch (IndexOutOfBoundsException e) {
            tournamentDone = true;
            nextMatchLabel.setText("The tournament is finished,\n" + Admin.getActiveTournament().getWinner().getName() + " won!");
            nextMatchButton.setDisable(true);
        }
        teamsLeftLabel.setText(Admin.getActiveTournament().getNumberOfTeams() + " teams are registered for this tournament");
        int matchesLeft = Admin.getActiveTournament().getUpcomingMatches().size();
        if (matchesLeft > 1) {
            statusLabel.setText(matchesLeft + " confirmed matches left");
        } else if (matchesLeft == 1) {
            statusLabel.setText(matchesLeft + " confirmed match left");
        } else {
            statusLabel.setText("All matches have been played");
        }

        if (Admin.getActiveTournament().getMatches().isEmpty()) {
            Label label = new Label("No matches have been played");
            historyContainer.getChildren().add(label);
        } else {
            for (int i = 0; i < Admin.getActiveTournament().getMatches().size(); i++) {
                String homeTeam = Admin.getActiveTournament().getMatches().get(i).getHomeTeam().getName();
                String awayTeam = Admin.getActiveTournament().getMatches().get(i).getAwayTeam().getName();
                int homeScore = Admin.getActiveTournament().getMatches().get(i).getHomeTeamScore();
                int awayScore = Admin.getActiveTournament().getMatches().get(i).getAwayTeamScore();
                String title = homeTeam + " vs " + awayTeam + " (" + homeScore + ":" + awayScore + ")";
                Button button = new Button((i + 1) + " - " + title);
                button.setPrefWidth(375.0);
                button.setCursor(Cursor.HAND);
                int finalI = i;
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    try {
                        loadMatchHistory(finalI);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                historyContainer.getChildren().add(button);
            }
        }
    }

    /**
     * Goes to the settings view when user clicks on the settings button
     * @param event Click event
     * @throws IOException if an error occurs
     */
    @FXML
    public void settingsButtonClick(Event event) throws IOException {
        SceneManager.setView("settings");
    }

    /**
     * Goes to the report page view when the user clicks on the report button
     * @param event Click event
     * @throws IOException if an error occurs
     */
    @FXML
    public void reportButtonClick(Event event) throws IOException {
        SceneManager.setView("errorForm");
    }

    /**
     * Goes to the main page view when the user clicks on the home button
     * @param event Click event
     * @throws IOException if an error occurs
     */
    @FXML
    public void exitButtonClick(Event event) throws IOException {
        SceneManager.setView("main");
    }

    /**
     * Goes to the last page view when the user clicks on the back button
     * @param event Click event
     * @throws IOException if an error occurs
     */
    @FXML
    public void backButtonClick(Event event) throws IOException {
        SceneManager.goToLastScene();
    }

    /**
     * Goes to the next match page when the user clicks on the next match button
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void nextMatch(ActionEvent actionEvent) throws IOException {
        SceneManager.setView("match");
    }

    /**
     * Goes to the match report of the selected match
     * @param n is the index in the register of the selected match
     * @throws IOException if an error occurs
     */
    @FXML
    public void loadMatchHistory(int n) throws IOException {
        Admin.setCurrentReportedMatch(n);
        SceneManager.setView("matchReport");
    }

    /**
     * Lets the user save the tournament when clicking the save button
     * @param actionEvent Click Event
     */
    @FXML
    public void save(ActionEvent actionEvent) {
        try {
            Admin.saveTournament(Admin.getActiveTournament());
            errorLabel.setText("Tournament was saved");
        } catch (IOException e) {
            errorLabel.setText("Was not able to save the active tournament");
        }
    }

    /**
     * Enables user to use enter key to go to the settings page
     * @param event KeyEvent
     * @throws IOException if an error occurs
     */
    @FXML
    public void settingsButtonEnter(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            settingsButtonClick(event);
        }
    }

    /**
     * Enables user to use enter key to go to the report page
     * @param event KeyEvent
     * @throws IOException if an error occurs
     */
    @FXML
    public void reportButtonEnter(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            reportButtonClick(event);
        }
    }

    /**
     * Enables user to use enter key to use the back button
     * @param event KeyEvent
     * @throws IOException if an error occurs
     */
    @FXML
    public void backButtonEnter(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            backButtonClick(event);
        }
    }

    /**
     * Enables user to use enter key to go to the home page
     * @param event KeyEvent
     * @throws IOException if an error occurs
     */
    @FXML
    public void homeButtonEnter(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            exitButtonClick(event);
        }
    }
}
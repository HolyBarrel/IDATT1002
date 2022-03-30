package edu.ntnu.idatt1002.k01g08.fta.guiControllers;

import edu.ntnu.idatt1002.k01g08.fta.Main;
import edu.ntnu.idatt1002.k01g08.fta.SceneManager;
import edu.ntnu.idatt1002.k01g08.fta.objects.KnockOut;
import edu.ntnu.idatt1002.k01g08.fta.objects.Tournament;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import edu.ntnu.idatt1002.k01g08.fta.SceneManager;

import java.io.IOException;

public class NewTournamentController {

    @FXML
    private TextField tournamentNumberTeamsInput;
    @FXML
    private TextField tournamentNameInput;
    @FXML
    private Label errorLabel;

    @FXML
    public void createTournament(ActionEvent actionEvent) throws IOException {
        Tournament tournament = new KnockOut(tournamentNameInput.getText());
        Main.getTournamentRegister().addTournament(tournament);
        Main.setNumOfTeams(Integer.parseInt(tournamentNumberTeamsInput.getText()));
        SceneManager.setView("newTeam");
    }
}
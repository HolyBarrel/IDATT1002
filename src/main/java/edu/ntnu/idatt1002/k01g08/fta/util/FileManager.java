package edu.ntnu.idatt1002.k01g08.fta.util;

import edu.ntnu.idatt1002.k01g08.fta.objects.*;
import edu.ntnu.idatt1002.k01g08.fta.registers.TeamRegister;

import javax.json.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Contains static methods for reading and writing teams and tournaments to JSON files.
 */
public class FileManager {
    private static final String PLAYER_NAME_KEY = "name";
    private static final String PLAYER_NUMBER_KEY = "number";
    private static final String PLAYER_GOALS_KEY = "goals";
    private static final String PLAYER_ASSISTS_KEY = "assists";
    private static final String PLAYER_RED_CARDS_KEY = "redCards";
    private static final String PLAYER_YELLOW_CARDS_KEY = "yellowCards";

    private static final String TEAM_NAME_KEY = "name";
    private static final String TEAM_PLAYERS_KEY = "players";

    private static final String TOURNAMENT_NAME_KEY = "name";
    private static final String TOURNAMENT_PRIZE_KEY = "prize";
    private static final String TOURNAMENT_FORMAT_KEY = "format";
    private static final String TOURNAMENT_START_DATE_KEY = "startDate";
    private static final String TOURNAMENT_MATCH_LENGTH_KEY = "matchLength";
    private static final String TOURNAMENT_UPCOMING_MATCHES_KEY = "upcomingMatches";
    private static final String TOURNAMENT_PREVIOUS_MATCHES_KEY = "upcomingMatches";
    private static final String TOURNAMENT_TEAMS_KEY = "teams";

    private static final String MATCH_HOME_TEAM_KEY = "homeTeam";
    private static final String MATCH_AWAY_TEAM_KEY = "awayTeam";
    private static final String MATCH_EVENTS_KEY = "events";

    private static final String GAME_EVENT_HOME_TEAM_KEY = "homeTeam";
    private static final String GAME_EVENT_PLAYER_NUMBER_KEY = "playerNumber";
    private static final String GAME_EVENT_TIMESTAMP_KEY = "timestamp";
    private static final String GAME_EVENT_TYPE_KEY = "type";

    private static final String GAME_EVENT_SUBSTITUTION_TYPE = "substitution";
    private static final String GAME_EVENT_GOAL_TYPE = "goal";
    private static final String GAME_EVENT_SELF_GOAL_TYPE = "selfGoal";
    private static final String GAME_EVENT_FOUL_TYPE = "foul";

    private static final String FOUL_TAG_KEY = "foulTag";
    private static final String FOUL_GIVE_CARD_KEY = "giveCard";

    private static final String SUBSTITUTION_PLAYER_IN_KEY = "playerIn";
    private static final String SUBSTITUTION_PLAYER_OUT_KEY = "playerOut";

    private static final String GOAL_ASSISTING_KEY = "assisting";

    static JsonStructure loadJson(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            try (JsonReader jsonReader = Json.createReader(fileReader)) {
                return jsonReader.read();
            }
        }
    }

    static JsonArray loadJsonArray(File file) throws IOException {
        return (JsonArray) loadJson(file);
    }

    static void saveJson(JsonStructure json, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            try (JsonWriter jsonWriter = Json.createWriter(fileWriter)) {
                jsonWriter.write(json);
            }
        }
    }

    static Player readPlayer(JsonObject json) {
        Player player = new Player(json.getString(PLAYER_NAME_KEY), json.getInt(PLAYER_NUMBER_KEY));
        for (String key : json.keySet()) {
            switch (key) {
                case PLAYER_GOALS_KEY:
                    player.increaseGoals(json.getInt(key));
                    break;
                case PLAYER_ASSISTS_KEY:
                    player.increaseAssists(json.getInt(key));
                    break;
                case PLAYER_RED_CARDS_KEY:
                    player.increaseRedCards(json.getInt(key));
                    break;
                case PLAYER_YELLOW_CARDS_KEY:
                    player.increaseYellowCards(json.getInt(key));
                    break;
            }
        }
        return player;
    }

    static Team readTeam(JsonObject json) {
        Team team = new Team(json.getString(TEAM_NAME_KEY));
        List<JsonObject> players = json.getJsonArray(TEAM_PLAYERS_KEY).getValuesAs(JsonObject.class);
        for (JsonObject player : players)
        if (!team.addPlayer(readPlayer(player))) throw new RuntimeException("player number already registered");
        return team;
    }

    static TeamRegister readTeamRegister(JsonArray json) {
        TeamRegister teamRegister = new TeamRegister();
        List<JsonObject> teams = json.getValuesAs(JsonObject.class);
        for (JsonObject team : teams) {
            teamRegister.addTeam(readTeam(team));
        }
        return teamRegister;
    }

    static TeamRegister loadTeamRegister(File file) throws IOException {
        return readTeamRegister(loadJsonArray(file));
    }

    static JsonObject toJson(Player player) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(PLAYER_NAME_KEY, player.getName());
        builder.add(PLAYER_NUMBER_KEY, player.getNumber());
        builder.add(PLAYER_GOALS_KEY, player.getGoals());
        builder.add(PLAYER_ASSISTS_KEY, player.getAssists());
        builder.add(PLAYER_YELLOW_CARDS_KEY, player.getYellowCards());
        builder.add(PLAYER_RED_CARDS_KEY, player.getRedCards());
        return builder.build();
    }

    static JsonObject toJson(Team team) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Player player : team) {
            arrayBuilder.add(toJson(player));
        }
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add(TEAM_NAME_KEY, team.getName());
        objectBuilder.add(TEAM_PLAYERS_KEY, arrayBuilder.build());
        return objectBuilder.build();
    }

    static JsonObject toJson(GameEvent event, Team homeTeam) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add(GAME_EVENT_HOME_TEAM_KEY, event.getTeam() == homeTeam);
        objectBuilder.add(GAME_EVENT_TIMESTAMP_KEY, event.getTimeStampOfMatchTime());
        Player player = event.getPlayer();
        if (player != null) {
            objectBuilder.add(GAME_EVENT_PLAYER_NUMBER_KEY, player.getNumber());
        }
        if (event.getClass() == Foul.class) objectBuilder.addAll(toJson((Foul) event));
        else if (event.getClass() == Goal.class) objectBuilder.addAll(toJson((Goal) event));
        else if (event.getClass() == Substitution.class) objectBuilder.addAll(toJson((Substitution) event));
        return objectBuilder.build();
    }

    static JsonObjectBuilder toJson(Foul foul) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(GAME_EVENT_TYPE_KEY, GAME_EVENT_FOUL_TYPE);
        String foulTag = foul.getFoulTag();
        if (foulTag != null) builder.add(FOUL_TAG_KEY, foulTag);
        if (foul.getRedCard() > 0) builder.add(FOUL_GIVE_CARD_KEY, 2);
        else if (foul.getYellowCard() > 0) builder.add(FOUL_GIVE_CARD_KEY, 1);
        return builder;
    }

    static JsonObjectBuilder toJson(Goal goal) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Player player = goal.getPlayer();
        if (goal.getTeam().getPlayer(player.getNumber()) == player) {
            builder.add(GAME_EVENT_TYPE_KEY, GAME_EVENT_GOAL_TYPE);
        } else {
            builder.add(GAME_EVENT_TYPE_KEY, GAME_EVENT_SELF_GOAL_TYPE);
        }
        if (goal.getAssistingPlayer() != null) builder.add(GOAL_ASSISTING_KEY, goal.getAssistingPlayer().getNumber());
        return builder;
    }

    static JsonObjectBuilder toJson(Substitution substitution) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(GAME_EVENT_TYPE_KEY, GAME_EVENT_SUBSTITUTION_TYPE);
        builder.add(SUBSTITUTION_PLAYER_IN_KEY, substitution.getPlayerIn().getNumber());
        builder.add(SUBSTITUTION_PLAYER_OUT_KEY, substitution.getPlayerOut().getNumber());
        return builder;
    }

    static JsonObject toJson(Match match) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add(MATCH_HOME_TEAM_KEY, match.getHomeTeam().getName());
        objectBuilder.add(MATCH_AWAY_TEAM_KEY, match.getAwayTeam().getName());

        Iterator<GameEvent> eventIterator = match.iterator();

        if (eventIterator.hasNext()) {
            JsonArrayBuilder eventArrayBuilder = Json.createArrayBuilder();
            for (GameEvent event : match) {
                eventArrayBuilder.add(toJson(event, match.getHomeTeam()));
            }
            objectBuilder.add(MATCH_EVENTS_KEY, eventArrayBuilder.build());
        }

        return objectBuilder.build();
    }

    static JsonObject toJson(Tournament tournament) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add(TOURNAMENT_FORMAT_KEY, tournament.getClass().getName());
        objectBuilder.add(TOURNAMENT_NAME_KEY, tournament.getTournamentName());

        if (tournament.getFirstPrize() > 0) {
            objectBuilder.add(TOURNAMENT_PRIZE_KEY, tournament.getFirstPrize());
        }

        if (tournament.getStartDate() != null) {
            objectBuilder.add(TOURNAMENT_START_DATE_KEY, tournament.getStartDate());
        }
        objectBuilder.add(TOURNAMENT_MATCH_LENGTH_KEY, tournament.getMatchLength());

        JsonArrayBuilder teamsBuilder = Json.createArrayBuilder();
        for (Team team : tournament.getTeams()) {
            teamsBuilder.add(team.getName());
        }
        objectBuilder.add(TOURNAMENT_TEAMS_KEY, teamsBuilder.build());

        JsonArrayBuilder upcomingBuilder = Json.createArrayBuilder();
        if (tournament.getUpcomingMatches() != null && !tournament.getUpcomingMatches().isEmpty()) {
            for (Match match : tournament.getUpcomingMatches()) {
                upcomingBuilder.add(toJson(match));
            }
        }
        objectBuilder.add(TOURNAMENT_UPCOMING_MATCHES_KEY, upcomingBuilder.build());

        JsonArrayBuilder previousBuilder = Json.createArrayBuilder();
        if (tournament.getMatches() != null && !tournament.getMatches().isEmpty()) {
            for (Match match : tournament.getMatches()) {
                previousBuilder.add(toJson(match));
            }
        }
        objectBuilder.add(TOURNAMENT_PREVIOUS_MATCHES_KEY, previousBuilder.build());

        return objectBuilder.build();
    }
}

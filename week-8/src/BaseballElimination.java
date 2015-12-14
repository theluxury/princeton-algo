import edu.princeton.cs.algs4.*;

import java.util.*;

/**
 * Created by theluxury on 12/2/15.
 */
public class BaseballElimination {
    private int numberOfTeams;
    private HashMap<String, TeamRecord> teamRecords = new HashMap<>();
    private Set<String> certificateOfElimination;

    private class TeamRecord implements Comparable<TeamRecord> {
        private final String name;
        private final int index;
        private int wins;
        private int losses;
        private int totalGamesRemaining;
        private int[] remainingGames;

        public TeamRecord(String name, int index, int wins, int losses, int totalGamesReaming, int[] remainingGames) {
            this.name = name;
            this.index = index;
            this.wins = wins;
            this.losses = losses;
            this.totalGamesRemaining = totalGamesReaming;
            this.remainingGames = remainingGames;
        }

        public int compareTo(TeamRecord that) {
            if (this.index < that.index) {
                return -1;
            } else if (this.index > that.index) {
                return 1;
            } else { // shouldn't happen but what the heck.
                return 0;
            }
        }

    }

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In readIn = new In(filename);
        numberOfTeams = readIn.readInt();

        for (int i = 0; i < numberOfTeams; i++) {
            String teamName = readIn.readString();
            int wins = readIn.readInt();
            int losses = readIn.readInt();
            int totalGamesRemaining = readIn.readInt();
            int[] remainingGames = new int[numberOfTeams];
            for (int j = 0; j < numberOfTeams; j++) {
                remainingGames[j] = readIn.readInt();
            }
            TeamRecord teamRecord = new TeamRecord(teamName, i, wins, losses, totalGamesRemaining, remainingGames);
            teamRecords.put(teamName, teamRecord);
        }

    }

    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        ArrayList<String> teams = new ArrayList<>();
        for (String teamName : teamRecords.keySet()) {
            teams.add(teamName);
        }
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        checkForTeamName(team);
        return teamRecords.get(team).wins;
    }

    // number of losses for given team
    public int losses(String team) {
        checkForTeamName(team);
        return teamRecords.get(team).losses;
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkForTeamName(team);
        return teamRecords.get(team).totalGamesRemaining;

    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkForTeamName(team1);
        checkForTeamName(team2);
        int indexOfTeam2 = teamRecords.get(team2).index;
        return teamRecords.get(team1).remainingGames[indexOfTeam2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkForTeamName(team);
        // Quick check for trivial elimination
        for (TeamRecord teamRecord : teamRecords.values()) {
            if (teamRecord.wins > teamRecords.get(team).wins + teamRecords.get(team).totalGamesRemaining) {
                certificateOfElimination = new HashSet<>();
                certificateOfElimination.add(teamRecord.name);
                return true;
            }
        }

        // Need a flowNetwork, and then to add Flow edges to it.
        // Flownetwork should have... 2 (s and t) + (number of teams - 1) choose 2 + (number of teams - 1) vertices.
        int numVertices = 2 + (numberOfTeams - 1) * (numberOfTeams - 2) / 2 + (numberOfTeams - 1);
        // 0 will be s, numVertices - 1 will be t
        FlowNetwork flowNetwork = new FlowNetwork(numVertices);
        // Get sorted by index list of other teams.
        ArrayList<TeamRecord> otherTeams = getOthersTeams(team);

        // Go through each pair of other teams to add edge from s to games remaining.
        int nodeNumber = 1;
        int startOfNodeNumberForTeams = 1 + (numberOfTeams - 1) * (numberOfTeams - 2) / 2;
        for (int i = 0; i < otherTeams.size(); i++) {
            for (int j = i + 1; j < otherTeams.size(); j++) {
                FlowEdge flowEdge = new FlowEdge(0, nodeNumber,
                        against(otherTeams.get(i).name, otherTeams.get(j).name));
                // edge from vs game node to teams node
                FlowEdge team1Edge = new FlowEdge(nodeNumber, startOfNodeNumberForTeams + i, Integer.MAX_VALUE);
                FlowEdge team2Edge = new FlowEdge(nodeNumber, startOfNodeNumberForTeams + j, Integer.MAX_VALUE);
                flowNetwork.addEdge(flowEdge);
                flowNetwork.addEdge(team1Edge);
                flowNetwork.addEdge(team2Edge);
                nodeNumber++;
            }
        }

        // Then add edge from teams node to t
        for (int i = 0; i < otherTeams.size(); i++) {
            int numberOfGamesCanWin = teamRecords.get(team).wins +
                    teamRecords.get(team).totalGamesRemaining - otherTeams.get(i).wins;
            FlowEdge winsEdge = new FlowEdge(numVertices - 1 - otherTeams.size() + i,
                    numVertices - 1, numberOfGamesCanWin);
            flowNetwork.addEdge(winsEdge);
        }

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, numVertices - 1);
        certificateOfElimination = new HashSet<>();
        boolean eliminated = false;
        for (int i = 1; i < startOfNodeNumberForTeams + 1; i++) {
            if (fordFulkerson.inCut(i)) {
                eliminated = true;
            }
        }

        if (eliminated) {
            for (int i = startOfNodeNumberForTeams; i < numVertices - 1; i++) {
                if (fordFulkerson.inCut(i)) {
                    certificateOfElimination.add(otherTeams.get(i - startOfNodeNumberForTeams).name);
                }
            }
        }
        return eliminated;
    }

    private void checkForTeamName(String team) {
        if (teamRecords.get(team) == null) {
            throw new java.lang.IllegalArgumentException("No such team dag");
        }
    }

    private ArrayList<TeamRecord> getOthersTeams(String team) {
        ArrayList<TeamRecord> otherTeams = new ArrayList<>();
        for (TeamRecord teamRecord : teamRecords.values()) {
            if (!team.equals(teamRecord.name)) {
                otherTeams.add(teamRecord);
            }
        }
        Collections.sort(otherTeams);
        return otherTeams;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkForTeamName(team);
        if (!isEliminated(team)) {
            return null;
        } else {
            return certificateOfElimination;
        }
    }

    public static void main(String[] args) {
        // BaseballElimination division = new BaseballElimination(args[0]);
        BaseballElimination division = new BaseballElimination("/Users/theluxury/Documents/algs/week-8/baseball/teams32.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

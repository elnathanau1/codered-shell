package com.eau.codered.coderedshell.commands;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.services.DraftService;
import com.eau.codered.coderedshell.services.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@ShellComponent
public class DraftCommands {

    @Autowired
    private DraftService draftService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private UtilCommands utilCommands;

    // state
    private static boolean drafting = false;
    private static LeagueEntity leagueEntity = null;


    @ShellMethod(value = "Start a draft for a league", key = "start-draft")
    public String startDraft(@ShellOption(value = {"-l", "--league"}) String league) {
        // confirm with user
        Scanner scanner = new Scanner(System.in);
        System.out.println("This will delete any existing draft data. Continue? (y/n)");
        String userInput = scanner.next();

        if (!userInput.equals("y")) {
            return "start-draft cancelled.";
        }

        utilCommands.clear();

        draftService.setupDraft(league);

        drafting = true;
        leagueEntity = leagueService.getLeagueByName(league);

        return "Draft set up!";
    }

    @ShellMethod(value = "Display the draft board", key = "draft-board")
    public String draftBoard(@ShellOption(value = {"-s", "--sort"}, defaultValue = "htag_rk") String sort) {
        List<String> possibleSort = new ArrayList<>();
        if (drafting) {
            // display table
            List<String[]> model = new ArrayList<>();
            model.add(new String[]{
                    "htag_rk", "espn_rk", "espn_adp", "name", "pos", "gp", "fg_pct", "ft_pct", "3pm", "pts", "reb", "ast", "stl", "blk", "to", "total"
            });

            List<DraftingRoomEntity> draftingRoomEntities = draftService.getDraftBoard(leagueEntity.getName());
            for (DraftingRoomEntity draftingRoomEntity : draftingRoomEntities) {
                model.add(new String[]{
                        String.valueOf(draftingRoomEntity.getHashtagRank()),
                        String.valueOf(draftingRoomEntity.getEspnRank()),
                        String.valueOf(draftingRoomEntity.getEspnAdp()).substring(0, 4),
                        String.valueOf(draftingRoomEntity.getName()),
                        String.valueOf(draftingRoomEntity.getPos()),
                        String.valueOf(draftingRoomEntity.getGp()),
                        String.valueOf(draftingRoomEntity.getFgPercent()),
                        String.valueOf(draftingRoomEntity.getFtPercent()),
                        String.valueOf(draftingRoomEntity.getThreepm()),
                        String.valueOf(draftingRoomEntity.getPts()),
                        String.valueOf(draftingRoomEntity.getTreb()),
                        String.valueOf(draftingRoomEntity.getAst()),
                        String.valueOf(draftingRoomEntity.getStl()),
                        String.valueOf(draftingRoomEntity.getBlk()),
                        String.valueOf(draftingRoomEntity.getTurnovers()),
                        String.valueOf(draftingRoomEntity.getTotal()),

                });
            }

            String[][] array = new String[model.size()][model.get(0).length];
            for (int i = 0; i < model.size(); i++) {
                array[i] = model.get(i);
            }
            TableModel tableModel = new ArrayTableModel(array);
            TableBuilder tableBuilder = new TableBuilder(tableModel);
            tableBuilder.addFullBorder(BorderStyle.fancy_light);
            String table = tableBuilder.build().render(150);

            utilCommands.clear();
            return table;
        }
        return "Not currently drafting, use start-draft to begin";

    }
}

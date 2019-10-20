package com.eau.codered.coderedshell.util;

import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.util.List;

public class StringUtil {

    // https://www.dotnetperls.com/truncate-java
    public static String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }

    public static String listToTable(List<String[]> model) {
        String[][] array = new String[model.size()][model.get(0).length];
        for (int i = 0; i < model.size(); i++) {
            array[i] = model.get(i);
        }
        TableModel tableModel = new ArrayTableModel(array);
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        String table = tableBuilder.build().render(150);

        return table;
    }
}

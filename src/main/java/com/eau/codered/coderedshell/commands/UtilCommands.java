package com.eau.codered.coderedshell.commands;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;

@ShellComponent
class UtilCommands {

    @Autowired
    private Terminal terminal;

    void clear() {
        terminal.puts(InfoCmp.Capability.clear_screen);
    }


}

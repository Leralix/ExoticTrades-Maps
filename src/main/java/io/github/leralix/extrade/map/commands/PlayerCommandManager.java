package io.github.leralix.extrade.map.commands;

import io.github.leralix.extrade.map.commands.subcommands.UpdateTraderCommand;
import org.leralix.lib.commands.CommandManager;

public class PlayerCommandManager extends CommandManager {


    public PlayerCommandManager(){
        super("extrade_map.admins.commands");
        addSubCommand(new UpdateTraderCommand());
    }

    @Override
    public String getName() {
        return "extrade_map";
    }


}

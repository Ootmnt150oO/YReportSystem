package yuziouo.ReportSystem;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class callCmd extends Command {
    Main main;
    public callCmd(Main main) {
        super("call","開啟問題回報模式");
        this.main = main;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            main.callMenu(player);
        }
        return true;
    }
}

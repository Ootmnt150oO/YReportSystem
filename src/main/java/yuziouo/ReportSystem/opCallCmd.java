package yuziouo.ReportSystem;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class opCallCmd extends Command {
    Main main;
    public opCallCmd(Main main) {
        super("opcall","管理員查看問題");
        this.main = main;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player){
            if (commandSender.isOp()){
                main.OPCallmenu((Player) commandSender);
            }
        }
        return true;
    }
}

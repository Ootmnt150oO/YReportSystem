package yuziouo.ReportSystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerAsyncPreLoginEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.*;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends PluginBase implements Listener {
    HashMap<String,HashMap<String,String>> list = new HashMap<>();
    HashMap<String,String> report;
    HashMap<String,String> qqq;
    HashMap<String,HashMap<String,String>> rl = new HashMap<>();
    int call = 987;
    int questnew = 988;
    int questset = 989;
    int questnow = 990;
    int settingQuest = 991;
    int opcall = 993;
    int opQuest = 994;
    int optitle = 995;
    File file;
    Config config;
    Config cfg;
    @Override
    public void onEnable() {
        file = new File(getDataFolder()+"");
        if (!file.exists())file.mkdirs();
        config = new Config(file+"/question.yml",Config.YAML);
        cfg = new Config(file+"/report.yml",Config.YAML);
        getServer().getPluginManager().registerEvents(this,this);
        getServer().getCommandMap().register("call",new callCmd(this));
        getServer().getCommandMap().register("opcall",new opCallCmd(this));
        loadQuestion();
        loadReport();
        getServer().getScheduler().scheduleRepeatingTask(this,new RemoveEmptyTask(this),20);
        super.onEnable();
    }
    @Override
    public void onDisable() {
        saveQuestion();
        saveReport();
        super.onDisable();
    }
    public void loadQuestion(){
        HashMap<String,Object> map = (HashMap<String, Object>) config.getAll();
        list = (HashMap<String, HashMap<String, String>>) map.clone();
        for (String key:config.getKeys()){
            config.remove(key);
        }
        config.save();
    }
    public void loadReport(){
        HashMap<String,Object> map = (HashMap<String, Object>) config.getAll();
        rl = (HashMap<String, HashMap<String, String>>) map.clone();
        for (String key:cfg.getKeys()){
            cfg.remove(key);
        }
        cfg.save();
    }
    public void saveQuestion(){
        for (Map.Entry<String,HashMap<String,String>> entry: list.entrySet()){
            config.set(entry.getKey(),entry.getValue());
        }
        config.save();
    }
    public void saveReport(){
        for (Map.Entry<String,HashMap<String,String>> entry: rl.entrySet()){
            cfg.set(entry.getKey(),entry.getValue());
        }
        cfg.save();
    }
    @EventHandler
    public void respon(PlayerFormRespondedEvent event){
        Player player = event.getPlayer();
        int id = event.getFormID();
        if (event.wasClosed())return;
        FormResponseSimple response;
        FormResponseCustom cresponse;
        switch (id){
            case 987:
                response = (FormResponseSimple) event.getResponse();
                switch (response.getClickedButtonId()){
                    case 0:
                        Questnew(player);
                        break;
                    case 1:
                        setQuest(player);
                        break;
                    case 2:
                        nowQuest(player);
                        break;
                    case 3:
                        reportPlayer(player);
                        break;
                    default:
                        return;
                }
                break;
            case 988:
                 cresponse = (FormResponseCustom) event.getResponse();
                addReport(player,cresponse.getInputResponse(0),getStringDate()+"]"+cresponse.getInputResponse(1));
                player.sendMessage("??????????????????");
                break;
            case 989:
                response = (FormResponseSimple) event.getResponse();
                settingQuestion(player,response.getClickedButton().getText(),getReport(player).get(response.getClickedButton().getText()));
                break;
            case 991:
               cresponse = (FormResponseCustom) event.getResponse();
               String[] ss = cresponse.getLabelResponse(0).split(":");
               String[] aa = cresponse.getLabelResponse(2).split(":");
               if (cresponse.getToggleResponse(4)){
                   delReport(player,ss[1]);
                   return;
               }
               setReport(player,ss[1],aa[1], cresponse.getInputResponse(1),cresponse.getInputResponse(3));
                break;
            case 990:
                response = (FormResponseSimple) event.getResponse();
                nowQuestnow(player,response.getClickedButton().getText(),getReport(player).get(response.getClickedButton().getText()));
                break;
            case 993:
                response = (FormResponseSimple) event.getResponse();
                switch (response.getClickedButtonId()){
                    case 0:
                        onQuestion(player);
                        break;
                    case 1:
                        opReportM(player);
                        break;
                }
                break;
            case 994:
                response = (FormResponseSimple) event.getResponse();
                showQuest(player,response.getClickedButton().getText());
                break;
            case 995:
                response = (FormResponseSimple) event.getResponse();
                for (String s:list.keySet()){
                    for (Map.Entry<String,String> entry: list.get(s).entrySet()){
                        if (entry.getKey().equals(response.getClickedButton().getText())){
                            seeArticle(player,s,response.getClickedButton().getText());
                            return;
                        }
                    }
                    break;
                }
            case 996:
                cresponse = (FormResponseCustom) event.getResponse();
                if (cresponse.getToggleResponse(3)){
                    String[] aac =  cresponse.getLabelResponse(0).split(":");
                    delReport(Server.getInstance().getPlayer(valuegetkey(aac[1])),aac[1]);
                    player.sendMessage("????????????");
                }
                break;
            case 86869:
                cresponse = (FormResponseCustom) event.getResponse();
                String name = cresponse.getDropdownResponse(0).getElementContent();
                abc(player,cresponse.getInputResponse(1),name);
                break;
            case 635841:
                response = (FormResponseSimple) event.getResponse();
                ReportMw(player,response.getClickedButton().getText());

        }
    }
    public void callMenu(Player player){
        FormWindowSimple form = new FormWindowSimple("??????????????????", "");
        form.addButton(new ElementButton("????????????"));
        form.addButton(new ElementButton("????????????"));
        form.addButton(new ElementButton("??????????????????"));
        form.addButton(new ElementButton("??????"));
        form.addButton(new ElementButton("??????"));
        player.showFormWindow(form,call);
    }
    public void Questnew (Player player){
        FormWindowCustom form = new FormWindowCustom("??????????????????");
        form.addElement(new ElementInput("???????????????"));
        form.addElement(new ElementInput("?????????????????????"));
        player.showFormWindow(form,questnew);
    }
    public void setQuest(Player player){

        if (hasReport(player)) {
            FormWindowSimple form = new FormWindowSimple("??????????????????","????????????????????????");
            for (String w : getReport(player).keySet()) {
                form.addButton(new ElementButton(w));
            }
            player.showFormWindow(form,questset);
        }else {
            FormWindowSimple formWindowSimple = new FormWindowSimple("??????????????????","?????????????????????");
            player.showFormWindow(formWindowSimple);
        }
    }
    public void nowQuest(Player player){
        if (hasReport(player)) {
            FormWindowSimple formWindowSimple = new FormWindowSimple("??????????????????","");
            for (String w : getReport(player).keySet()) {
                formWindowSimple.addButton(new ElementButton(w));
            }
            player.showFormWindow(formWindowSimple,questnow);
        }else {
            FormWindowSimple formWindowSimple = new FormWindowSimple("??????????????????","?????????????????????");
            player.showFormWindow(formWindowSimple);
        }
    }
    public void settingQuestion(Player player,String tital, String s){
        String[] aa = s.split("]");
        FormWindowCustom formWindowCustom = new FormWindowCustom("??????????????????(??????)");
        formWindowCustom.addElement(new ElementLabel("????????????:"+tital));
        formWindowCustom.addElement(new ElementInput("????????????????????????"));
        formWindowCustom.addElement(new ElementLabel("?????????:"+aa[1]));
        formWindowCustom.addElement(new ElementInput("???????????????????????????"));
        formWindowCustom.addElement(new ElementToggle("??????????????????"));
        player.showFormWindow(formWindowCustom,settingQuest);
    }
    public void nowQuestnow(Player player,String tital,String s){
        String[] a = s.split("]");
        FormWindowSimple formWindowSimple = new FormWindowSimple(tital,"??????:"+a[0]+"\n????????????:\n"+a[1]);
        formWindowSimple.addButton(new ElementButton("??????"));
        player.showFormWindow(formWindowSimple);
    }
    public void OPCallmenu(Player player){
        FormWindowSimple simple = new FormWindowSimple("op????????????","");
        int sets = 0;
        for (String key:rl.keySet()){
         sets += rl.get(key).size();
        }
        simple.addButton(new ElementButton("??????"));
        simple.addButton(new ElementButton("??????("+sets+"???)"));
        player.showFormWindow(simple,opcall);
    }
    public void onQuestion(Player player){
        if (!list.isEmpty()) {
            FormWindowSimple simple = new FormWindowSimple("OP??????????????????", "");
            for (String m : list.keySet()) {
                simple.addButton(new ElementButton(m));
            }
            player.showFormWindow(simple,opQuest);
        }else {
            FormWindowSimple simple = new FormWindowSimple("OP??????????????????", "?????????????????????????????????");
            player.showFormWindow(simple);
        }
    }
    public void showQuest(Player player,String name){
        FormWindowSimple simple = new FormWindowSimple("OP??????????????????", "??????:"+name);
        for (String aa:list.get(name).keySet()){
            simple.addButton(new ElementButton(aa));
        }
        player.showFormWindow(simple,optitle);
    }
    public void seeArticle(Player player,String name,String title){
        String[] a = list.get(name).get(title).split("]");
        FormWindowCustom formWindowSimple = new FormWindowCustom("OP??????????????????");
        formWindowSimple.addElement(new ElementLabel("??????:"+title));
        formWindowSimple.addElement(new ElementLabel("??????:"+a[1]));
        formWindowSimple.addElement(new ElementLabel("????????????:"+a[0]));
        formWindowSimple.addElement(new ElementToggle("??????????????????"));
        player.showFormWindow(formWindowSimple,996);
    }
    public void reportPlayer(Player player){
        FormWindowCustom custom = new FormWindowCustom("??????????????????");
        List<String> a = new ArrayList<>();
        for (Player player1:Server.getInstance().getOnlinePlayers().values()){
            if (player1.getName().equals(player.getName())) continue;
            a.add(player1.getName());
        }
        custom.addElement(new ElementDropdown("????????????id",a));
        custom.addElement(new ElementInput("????????????"));
        player.showFormWindow(custom,86869);
    }
    public void addReport(Player player,String title, String s){
        if (!hasReport(player)) {
            report = new HashMap<>();
            report.put(title, s);
            list.put(player.getName(), report);
            player.sendMessage("??????????????????");
        }else {
            if (!getReport(player).containsKey(title)) {
                getReport(player).put(title, s);
                player.sendMessage("??????????????????");
            }else {
                player.sendMessage("???????????????????????????");
            }
        }
    }
    public void opReportM(Player player){
        if (!rl.isEmpty()) {
        FormWindowSimple custom = new FormWindowSimple("?????????????????????","");
            for (String s : rl.keySet()) {
                custom.addButton(new ElementButton(s));
            }
            player.showFormWindow(custom, 635841);
        }else {
            FormWindowSimple custom = new FormWindowSimple("?????????????????????","????????????????????????????????????");
            player.showFormWindow(custom);
        }
    }
    public void ReportMw(Player player,String s){
        FormWindowSimple custom = new FormWindowSimple("?????????????????????","");
        for (String keys: rl.get(s).keySet()){
            custom.addButton(new ElementButton(keys));
        }
        player.showFormWindow(custom,263211245);
    }
    public void abc(Player player,String why,String who){
        if (!abb(player)){
            qqq = new HashMap<>();
            qqq.put(who,getStringDate()+"||"+why);
            rl.put(player.getName(),qqq);
            player.sendMessage("????????????");
        }else {
            if (!getQqq(player).containsKey(who)){
                getQqq(player).put(who,getStringDate()+"||"+why);
                player.sendMessage("????????????");
            }else {
                player.sendMessage("???????????????????????????");
            }
        }
    }
    public void bac(Player player,String who){
        if (abb(player)){
            if (getQqq(player).containsKey(who)){
                getQqq(player).remove(who);
                player.sendMessage("????????????");
            }else {
                player.sendMessage("?????????????????????");
            }
        }else {
            player.sendMessage("???????????????????????????");
        }
    }
    public boolean abb(Player player){
        for (String bbc:rl.keySet()){
            if (bbc.contains(player.getName()))
                return true;
        }
        return false;
    }
    public HashMap<String,String> getQqq(Player player){
        for (Map.Entry<String,HashMap<String,String> >entry:rl.entrySet()){
            if (entry.getKey().contains(player.getName()))
                return entry.getValue();
        }
        return null;
    }
    public void delReport(Player player,String title){
        if (hasReport(player)){
            if (getReport(player).containsKey(title)){
                getReport(player).remove(title);
                player.sendMessage("????????????");
            }else {
                player.sendMessage("???????????????????????????");
            }
        }
    }
    public void setReport(Player player,String title,String s,String newt,String news){
        if (hasReport(player)){
            if (getReport(player).containsKey(title)){
                getReport(player).remove(title);
                getReport(player).put(newt,getStringDate()+"]"+news);
                player.sendMessage("????????????:)");
            }else {
                player.sendMessage("???????????????");
            }
        }else {
            player.sendMessage("??????????????????????????????");
        }
    }
    public boolean hasReport(Player player){
        return list.containsKey(player.getName());
    }
    public HashMap<String,String> getReport(Player player){
        return list.get(player.getName());
    }
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }
    public String valuegetkey(String aaa) {
        for (Map.Entry<String, HashMap<String, String>> entry : list.entrySet()) {
            if (entry.getValue().containsKey(aaa)){
                return entry.getKey();
            }
        }
        return null;
    }
}

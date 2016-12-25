/* Only The Memorial Package */
var status = 0;
var legends = ""

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
                legends += "#L0#Get The <BEGINNER> #l\r\n";
                legends += "#L1#Get The <PartyQuestsManis> #l\r\n";
                  legends += "#L2#Get The <QuestsSpecialists>#l\r\n";
                if (cm.getChar().getMonsterCount() >= 100000) {
                   legends += "#L3#Get The <SuperHunter>#l#\r\n";
                }
                if (cm.getChar().getHorntailSlyed() >= 1) {
                   legends += "#L4#Get The <Horntail Slyer>#l#\r\n";
                }
                if (cm.getChar().getPinkBeenSlyed() >= 1) {
                   legends += "#L5#Get The <Pinkbeen Slyer>#l#\r\n";
                }
                if (cm.getLevel() >= 200 && (cm.getJob().HERO || cm.getJob().DARKKNIGHT || cm.getJob().PALADIN)) {
                   legends += "#L6#Get The <King> #l\r\n";
                }
                if (cm.getLevel() >= 200 && (cm.getJob().BISHOP || cm.getJob().FP_ARCHMAGE || cm.getJob().IL_ARCHMAGE)) {
                   legends += "#L7#Get The <Devil> #l\r\n";
                }
                if (cm.getLevel() >= 200 && (cm.getJob().BOWMASTER || cm.getJob().MARKSMAN)) {
                   legends += "#L8#Get The <Genius> #l\r\n";
                }
                if (cm.getLevel() >= 200 && (cm.getJob().SHADOWER || cm.getJob().NIGHTLORD)) {
                   legends += "#L9#Get The <Ninja> #l\r\n";
                }
                if (cm.getLevel() >= 200 && (cm.getJob().BUCCANEER || cm.getJob().CORSAIR)) {
                   legends += "#L10#Get The <Sea King> #l\r\n";
                }
                if (cm.getFame() >= 100) {
                   legends += "#L11#Get The <Fame Star> #l\r\n";
                }
                if (cm.getFame() >= 500) {
                   legends += "#L12#Get The <Maple Idle Star> #l\r\n";
                }
                if (cm.getPvpKills() >= 200) {
                   legends += "#L13#Get The <Win King> #l\r\n";
                }
                if (cm.getPvpDeaths() >= 200) {
                   legends += "#L14#Get The <Loss King> #l\r\n";
                }
                if (cm.getGender() == 0) {
                   legends += "#L15#Get The <Romeo> #l\r\n";
                }
                if (cm.getGender() == 1) {
                   legends += "#L16#Get The <Juliet> #l\r\n";
                }
                if (cm.getLevel() >= 200 && cm.getReborns() >= 100) {
                   legends += "#L18#Get The <Rebirth King>#l\r\n";
                }
                if (cm.getChar().gmLevel() >= 1) {
                   legends += "#L19#Get The <GameMaster> #l\r\n";
                }
                if (cm.getLegend() != 0) {
                   legends += "#L20#Delete Legend#l\r\n";
                }
            cm.sendSimple ("Hi, I am Legend Master. I can give Legend for you! and... need #b#e1,500,000Mesos#n#k\r\n#b#L99#Whats is Legend System?#l\r\n" + legends);
        } else if (status == 1) {
            switch(selection) {
              case 99:
                cm.sendOk("Legend is Memorial Project Original System? Whats is Memorial Projects? Oh... Memorial Project is JMS Emulator Server Project Team.");
                cm.dispose();
              break;
              case 0:
                   legendid = 2;
                   legendname = "BEGINNER";
                 cm.sendYesNo("Do you want Legend <BEGINNER> ?");
              break;
              case 1:
                   legendid = 3;
                   legendname = "PartyQuestsMania";
                 cm.sendYesNo("Do you want Legend <PartyQuestsMania> ?");
              break;
              case 2:
                   legendid = 4;
                   legendname = "QuestsSpecialists";
                 cm.sendYesNo("Do you want Legend <QuestsSpecialists> ?");
              break;
              case 3:
                   legendid = 5;
                   legendname = "Super Hunter";
                 cm.sendYesNo("Do you want Legend <Super Hunter> ?");
              break;
              case 4:
                   legendid = 13;
                   legendname = "Horntail Slyer";
                 cm.sendYesNo("Do you want Legend <Horntail Slyer> ?");
              break;
              case 5:
                   legendid = 14;
                   legendname = "Pinkbeen Slyer";
                 cm.sendYesNo("Do you want Legend <Pinkbeen Slyer> ?");
              break;
              case 6:
                   legendid = 6;
                   legendname = "King";
                 cm.sendYesNo("Do you want Legend <King> ?");
              break;
              case 7:
                   legendid = 7;
                   legendname = "Devil";
                 cm.sendYesNo("Do you want Legend <Devil> ?");
              break;
              case 8:
                   legendid = 8;
                   legendname = "Genius";
                 cm.sendYesNo("Do you want Legend <Genius> ?");
              break;
              case 9:
                   legendid = 9;
                   legendname = "Ninja";
                 cm.sendYesNo("Do you want Legend <Ninja>?");
              break;
              case 10:
                   legendid = 10;
                   legendname = "Sea King";
                 cm.sendYesNo("Do you want Legend <Sea King>?");
              break;
              case 11:
                   legendid = 11;
                   legendname = "Fame Star";
                 cm.sendYesNo("Do you want Legend <Fame Star> ?");
              break;
              case 12:
                   legendid = 12;
                   legendname = "Maple Idle Star";
                 cm.sendYesNo("Do you want Legend <Maple Idle Star> ?");
              break;
              case 13:
                   legendid = 17;
                   legendname = "Win King";
                 cm.sendYesNo("Do you want Legend <Win King> ?");
              break;
              case 14:
                   legendid = 16;
                   legendname = "Loss King";
                 cm.sendYesNo("Do you want Legend <Loss King> ?");
              break;
              case 15:
                   legendid = 18;
                   legendname = "Romeo";
                 cm.sendYesNo("Do you want Legend  <Romeo> ?");
              break;
              case 16:
                   legendid = 19;
                   legendname = "Juliet";
                 cm.sendYesNo("Do you want Legend <Juliet> ?");
              break;
              case 17:
                   legendid = 20;
                   legendname = "VIPPER";
                 cm.sendYesNo("Do you want Legend <VIPPER> ?");
              break;
              case 18:
                   legendid = 21;
                   legendname = "Rebirth King";
                 cm.sendYesNo("Do you want Legend <Rebirth King> ?");
              break;
              case 19:
                   legendid = 1;
                   legendname = "GameMaster";
                 cm.sendYesNo("Do you want Legend <GameMaster> ?");
              break;
              case 20:
                   legendid = 0;
                   legendname = "Delete";
                 cm.sendYesNo("Delete Legend? You need#b#e1,500,000Mesos#n#k!");
              break;
            }
           } else if (status == 2) {
               if (cm.getMeso() >= 1500000) {
                   cm.getChar().setLegend(legendid);
                   cm.gainMeso(-1500000);
                 cm.sendYesNo("You get <" + legendname + "> !");
                   cm.dispose();
               } else {
                 cm.sendOk("You are enoghs Mesos.");
                   cm.dispose();
               }
           }
      }
}  
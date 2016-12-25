/* Created By Meetoo From MapleStory.*/ 

var wui = 0; 

function start() { 
    cm.sendSimple ("Hello, I'm The Event Trophie Trader For MapleStory. If you got any Trophies you could exchange them For prizes. But Please Remeber To Pick Wisley I Do Not Give Any Refunds. Please Also remeber More Prizes Will be Added.\r\n#L0# #b#b5#k Trophies For A  #i2022178#\n\ x100  #l\r\n#L1##b30#k Trophies For  #i2340000#\n\ x10  #l\r\n#L2##b30#k Trophies For A  #i1012105#\n\  #l\r\n#L3##b50#k Tropies For A #i1302049#\n\  #l\r\n#L4##b70#k Trophies For  #i1082223#\n\  #l\r\n#L5##b100#k Trophies For A  #i1002518#\n\  #l\r\n#L6##b100#k Trophies For A  #i2101024#\n\  #l\r\n#L7##b1000#k Trophies For A #i1002140#\n\ "); 
} 

function action(mode, type, selection) { 
cm.dispose(); 
    if (selection == 0) { 

 if (cm.haveItem(4000038, 5)) {  
                      cm.gainItem(2022178,100); 
                      cm.gainItem(4000038, -5); 
                      cm.sendOk("Well done, Here are your 100 All Cure Poitions Use Them Wisley."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You Do Not Have Enough Trophies For 100 All Cure Poitions "); 
                cm.dispose(); 
                } 

     
        } else if (selection == 1) { 


                    if (cm.haveItem(4000038, 30)) { 
                     cm.gainItem(2340000,10);  
                      cm.gainItem(4000038, -30); 
                      cm.sendOk("Well done!, Here Are Your 10 White SCrolls."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You Do Not Have Enough Trophies For 10 White Scrolls. "); 
                cm.dispose(); 
                } 


        } else if (selection == 2) { 

                    if (cm.haveItem(4000038, 30)) { 
                      cm.gainItem(1012105,1);  
                      cm.gainItem(4000038, -30); 
                      cm.sendOk("Well done!, Here Is your Baby Pacifier."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You Do Not Have Enough Trophies For a baby Pacifier. "); 
                cm.dispose(); 
                } 



        } else if (selection == 3) { 


                    if (cm.haveItem(4000038, 50)) { 
                   
                      cm.gainItem(1302049,1); 
                      cm.gainItem(4000038, -50); 
                      cm.sendOk("Well done!, Here Is your #r Glowing Whip#k."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You Do Not Have Enough Trophies For a Glowing Whip. "); 
                cm.dispose(); 
                } 



        } else if (selection == 4) { 
   
                    if (cm.haveItem(4000038, 70)) { 
                   
                      cm.gainItem(1082223,1); 
                      cm.gainItem(4000038, -70); 
                      cm.sendOk("Well done!, Here Are your StromCaster Gloves."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You Do Not Have Enough Trophies For a Pair Of StormCaster Gloves . "); 
                cm.dispose(); 
                } 

        } else if (selection == 5) { 

                    if (cm.haveItem(4000038, 100)) { 
                      cm.gainItem(1002518,1);  
                      cm.gainItem(4000038, -100); 
                      cm.sendOk("We'll Done! Here Is Your Maple Bandana, Please go Collect More Trophies."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You Do Not Have Enough Trophies For a Blue Maple Bandana. "); 
                cm.dispose(); 
                }

 
         } else if (selection == 6) { 

                    if (cm.haveItem(4000038, 100)) {
 
                      cm.gainItem(2101024,1);  
                      cm.gainItem(4000038, -100); 
                      cm.sendOk("We'll Done On Getting A Silver Slime Summoning Sack, Please go Collect more Trophies."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You Do Not Have Enough Trophies For a Silver Slime Summoning Sack. "); 
                cm.dispose(); 
                } 


            } else if (selection == 7) { 

                    if (cm.haveItem(4000038, 1000)) {
 
                      cm.gainItem(1002140,1);  
                      cm.gainItem(4000038, -1000); 
                      cm.sendOk("Congratz On Getting The Top Prize A Wizet Hat!, Please go Collect more Trophies."); 
                      cm.dispose(); 
            } else { 
                cm.sendOk(" Sorry. You do not have enough Trophies. "); 
                cm.dispose(); 
                } 


        cm.dispose(); 
        }     


     
}  

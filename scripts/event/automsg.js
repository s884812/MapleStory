/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
var setupTask;

function init() {
    scheduleNew();
}

function scheduleNew() {
    var cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    var nextTime = cal.getTimeInMillis();
    while (nextTime <= java.lang.System.currentTimeMillis())
        nextTime += 300 * 1000;
    setupTask = em.scheduleAtTimestamp("start", nextTime);
}

function cancelSchedule() {
    setupTask.cancel(true);
}

function start() {
    scheduleNew();
    var Message = new Array("The more you use a weapon, the higher it will level up", "You have random chances to gain a 150% exp bonus", "You will gain medals for various achievements", "type '@help' or '@commands' to view a list of commands", "You will always auto job advance if there isn't a choice between classes", "You gain NX and lots of other possible bonuses by leveling up", "Sometimes you'll gain NX for killing mobs", "Your stats will be reset upon getting the first job advance", "You can earn a vote point from voting every 12 hours.", "Many rare items are availible exclusively through the use of vote points", "Donators receieve an extra set of commands and donator points based on how much they donate", "You gain 100 GM scrolls for donating 50$ or more.", "On your fifth rebirth, you get the GM job.", "Your legend will appear in the place of a medal when you smega");
    em.getChannelServer().yellowWorldMessage("[HiddenMSTip] " + Message[Math.floor(Math.random() * Message.length)]);
}
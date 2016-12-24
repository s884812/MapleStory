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
package net.channel.handler;

import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleClient;
import client.command.Commands;

public final class GeneralchatHandler extends net.AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String s = slea.readMapleAsciiString();
        char heading = s.charAt(0);
        if (heading == '/' || heading == '!' || heading == '@') {
            String[] sp = s.split(" ");
            sp[0] = sp[0].toLowerCase().substring(1);
            if (c.getPlayer().isDonator()) {
            Commands.executeDonatorCommand(c, sp, heading);
            }
            if (!Commands.executePlayerCommand(c, sp, heading)) {
                if (c.getPlayer().isGM()) {
                    c.getPlayer().addCommandToList(s);
                    if (!Commands.executeGMCommand(c, sp, heading)) {
                        Commands.executeAdminCommand(c, sp, heading);
                    }
                }
            }
        } else {
             if (s.contains("fuck") || s.contains("shit") || s.contains("bitch") || s.contains("hoe") || s.contains("cunt") || s.contains("hell") || s.contains("penis") || s.contains("vagina") || s.contains("dick") || s.contains("whore")) {
             c.getPlayer().giftMedal(1142071);
             }
                 c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), s, c.getPlayer().getGMChat(), slea.readByte()));

        }
    }
}


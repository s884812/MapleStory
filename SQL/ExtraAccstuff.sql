ALTER TABLE `accounts` ADD `message` tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE `characters` ADD COLUMN `donatorpoints` int(11) NOT NULL DEFAULT '0';    
ALTER TABLE `characters` ADD COLUMN `votepoints` int(11) NOT NULL DEFAULT '0';  
ALTER TABLE `characters` ADD COLUMN `occupation` int(11) NOT NULL DEFAULT '0';  
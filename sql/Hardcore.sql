-- Simple enough. Hardcore mode.
ALTER TABLE `MapleStoryv83`.`characters` ADD COLUMN `hardcore` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' AFTER `summonValue`, ADD COLUMN `dead` tinyint(1) UNSIGNED NOT NULL DEFAULT '0' AFTER `hardcore`;

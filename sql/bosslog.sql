CREATE TABLE `bosslog` (
`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
`accountid` INTEGER UNSIGNED NOT NULL,
`characterid` INTEGER UNSIGNED NOT NULL,
`bossid` VARCHAR(20) NOT NULL,
`lastattempt` TIMESTAMP NOT NULL,
PRIMARY KEY (`id`)
)
DROP DATABASE IF EXISTS `stop_drinking`;
CREATE DATABASE IF NOT EXISTS `stop_drinking`;
USE `stop_drinking`;

CREATE TABLE IF NOT EXISTS `Member`(

	memberId VARCHAR(20) NOT NULL,
    memberPw VARCHAR(100) NOT NULL,
    memberEmail VARCHAR(100) NOT NULL,
    PRIMARY KEY(`memberId`),
    UNIQUE KEY(`memberId`, `memberEmail`)

) CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS `Party`(

	partyId INT NOT NULL AUTO_INCREMENT,
    
    partyTension INT NOT NULL,
    partyDrinkingYesterday INT NOT NULL,
    partySleepHour INT NOT NULL,
    
    partyHolder VARCHAR(20) NOT NULL,
    partyTitle VARCHAR(100) NOT NULL,
    partyDate DATETIME NOT NULL,
    partyCompany VARCHAR(100),
    
    PRIMARY KEY(`partyId`),
    UNIQUE KEY(`partyHolder`, `partyTitle`, `partyDate`),
    FOREIGN KEY (`partyHolder`) REFERENCES `Member`(`memberId`)

) CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS `Feedback`(

	partyId INT NOT NULL,
    feedbackAmountDrink INT NOT NULL,
    feedbackDrinkness INT NOT NULL,
    
    feedbackMemo TEXT,
    
    PRIMARY KEY(`partyId`),
    FOREIGN KEY(`partyId`) REFERENCES `Party`(partyId)

) CHARACTER SET utf8 COLLATE utf8_general_ci;

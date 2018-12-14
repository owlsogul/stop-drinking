CREATE TABLE IF NOT EXISTS `Member`(

	memberId VARCHAR(20) NOT NULL,
    memberPw VARCHAR(100) NOT NULL,
    memberEmail VARCHAR(100) NOT NULL,
    PRIMARY KEY(`memberId`),
    UNIQUE(`memberId`, `memberEmail`)

);

CREATE TABLE IF NOT EXISTS `Party`(

	partyId INTEGER NOT NULL,

	partyTension INT NOT NULL,
	partyDrinkingYesterday INT NOT NULL,
    partySleepHour INT NOT NULL,

    partyHolder VARCHAR(20) NOT NULL,
    partyTitle VARCHAR(100) NOT NULL,
    partyDate DATE,
    partyCompany VARCHAR(100),

    PRIMARY KEY(`partyId`),
    UNIQUE(`partyHolder`, `partyTitle`, `partyDate`),
    FOREIGN KEY (`partyHolder`) REFERENCES `Member`(`memberId`)

);

CREATE TABLE IF NOT EXISTS `Feedback`(

	partyId INTEGER NOT NULL,
    feedbackAmountDrink INT NOT NULL,
    feedbackDrinkness INT NOT NULL,

    feedbackMemo TEXT,

    PRIMARY KEY(`partyId`),
    FOREIGN KEY(`partyId`) REFERENCES `Party`(partyId)

);

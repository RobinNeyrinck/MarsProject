INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('bruh', 'bruh', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Denji', 'Makima', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Deez', 'eh', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Max', 'Mustermann', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Jean', 'Kirstein', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Heidi', 'Wurm', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Dome', 'Gregori', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Psycho', 'Andreas', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');
INSERT INTO users (FIRSTNAME, LASTNAME, AVATAR) VALUES ('Tante', 'Marieanne', 'https://alanmajchrowicz.com/wp-content/uploads/2019/01/glacier_peak_image_lake_58240.jpg');

INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (2, 'Chainsaw check-up', '01-01-2052 18:15:00', 'Mars-Tokyo');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (2, 'Therapy', '03-01-2052 11:15:00', 'Mars-Tokyo');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (2, 'Skin care', '27-03-2052 18:15:00', 'Beauty Salon');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (2, 'Pedicure', '17-05-2052 11:15:00', 'Beauty Salon');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (3, 'Deez nuts', '01-01-2052 17:15:00', 'Bam');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (4, 'Prostate Exam', '01-01-2052 17:15:00', 'Galle');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (5, 'Regular Checkup', '07-04-2052 19:15:00', 'Bam');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (5, 'Dance classes', '01-08-2052 17:15:00', 'Airy-0');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (6, 'Swimming', '01-01-2052 17:15:00', 'Galle');
INSERT INTO APPOINTMENTS (USERID, DESCRIPTION, DATETIME, LOCATION) values (3, 'Therapy', '10-08-2052 17:15:00', 'Galle');

INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Tom Knockaert', 'Howest is burning down!', 5, 'Mars', 1,'EMERGENCY');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Levi Ackerman', 'Give up on your dreams and die', 3, 'Wall Marshiganshina', 2,'EMERGENCY');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Heidi Wurm', 'Your Periode will start in 5 Days', 3, 'Galle', 2,'EMERGENCY');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Max Mustermann', 'Go for a walk, your have not reached your daily step goal', 2, 'Mars-Tokyo', 2,'EMERGENCY');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Eadaz uq-Nāra', 'Do not forget to use Sunscreen', 1, 'Airy-0', 2,'EMERGENCY');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Aki', 'Want´s to be your friend', 1, '', 2,'NEW_FRIEND');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Aki', 'You are now friends!', 1, '', 2,'NEW_FRIEND');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Bruh', 'Want´s to be your friend', 1, '', 2,'NEW_FRIEND');
INSERT INTO ALERTS(NAME, DESCRIPTION, URGENCY, LOCATION, USERID, TYPE) values ('Bruh', 'You are now friends!', 1, '', 2,'NEW_FRIEND');

INSERT INTO RECOMMENDATIONS(RECOMMENDATIONID, USERID, ACTIVITYNAME) values (1, 2, 'Go for a walk, your have not reached your daily step goal');
INSERT INTO RECOMMENDATIONS(RECOMMENDATIONID, USERID, ACTIVITYNAME) values (1, 2, 'Eat something, your blood sugar is low');
INSERT INTO RECOMMENDATIONS(RECOMMENDATIONID, USERID, ACTIVITYNAME) values (1, 2, 'Watch out with what you eat, you have consumed quite a lot of calories already');

INSERT INTO FRIENDS(USERID, FRIENDID) values ('2', '3');
INSERT INTO FRIENDS(USERID, FRIENDID) values ('2', '1');
INSERT INTO FRIENDS(USERID, FRIENDID) values ('2', '4');
INSERT INTO FRIENDS(USERID, FRIENDID) values ('2', '5');
INSERT INTO FRIENDS(USERID, FRIENDID) values ('2', '6');

INSERT INTO FRIENDS(USERID, FRIENDID) values ('1', '2');
INSERT INTO FRIENDS(USERID, FRIENDID) values ('1', '4');
INSERT INTO FRIENDS(USERID, FRIENDID) values ('1', '5');
INSERT INTO FRIENDS(USERID, FRIENDID) values ('1', '6');

INSERT INTO MEDICALDATA(USERID, CHRONICDISEASES,GENETICDISEASES, WEIGHT, HEIGHT, BLOODTYPE, PREGNANT, GENDER, ALLERGIES, BIRTHDATE, AGE) values (1, 'some diseases', 'ligma', 80.0, 180.0, 'A_POS', false, 'male', 'nuts', '2002-12-08T10:38:31.174403600', 20);
INSERT INTO MEDICALDATA(USERID, CHRONICDISEASES,GENETICDISEASES, WEIGHT, HEIGHT, BLOODTYPE, PREGNANT, GENDER, ALLERGIES, BIRTHDATE, AGE) values (2, 'some diseases', 'ligma', 80.0, 180.0, 'A_POS', false, 'male', 'katanas', '2006-12-08T10:38:31.174403600', 16);

INSERT INTO MEASUREMENTS(USERID,VALUE, DATETIME, MEASUREMENTTYPE, FOOTSTEPS, CALORIESBURNT) values (2, 50., 'bruh', 'CalorieMeasurement', 50, 15);
INSERT INTO MEASUREMENTS(USERID,VALUE, DATETIME, MEASUREMENTTYPE) values (2, 65, 'bruh', 'HeartrateMeasurement');
INSERT INTO MEASUREMENTS(USERID,VALUE, DATETIME, MEASUREMENTTYPE, BLOODSUGARLEVEL, BLOODPRESSURE) values (2, 65, 'bruh', 'BloodMeasurement', 10, 50);

INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (1, 2, 'I am fine, how are you?', '2021-12-08T10:38:31.174403600');
INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (2, 1, 'Hey, how are you?', '2020-01-01T10:38:31.174403600');
INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (2, 1, 'I am fine', '2021-12-10T10:38:31.174403600');

INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (1, 4, 'I am fine, how are you?', '2022-12-08T10:38:31.174403600');
INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (4, 1, 'Hey, how are you?', '2022-11-08T10:38:31.174403600');
INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (4, 1, 'I am fine', '2021-12-10T10:38:31.174403600');

INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (4, 2, 'I am fine, how are you?', '2022-12-08T10:38:31.174403600');
INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (2, 4, 'Hey, how are you?', '2022-11-08T10:38:31.174403600');
INSERT INTO MESSAGES(SENDERID, RECEIVERID, MESSAGE, TIMESTAMP) values (2, 4, 'I am fine', '2021-12-10T10:38:31.174403600');

INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 50 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 60 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 50 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 60 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 50 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 60 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 50 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 60 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 50 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 60 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 50 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 60 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 50 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 60 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 70 );
INSERT INTO HEALTHSCORES(USERID, HEALTHSCORE) values ( 2, 65 );
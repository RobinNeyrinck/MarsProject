drop table if exists friends;
drop table if exists alerts;
drop table if exists recommendations;
drop table if exists appointments;
drop table if exists measurements;
drop table if exists healthscores;
drop table if exists medicaldata;
drop table if exists messages;
drop table if exists users;

create table users
(
    userID    int auto_increment primary key,
    location  varchar(255),
    firstname varchar(255),
    lastname  varchar(255),
    avatar    varchar(255)
);

create table friends
(
    userID   int,
    friendID int,
    foreign key (userID) references users (userID),
    foreign key (friendID) references users (userID)
);

create table alerts
(
    alertID     int auto_increment primary key,
    userID      int,
    foreign key (userID) references users (userID),
    name        varchar(255),
    description varchar(255),
    urgency     int,
    location    varchar(255),
    type        varchar(255)
);

create table appointments
(
    appointmentID int auto_increment primary key,
    userID        int,
    description   varchar(255),
    datetime          varchar(255),
    location      varchar(255),
    foreign key (userID) references users (userID)
);

create table recommendations
(
    recommendationID int,
    userID           int,
    activityname     varchar(255),
    foreign key (userID) references users (userID)
);

create table medicaldata
(
    userID int,
    age int,
    birthdate varchar(255),
    chronicdiseases varchar(255),
    geneticdiseases varchar(255),
    allergies varchar(255),
    weight number,
    height number,
    bloodtype varchar(10),
    pregnant bool,
    gender varchar(10),
    foreign key (userID) references users (userID)
);

create table measurements(
                             userID int,
                             value number,
                             datetime varchar(50),
                             measurementtype varchar(50),
                             bloodSugarLevel number null ,
                             bloodPressure number null,
                             caloriesburnt int null,
                             footsteps int null,
                             foreign key (userID) references users (userID)
);

create table healthscores(
                             userID int,
                             healthscore int,
                             foreign key (userID) references users (userID)
);

create table messages(
                         receiverID int,
                         senderID int,
                         message varchar(255),
                         timestamp varchar(255),
                         foreign key (receiverID) references users (userID),
                         foreign key (senderID) references users (userID)
);
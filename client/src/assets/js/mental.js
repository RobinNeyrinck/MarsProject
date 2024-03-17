"use strict";

function initMental() {
    getMentalScore();
    fetchAlerts();
    fetchGeneralData();
}

function fetchAlerts() {
    get(`users/${loadFromStorage("userId")}/alerts`, filterAlerts);
}

function filterAlerts(res) {
    const alerts = res["alerts"];
    const emergencies = alerts.filter(alert => alert['type'] === 'EMERGENCY');
    buildAlerts(emergencies);
}

function buildAlerts(emergencies) {
    const maxToShow = 7;
    const $alerts = document.querySelector("#alerts div");
    const displayedAlerts = takeFirstXAmount(emergencies, maxToShow);
    const hiddenAlerts = emergencies;

    injectDisplayedAlerts(displayedAlerts, $alerts);
    injectHiddenAlerts(hiddenAlerts, $alerts);
    addViewMoreButton(hiddenAlerts, $alerts);
}

function getMentalScore() {
    getFromOtherGroup("03", `users/${loadFromStorage("userId")}`, showData);
}

function showData(res) {
    calculateMentalScore("mental", res["status"]);
}

function fetchGeneralData() {
    get(`/user?userID=${loadFromStorage("userId")}`, buildGeneralData);
}

function buildGeneralData(res) {
    const name = `${res["firstname"]} ${res["lastname"]}`;
    insertData(name, res["avatar"]);
    fetchBirthday();
}

function fetchBirthday() {
    get(`/users/${loadFromStorage("userId")}/Medical`, processData);
}

function processData(res) {
    const birthday = new Date(res["birthdate"]).toDateString();
    insertBirthdayData(birthday);

}

function insertBirthdayData(birthday) {
    const $birthday = document.querySelector("#general-data #birthday");
    $birthday.innerHTML = birthday;
}

function insertData(name, image) {
    const $avatar = document.querySelector("img[alt='Your Avatar']");
    const $name = document.querySelector("#general-data #name");
    $avatar.setAttribute("src", image);
    $name.innerHTML = name;
}

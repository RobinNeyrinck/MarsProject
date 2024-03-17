"use strict";

const _detailedFriend = "detailed-friend";

function initDetailedFriend() {
    addRemoveFriendEventListener();
    fetchData();
    fetchFriends();
}

function fetchData() {
    const friendName = loadFromStorage(_detailedFriend).replace(" ", "-");
    get(`user?name=${friendName}`, displayData);
}

function displayData(res){
    displayFriendName(res);
    fetchAlerts(res.userID);
    fetchAppointments(res.userID);
    getPhysicalScore(res.userID);
    getMentalScore(res.userID);
}

function buildMentalHealthChart(score) {
    const ctx = document.querySelector(".doughnut-chart-mental .doughnut-chart").getContext("2d");
    const data = score;
    const configuration = {
        type: "doughnut",
        data: {
            datasets: [
                {
                    data: [data, 100 - data],
                    backgroundColor: ["black", "white"],
                }]
        },
        options: {
            cutout: "90%",
        }
    };
    showMentalProgress(data);
    new Chart(ctx, configuration); //NOSONAR
}
function getMentalScore(userID) {
    getFromOtherGroup("03", `users/${userID}`, showData);
}

function showData(res) {
    calculateMentalScore(_detailedFriend, res["status"]);
}

function getPhysicalScore(userID) {
    get(`users/${userID}/Medical`, (res) => {
                calculatePhysicalScore(res.healthScores);
            });
}

function showMentalProgress(data) {
    const $mentalProgress = document.querySelector("#mental-progress h4");
    const $feedback = document.querySelector("#mental-health .feedback p");
    $mentalProgress.innerHTML = `${data} / 100`;
    $feedback.innerHTML += data;
}

function showPhysicalProgress(data) {
    const $mentalProgress = document.querySelector("#physical-progress h3");
    const $feedback = document.querySelector("#physical-health .feedback p");
    $mentalProgress.innerHTML = `${data} / 100`;
    $feedback.innerHTML += data;
}

function displayFriendName(friendData) {
    const $friendName = document.querySelector('h1#friend-name');

    $friendName.innerHTML = `${friendData.firstname} ${friendData.lastname}'s Statistics`;
}

function addRemoveFriendEventListener() {
    document.querySelector('button#remove-friend').addEventListener('click', processRemoveFriend);
}

function processRemoveFriend() {
    const friendName = loadFromStorage(_detailedFriend).replace(" ", "-");
    getFriendID(friendName);
}

function getFriendID(friendName) {
    get(`user?name=${friendName}`, (res) => {
        removeFriend(res.userID);
    });
}

function removeFriend(friendID) {
    remove(`users/${loadFromStorage("userId")}/friends/${friendID}`, moveToFriends);
}

function moveToFriends() {
    window.location.href = "./friends.html";
}

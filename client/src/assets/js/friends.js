"use strict";
const _userID = loadFromStorage("userId");

function initFriends() {
    fetchRecommendations();
    fetchFriends();
    fetchAlerts();
    fetchAppointments();
    generateDataForFriendsScores();
    document.querySelector('form').addEventListener('submit', processAddNewFriend);
    document.querySelector('#find-friend img').addEventListener('click', processAddNewFriend);
}

function fetchRecommendations() {
    get(`users/${_userID}/recommendations`, processRecommendations);
}

function processRecommendations(res) {
    injectRecommendation(res);
}

function showBestAndWorstScores(users) {
    const bestScore = users.reduce((best, user) => {
        if (user.healthScores.length - 1 > best.healthScores.length - 1) {
            return user;
        }
        return best;
    });
    const worstScore = users.reduce((worst, user) => {
        if (user.healthScores.length - 1 < worst.healthScores.length - 1) {
            return user;
        }
        return worst;
    });
    showBestScore(bestScore);
    showWorstScore(worstScore);
}

function showBestScore(bestScore) {
    const $bestImprovement = document.querySelector('#best-score p');
    get(`user?userID=${bestScore.userID}`, res => {
        const username = `${res.firstname} ${res.lastname}`;
        $bestImprovement.innerHTML = `${username} has the best score right now!`;
    });
}

function showWorstScore(worstScore) {
    const $worstImprovement = document.querySelector('#worst-score p');
    get(`user?userID=${worstScore.userID}`, res => {
        const username = `${res.firstname} ${res.lastname}`;
        $worstImprovement.innerHTML = `${username} has the worst score right now!`;
    });
}

function generateDataForFriendsScores() {
    get(`users/${_userID}/friends`, (res) => {
        getMedicalScores(res.users);
    });
}

function getMedicalScores(users) {
    get(`users/Medical`, res => {
        filterFriends(users, res.medicalData);
    });
}

function filterFriends(friends, allMedicalData) {
    const userIDs = friends.map(user => user.userID);
    const filteredFriends = [];
    allMedicalData.filter(data => {
        if (userIDs.includes(data.userID)) {
            filteredFriends.push(data);
        }
    });
    drawLineGraph(filteredFriends);
    showBestAndWorstScores(filteredFriends);
}

function injectRecommendation(json) {
    const numberOfRecommendations = 3;
    const $recommendations = document.querySelector('#recommendations ul');
    $recommendations.innerHTML = '';

    json["recommendations"].slice(0, numberOfRecommendations).map(recommendation => {
        $recommendations.insertAdjacentHTML("beforeend", `<li>${recommendation["activityName"]}</li>`);
    });
}

function fetchFriends() {
    get(`users/${_userID}/friends`, processFriends);
}

function processFriends(res) {
    injectFriend(res);
    addEventForDetailedFriend();
}

function injectFriend(json) {
    const $friends = document.querySelector('aside');
    $friends.innerHTML = '';

    json["users"].map(friend => {
        const name = `${friend["firstname"]} ${friend["lastname"]}`;
        $friends.insertAdjacentHTML("beforeend", `<img src="${friend["avatar"]}" alt="${name}" title="${name}">`);
    });
}

function fetchAlerts(userid = _userID) {
    get(`users/${userid}/alerts`, processAlerts);
}

function processAlerts(res) {
    injectAlert(res);
}

function injectAlert(json) {
    const $alerts = document.querySelector('#alerts div');
    const displayedAlerts = takeFirstXAmount(json["alerts"]);
    const hiddenAlerts = json["alerts"];

    injectDisplayedAlerts(displayedAlerts, $alerts);
    injectHiddenAlerts(hiddenAlerts, $alerts);
    addViewMoreButton(hiddenAlerts, $alerts);
}

function fetchAppointments() {
    get(`users/${_userID}/appointments`, processAppointments);
}

function processAppointments(res) {
    injectAppointments(res);
}

function injectDisplayedAppointments(displayedAppointments, $appointments) {

    displayedAppointments.map(app => {
        buildAppointment(app, $appointments);
    });
}

function injectHiddenAppointments(displayedAppointments, $appointments) {
    displayedAppointments.map(app => {
        buildAppointment(app, $appointments, "hidden");
    });
}

function injectAppointments(json) {
    const $appointments = document.querySelector('#appointments div');
    const sortedAppointments = sortAppointments(json["appointments"]);
    const displayedAppointments = takeFirstXAmount(sortedAppointments);
    const hiddenAppointments = sortedAppointments;

    injectDisplayedAppointments(displayedAppointments, $appointments);
    injectHiddenAppointments(hiddenAppointments, $appointments);
    addViewMoreButton(hiddenAppointments, $appointments);
}

function processAddNewFriend(e) {
    e.preventDefault();
    const $inputField = document.querySelector('form input');
    const input = $inputField.value;

    if (input === '') {
        showErrorMessageForFriendInput("Fill in friends name");
    } else {
        $inputField.value = '';
        input.trim();
        if (!input.includes(' ')) {
            showErrorMessageForFriendInput("Friends name must contain firstname and lastname");
        } else {
            const splittedName = input.split(' ').map(word => word[0].toUpperCase() + word.substring(1));
            const fullname = splittedName.join('-');
            fetchFriendID(fullname);
        }
    }
}

function fetchFriendID(name) {
    return get(`user?name=${name}`, processFriendID, showErrorMessageForFriendInput);
}

function processFriendID(res) {
    postNewFriend(res.userID);
}

function postNewFriend(friendID) {
    post(`users/${_userID}/friends/${friendID}`, null, () => {
        document.location.reload();
    });
}

function showErrorMessageForFriendInput(error) {
    console.error(error);
    const $findFriend = document.querySelector('#find-friend');

    $findFriend.insertAdjacentHTML('beforeend', `<p class="error-message"> ${error}</p>`);
}

function addEventForDetailedFriend() {
    const $friends = document.querySelectorAll('#friends img');
    $friends.forEach($elem => $elem.addEventListener("click", navigateToDetailedFriend));
}

function navigateToDetailedFriend(e) {
    const friendName = e.target.closest("img").title;
    saveToStorage("detailed-friend", friendName);
    window.location.href = `./detailed-friend.html`;
}

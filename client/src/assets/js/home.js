'use strict';

function initHomePage() {
    getAppointments();
    getAlerts();
    getSteps();
}

function getAppointments() {
    get(`/users/${loadFromStorage("userId")}/appointments`, addAppointmentsToClient);
}

function addAppointmentsToClient(response) {
    showAppointments(response);
}

function getSteps() {
    get(`/users/${loadFromStorage("userId")}/Medical`, addStepsToClient);
}

function addStepsToClient(response) {
    const measurements = response["measurements"];
    const calorieMeasurements = measurements.filter(measurement => measurement["type"] === "CalorieMeasurement");
    const lastMeasurement = calorieMeasurements[calorieMeasurements.length - 1];
    const calorieGoal = lastMeasurement.value;

    buildPieChart("#doughnut-chart", lastMeasurement.caloriesBurnt, calorieGoal);
    showSteps(lastMeasurement, calorieGoal);
}

function showAppointments(res) {
    const $appointments = document.querySelector("#appointments");
    const appointments = sortAppointments(res["appointments"]);
    const displayedAppointments = takeFirstXAmount(appointments);
    const hiddenAppointments = appointments;

    displayedAppointments.forEach(app => {
        buildAppointment(app, $appointments);
    });

    hiddenAppointments.forEach(app => {
        buildAppointment(app, $appointments, "hidden");
    });
    addViewMoreButton(hiddenAppointments, $appointments);
}

function showSteps(lastMeasurement, calorieGoal) {
    const $steps = document.querySelector("#step-data");
    $steps.insertAdjacentHTML('afterbegin',
        `<p><strong>${lastMeasurement.footsteps}</strong></p>`);
    $steps.insertAdjacentHTML('beforeend',
        `<p>${lastMeasurement.caloriesBurnt}/${calorieGoal} kcal</p>`);
}

function getAlerts() {
    get(`/users/${loadFromStorage("userId")}/alerts`, showAlerts);
}

function showAlerts(response) {
    const friendsInNeed = response["alerts"].filter(alert => alert['type'] === 'EMERGENCY');
    const notifications = response["alerts"].filter(alert => alert['type'] !== 'EMERGENCY');

    showFriendsInNeed(friendsInNeed);
    showNotifications(notifications);
}

function addDisplayedFriendInNeed($friendsInNeed, alert) {
    $friendsInNeed.insertAdjacentHTML('beforeend',
        `<div>
            <h3>${alert.name} needs help from a friend!</h3>
            <p>${alert.description}</p>
        </div>`);
}

function addHiddenFriendInNeed($friendsInNeed, hiddenAlert) {
    $friendsInNeed.insertAdjacentHTML('beforeend',
        `<div class="hidden">
            <h3>${hiddenAlert.name} needs help from a friend!</h3>
            <p>${hiddenAlert.description}</p>
        </div>`);
}

function showFriendsInNeed(friendsInNeed) {
    const $friendsInNeed = document.querySelector("#friends-in-need");
    $friendsInNeed.innerHTML = "";
    const displayedFriendsInNeed = takeFirstXAmount(friendsInNeed);
    const hiddenFriendsInNeed = friendsInNeed;

    displayedFriendsInNeed.forEach(alert => {
        addDisplayedFriendInNeed($friendsInNeed, alert);
    });

    hiddenFriendsInNeed.forEach(hiddenAlert => {
        addHiddenFriendInNeed($friendsInNeed, hiddenAlert);
    });
    addViewMoreButton(hiddenFriendsInNeed, $friendsInNeed);
}

function determineNotificationType(alert) {
    let h3 = "";
    let description = "";

    switch (alert["type"]) {
        case 'FRIEND_REQUEST':
            h3 = `New friend request: ${alert.name}.`;
            description = alert.name + " wants to be your friend!";
            break;
        case 'NEW_FRIEND':
            h3 = alert.name + " accepted your friend request!";
            description = 'You are now friends!';
            break;
        default:
            throw new Error("AlertType not found");
    }
    return {h3, description};
}

function addHiddenNotificationToHTML($notifications, h3, description) {
    $notifications.insertAdjacentHTML('beforeend',
        `<div class="hidden">
            <h3>${h3}</h3>
            <p>${description}</p>
        </div>`);
}

function addDisplayedNotificationToHTML($notifications, h3, description) {
    $notifications.insertAdjacentHTML('beforeend',
        `<div>
            <h3>${h3}</h3>
            <p>${description}</p>
        </div>`);
}

function showNotifications(notifications) {
    const $notifications = document.querySelector("#notifications");
    $notifications.innerHTML = "";
    const displayedNotifications = takeFirstXAmount(notifications);
    const hiddenNotifications = notifications;

    displayedNotifications.forEach(alert => {
        const {h3, description} = determineNotificationType(alert);
        addDisplayedNotificationToHTML($notifications, h3, description);
    });
    hiddenNotifications.forEach(alert => {
        const {h3, description} = determineNotificationType(alert);
        addHiddenNotificationToHTML($notifications, h3, description);
    });

    addViewMoreButton(hiddenNotifications, $notifications);
}

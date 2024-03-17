let api;
const _amountOfItemsToShow = 3;

const _vapid_key = "BLydROMDPetgxalNDbYQr3Jo3ufdgJ0jYIBzV5korpmfgYXGyFj9mCZBhaTcKZIv2DlWgSCn0JIpwVN0fb7isQA";

document.addEventListener("DOMContentLoaded", loadConfig);

function init() {
    initPushNotifications();
    loadInitFromOtherPages();
    loginAsDefaultUser();
    createOpenNavEvent();
    navigation();
}

function loadInitFromOtherPages() {
    if (window.location.pathname.toString().includes('index.html') || !window.location.pathname.toString().includes('.html')) {
        initHomePage();
    } else if (window.location.pathname.toString().includes('friends.html')) {
        initFriends();
    } else if (window.location.pathname.toString().includes('physical.html')) {
        initPhysical();
    } else if (window.location.pathname.toString().includes('messages.html')) {
        initMessages();
    } else if (window.location.pathname.toString().includes('detailed-friend.html')) {
        initDetailedFriend();
    } else if (window.location.pathname.toString().includes('mental.html')) {
        initMental();
    }
}

function loginAsDefaultUser() {
    if (localStorage.getItem("userId") === null) {
        saveToStorage("userId", 2);
    }
}

function loadConfig() {
    fetch("config.json")
        .then(resp => resp.json())
        .then(config => {
            api = `${config.host ? config.host + '/' : ''}${config.group ? config.group + '/' : ''}api/`;
            init();
        });
}

function initPushNotifications() {
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register("sw.js");
        navigator.serviceWorker.ready.then(registerNotifications);
    }
}

function registerPush() {
    const subscriberOptions = {
        userVisibleOnly: true,
        applicationServerKey: urlBase64ToUInt8Array(_vapid_key)
    };
    navigator.serviceWorker.ready.then(reg => {
        return reg.pushManager.subscribe(subscriberOptions);
    }).then(sub => {
        post(`/users/${loadFromStorage("userId")}/subscribe`, sub);
    });
}

function urlBase64ToUInt8Array(base64String) {
    const padding = "=".repeat((4 - base64String.length % 4) % 4);
    const base64 = (base64String + padding)
        .replace(/_/g, '+')
        .replace(/_/g, '/');
    const rawData = window.atob(base64);
    const outputArray = new Uint8Array(rawData.length);

    for (let i = 0; i < rawData.length; i++) {
        outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
}

function registerNotifications() {
    if ("Notification" in window) {
        Notification.requestPermission().then(permission => {
            if (permission === "granted") {
                registerPush();
            }
        });
    }
}

function createOpenNavEvent() {
    document.querySelector("img[alt=\"hamburgermenu\"]").addEventListener('click', openNav);

}

function openNav() {
    document.getElementById("overlay").style.display = "block";
    createCloseNavEvent();
}

function createCloseNavEvent() {
    document.querySelector("main").addEventListener('click', closeNav);

}

function closeNav() {
    document.getElementById("overlay").style.display = "none";
}

function navigation() {
    document.querySelector("#overlay ul").addEventListener('click', navigateToNextPage);
}

function navigateToNextPage(e) {
    e.preventDefault();
    let destination = e.target.closest("li").innerText.toLowerCase();
    if (e.target.closest("li").innerText.toLowerCase() === "home") {
        destination = "index";
        window.location.href = `./${destination}.html`;
    } else {
        window.location.href = `./${destination}.html`;
    }
}

function addViewMoreButton(list, $destination) {
    if (list.length > 0) {
        $destination.insertAdjacentHTML('beforeend', `<button class="view-button">View more</button>`);
        $destination.classList.add('more-available');
        const $viewButtons = document.querySelectorAll(`.view-button`);
        $viewButtons.forEach(button => {
            button.removeEventListener('click', checkMoreOrLessItems);
            button.addEventListener('click', checkMoreOrLessItems);
        });
    }
}

function checkMoreOrLessItems(e) {
    if (e.target.innerText === "View more") {
        viewMoreItems(e);
    } else {
        viewLessItems(e);
    }
}

function viewMoreItems(e) {
    e.preventDefault();
    for (const child of e.target.closest('.more-available').children) {
        child.classList.remove('hidden');
    }
    e.target.innerText = "View less";
}

function viewLessItems(e) {
    e.preventDefault();
    const lastChildren = takeNotFirstXAmount(convertNodeListToArray(e.target.closest('.more-available').children));
    for (const child of lastChildren) {
        if (!child.classList.contains("view-button")) {
            child.classList.add('hidden');
        }
    }
    e.target.innerText = "View more";
}

function injectDisplayedAlerts(displayedAlerts, $alerts) {
    displayedAlerts.map(alert => {
        buildAlert(alert, $alerts);
    });
}

function buildAlert(alert, $alerts, classToBeAdded = "none") {
    const $template = $alerts.querySelector("template").content.firstElementChild.cloneNode(true);

    if (classToBeAdded === "hidden") {
        $template.classList.add(classToBeAdded);
    }

    $template.querySelector("h3").innerText = alert.name;
    $template.querySelector("p").innerText = alert.description;
    $template.querySelector("img").src = "./images/location.svg";
    $template.querySelector("img").alt = "location";
    $template.querySelector("img").title = "location";
    $template.querySelector("p em").innerText = alert.location;

    $alerts.insertAdjacentHTML("beforeend", $template.outerHTML);
}

function injectHiddenAlerts(hiddenAlerts, $alerts) {
    hiddenAlerts.map(alert => {
        buildAlert(alert, $alerts, "hidden");
    });
}

function showDailyProgress(type, data) {
    let selector;
    if (type === "physical") {
        selector = "#physical-health";
    } else if (type === "mental") {
        selector = "#mental-health";
    } else if (type === "daily-score") {
        selector = "#daily-score";
    }

    const $monday = document.querySelector(`${selector} .monday`);
    const $tuesday = document.querySelector(`${selector}  .tuesday`);
    const $wednesday = document.querySelector(`${selector}  .wednesday`);
    const $thursday = document.querySelector(`${selector}  .thursday`);
    const $friday = document.querySelector(`${selector} .friday`);
    const $saturday = document.querySelector(`${selector} .saturday`);
    const $sunday = document.querySelector(`${selector} .sunday`);

    showDailyScore($monday, data[0]);
    showDailyScore($tuesday, data[1]);
    showDailyScore($wednesday, data[2]);
    showDailyScore($thursday, data[3]);
    showDailyScore($friday, data[4]);
    showDailyScore($saturday, data[5]);
    showDailyScore($sunday, data[6]);
}

function showDailyScore(element, score) {
    element.innerHTML = `${score}%`;
    element.style.width = `${score}%`;
}

function showProgress(selector, data, status) {
    const $progress = document.querySelector(`#${selector} h4`);
    const $feedback = document.querySelector(`#${selector} p`);
    $progress.innerHTML = `${data} / 100`;
    $feedback.innerHTML += `<em>${status}</em>`;
}

function checkStatus(average) {
    if (average === 100) {
        return "excellent";
    } else if (average >= 80) {
        return "very good";
    } else if (average >= 60) {
        return "good";
    } else if (average >= 40) {
        return "average";
    } else if (average >= 20) {
        return "poor";
    } else if (average >= 0) {
        return "bad";
    } else {
        return "unknown";
    }
}

function buildAppointment(appointment, $location, classToBeAdded = "none") {
    const $template = $location.querySelector("template").content.firstElementChild.cloneNode(true);

    if (classToBeAdded === "hidden") {
        $template.classList.add("hidden");
    }

    $template.querySelector("h3").innerHTML = `New Appointment: ${removeUselessText(appointment.date)}`;
    $template.querySelector("p").innerHTML = appointment.description;
    $template.querySelector("img").src = "./images/location.svg";
    $template.querySelector("img").alt = "location";
    $template.querySelector("img").title = "location";
    $template.querySelector("em").innerHTML = appointment.location;

    $location.insertAdjacentHTML("beforeend", $template.outerHTML);
}

function removeUselessText(text) {
    return text.replace("T", " ").replace(".000", "");
}

function sortAppointments(appointments) {
    return appointments.sort((a, b) => {
        if (a.date.includes("Z")) {
            a.date = a.date.replace("Z", "");
        } else if (b.date.includes("Z")) {
            b.date = b.date.replace("Z", "");
        }
        return new Date(a.date + "Z") - new Date(b.date + "Z");
    });
}

function saveToStorage(key, value) {
    if (localStorage) {
        return localStorage.setItem(key, JSON.stringify(value));
    }
    return false;
}

function loadFromStorage(key) {
    if (localStorage) {
        return JSON.parse(localStorage.getItem(key));
    }
    return false;
}

function convertNodeListToArray(nodeList) {
    return Array.prototype.slice.call(nodeList);
}

function takeFirstXAmount(list, amount = _amountOfItemsToShow) {
    return list.splice(0, amount);
}

function takeNotFirstXAmount(list) {
    list.map(item => {
        if (item.nodeName.includes("TEMPLATE")) {
            list.splice(list.indexOf(item), 1);
        }
    });

    return list.splice(_amountOfItemsToShow, list.length);
}

function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

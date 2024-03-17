"use strict";

let _friend;

function initMessages() {
    fetchCurrentUser();
    fetchFriendsList();
    document.querySelector('input[type="text"]').addEventListener('keyup', filterFriends);
}

function fetchCurrentUser() {
    get(`user?userID=${loadFromStorage("userId")}`, showCurrentUserAvatar);
}

function showCurrentUserAvatar(response) {
    const ownAvatar = response["avatar"];
    document.querySelector("#friends-menu div").insertAdjacentHTML("afterbegin",
        `<img alt="Profile Picture" src="${ownAvatar}">`);
}

function fetchFriendsList() {
    get(`users/${loadFromStorage('userId')}/friends`, processFriendsList);
}

function filterFriends(e) {
    e.preventDefault();
    fetchFilteredFriends(e.target.value);
}

function fetchFilteredFriends(filter) {
    get(`users/${loadFromStorage('userId')}/friends`, (res) => {
        processFilteredFriends(res, filter);
    });
}

function processFilteredFriends(res, filter) {
    const filteredFriends = {
        users: []
    };

    res.users.map(friend => {
        if (friend["firstname"].toLowerCase().includes(filter.toLowerCase()) || friend["lastname"].toLowerCase().includes(filter.toLowerCase())) {
            filteredFriends.users.push(friend);
        }
    });
    injectResults(filteredFriends);
}

function processFriendsList(res) {
    injectResults(res);
    addEventListenersForFriends();
}

function injectResults(friends) {
    const $friendsList = document.querySelector('#friends-menu aside');
    $friendsList.innerHTML = '';

    if (friends.users.length === 0) {
        const html = `<p class="error">No friends found</p>`;
        $friendsList.insertAdjacentHTML("beforeend", html);
    }

    buildStandardMessages(friends.users[0]);

    friends["users"].map(friend => {
        const name = `${friend["firstname"]} ${friend["lastname"]}`;
        const html = `<article>
                        <img src="${friend["avatar"]}" alt="${name}" title="${name}">
                        <h3>${name}</h3>
                    </article>`;
        $friendsList.insertAdjacentHTML("beforeend", html);
    });
    addEventListenersForFriends();
}

function addEventListenersForFriends() {
    const $friends = document.querySelectorAll('#friends-menu aside article');
    for (const $friend of $friends) {
        $friend.addEventListener('click', switchToFriend);
    }
}

function switchToFriend(e) {
    e.preventDefault();
    let friend = e.target.closest('article').querySelector("h3").innerText;
    friend = friend.replace(" ", "-");
    fetchFriendByName(friend);
}

function fetchFriendByName(friendName) {
    get(`user?name=${friendName}`, getAllMessagesForConversation);
}

function getAllMessagesForConversation(res) {
    _friend = res["userID"];
    buildHeader(res);
    get(`users/${loadFromStorage('userId')}/messages/${res["userID"]}`, processMessages);
}

function buildStandardMessages(friend) {
    _friend = friend["userID"];
    buildHeader(friend);
    get(`users/${loadFromStorage("userId")}/messages/${friend.userID}`, buildMessages);
}

function processMessages(res) {
    injectMessages(res);
}

function injectMessages(messages) {
    buildMessages(messages);
}

function buildHeader(friend) {
    const $messagesMenu = document.querySelector('#messages-menu');
    $messagesMenu.innerHTML = '';
    const friendName = `${friend["firstname"]} ${friend["lastname"]}`;

    const html = `
        <div id="header">
            <img src="${friend.avatar}" alt="${friendName}" title="${friendName}">
            <h2>${friendName}</h2>
            <img id="add-appointment" src="images/appointment.svg" alt="Create a new appointment" title="Create a new appointment" />
        </div>
        <div id="messages-screen">
            <div id="messages"></div>
        </div>`;

    $messagesMenu.insertAdjacentHTML("beforeend", html);
    buildForm();
    initAppointments();
}

function checkIncomingOrOutgoing(message) {
    if (message.senderID === loadFromStorage('userId')) {
        return "outgoing";
    } else {
        return "incoming";
    }
}

function buildMessages(messages) {
    const $messages = document.querySelector('#messages-menu #messages');
    const sortedMessages = sortMessages(messages.messages);

    if (sortedMessages.length === 0) {
        $messages.insertAdjacentHTML("beforeend", `<p id="no-messages">No messages to show</p>`);
    }

    sortedMessages.map(message => {
        const messageClass = checkIncomingOrOutgoing(message);
        const html = `<p class="${messageClass}">${message["message"]}</p>`;

        $messages.insertAdjacentHTML("beforeend", html);
    });
}

function buildForm() {
    const $messagesScreen = document.querySelector('#messages-screen');
    const html = `<form>
                <input type="text" placeholder="Send a message" id="chat-message">
                <input type="submit" value="Send Message" id="send-message">
            </form>`;

    $messagesScreen.insertAdjacentHTML("beforeend", html);

    initChatroom();
}

function sortMessages(messages) {
    return messages.sort((a, b) => {
        return new Date(a["timestamp"]) - new Date(b["timestamp"]);
    });
}

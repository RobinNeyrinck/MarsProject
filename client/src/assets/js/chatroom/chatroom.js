"use strict";
let sendToServer;

async function initChatroom() {
    document.querySelector('#messages-menu form').addEventListener('submit', sendMessage);
    const config = await getConfig();
    sendToServer = openSocket(`${config.host}/${config.group}/events`);
}

async function getConfig() {
    const response = await fetch("config.json");
    return response.json();
}

function sendMessage(e) {
    e.preventDefault();

    const message = document.querySelector('#chat-message').value;
    const data = {type: 'message', message: message};

    sendToServer(data);

    document.querySelector('#chat-message').value = '';
}

function onMessage(error, message) {

    fetchFriendByID(_friend);

    if (error) {
        console.error(error);
    }
}

function fetchFriendByID(friendId) {
    get(`user?userID=${friendId}`, getAllMessagesForConversation);
}

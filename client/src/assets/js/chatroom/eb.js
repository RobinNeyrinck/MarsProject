const _CHNL_TO_SERVER = "events.to.server";
const _CHNL_TO_CLIENTS_BROADCAST = "events.to.clients";
const _CHNL_TO_CLIENT_UNICAST = "events.to.client.";

function openSocket(_EVENTBUS_PATH) {
    const eb = new EventBus(_EVENTBUS_PATH);

    let clientId = localStorage.getItem("clientId");

    if (localStorage.getItem('clientId') === null) {
        clientId = uuidv4();
        localStorage.setItem('clientId', clientId);
    }

    function sendToServer(message) {
        message.clientId = clientId;

        const body = {
            "message": message.message
        };

        post(`users/${loadFromStorage("userId")}/messages/${_friend}`, body);
        eb.send(_CHNL_TO_SERVER, message);
    }

    eb.onopen = function () {
        eb.registerHandler(_CHNL_TO_CLIENTS_BROADCAST, onMessage);
        eb.registerHandler(_CHNL_TO_CLIENT_UNICAST + clientId, onMessage);
    };

    return sendToServer;
}

function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}


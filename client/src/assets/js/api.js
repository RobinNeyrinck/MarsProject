function get(uri, successHandler = logJson, failureHandler = showError) {
    const request = new Request(api + uri, {
        method: 'GET',
    });
    call(request, successHandler, failureHandler);
}

function getFromOtherGroup(group, uri, successHandler = logJson, failureHandler = showError) {
    const apiOfOtherGroup = `https://project-ii.ti.howest.be/mars-${group}/api/`;
    const request = new Request(apiOfOtherGroup + uri, {
        method: 'GET',
    });
    call(request, successHandler, failureHandler);
}

function post(uri, body, successHandler = logJson, failureHandler = showError) {
    const request = new Request(api + uri, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;'
        },
        body: JSON.stringify(body)
    });

    call(request, successHandler, failureHandler);
}

function put(uri, body, successHandler = logJson, failureHandler = showError) {
    const request = new Request(api + uri, {
        method: 'PUT',
        headers: {
            'Content-type': 'application/json;'
        },
        body: JSON.stringify(body)
    });

    call(request, successHandler, failureHandler);
}

function remove(uri, successHandler = logJson, failureHandler = showError) {
    const request = new Request(api + uri, {
        method: 'DELETE',
    });

    call(request, successHandler, failureHandler);
}

function logJson(response) {
    console.log(response);
}

function showError(error) {
    console.error(error);
    document.querySelector(".error").classList.remove("hidden");
    document.querySelector(".error").innerHTML = "";
    document.querySelector(".error").insertAdjacentHTML("beforeend",`<h2>An error occurred:</h2><p>Something went wrong! Try again later.</p>`);
}

function call(request, successHandler, errorHandler) {
    fetch(request).then(res => res.json()).then(successHandler).catch(errorHandler);
}

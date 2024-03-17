"use strict";

document.addEventListener("DOMContentLoaded", returnInit);

function returnInit(){
    document.querySelector("img[alt=\"return\"]").addEventListener("click", goToPreviousPage);
}

function goToPreviousPage(){
    if (window.location.pathname.includes("detailed-friend")){
        window.location.href = "./friends.html";
    } else if (window.location.pathname.includes("specific-message")){
        window.location.href = "./messages.html";
    }
}

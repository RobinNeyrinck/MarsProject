"use strict";

const _appointmentForm = "#appointment-form";

function initAppointments() {
    document.querySelector("#header img#add-appointment").addEventListener('click', createAppointment);
    document.querySelector("#appointment-form form").addEventListener('submit', submitAppointment);
    document.querySelector("#cancel-appointment").addEventListener('click', cancelAppointment);
}

function createAppointment(e) {
    e.preventDefault();
    document.querySelector(_appointmentForm).classList.toggle("hidden");
}

function submitAppointment(e) {
    e.preventDefault();
    const $appointmentTab = document.querySelector(_appointmentForm);
    const $form = $appointmentTab.querySelector("form");

    const appointment = createAppointmentObject($form);

    $appointmentTab.classList.toggle("hidden");
    $form.reset();
    post(`users/${loadFromStorage('userId')}/appointments`, appointment);
}

function createAppointmentObject($form) {
    const location = $form.querySelector("#location").value;
    const date = $form.querySelector("#date").value;
    const time = $form.querySelector("#time").value;
    const description = $form.querySelector("#description").value;

    const splitDate = date.split("-");
    const splitTime = time.split(":");

    const dateObject = new Date(splitDate[0], (splitDate[1] - 1), splitDate[2], splitTime[0], splitTime[1]);

    return {
        location: location,
        datetime: dateObject.toISOString(),
        description: description
    };
}

function cancelAppointment(e) {
    e.preventDefault();
    document.querySelector(_appointmentForm).classList.toggle("hidden");
}

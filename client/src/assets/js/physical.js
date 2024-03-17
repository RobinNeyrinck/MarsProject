const MAX_PHYSICAL_SCORE = 100;

function initPhysical() {
    getPhysicalData();
    getUserAlerts();
    getMentalUserData();
    getUser();
    insertCalories();
}

function getPhysicalData() {
    get(`users/${loadFromStorage("userId")}/Medical`, addPhysicalDataToClient);
}

function addPhysicalDataToClient(response) {
    insertScoreChart(response);
    insertWeeklyScores(response);
    insertHeartRate(response);
    insertSteps(response);
    insertBloodStats(response);
    insertBloodTestData(response);
    insertGeneralData(response);
}

function getUserAlerts() {
    get(`users/${loadFromStorage("userId")}/alerts`, insertAlerts);
}

function getMentalUserData() {
    getFromOtherGroup('03', `users/${loadFromStorage("userId")}`, addStressMeasurement);
}

function getUser() {
    get(`user?userID=${loadFromStorage("userId")}`, addUserDataToGeneralData);
}

function insertScoreChart(json) {
    const physicalScore = json["healthScores"][0] % MAX_PHYSICAL_SCORE;

    buildPieChart("#piechart", physicalScore);
    showProgress("score", physicalScore, checkStatus(physicalScore));
}

function insertWeeklyScores(json) {
    const $monday = document.querySelector('.monday');
    const $tuesday = document.querySelector('.tuesday');
    const $wednesday = document.querySelector('.wednesday');
    const $thursday = document.querySelector('.thursday');
    const $friday = document.querySelector('.friday');
    const $saturday = document.querySelector('.saturday');
    const $sunday = document.querySelector('.sunday');

    showDailyScore($monday, json["healthScores"][0]);
    showDailyScore($tuesday, json["healthScores"][1]);
    showDailyScore($wednesday, json["healthScores"][2]);
    showDailyScore($thursday, json["healthScores"][3]);
    showDailyScore($friday, json["healthScores"][4]);
    showDailyScore($saturday, json["healthScores"][5]);
    showDailyScore($sunday, json["healthScores"][6]);
}

function insertHeartRate(json) {
    const heartRateMeasurements = json["measurements"].filter(measurement => measurement['type'] === "HeartrateMeasurement");
    const mostRecentHRM = heartRateMeasurements[heartRateMeasurements.length - 1].value;
    const $personalHeartRate = document.querySelector('#heart #personal');
    const recommendationForHRM = checkHRMStatus(mostRecentHRM);

    $personalHeartRate.insertAdjacentHTML("beforeend", `<p>${mostRecentHRM} bpm</p>
    <p>${recommendationForHRM}</p>`);

    const heartRates = [];
    const dateTimes = [];
    heartRateMeasurements.forEach(hrm => {
        heartRates.push(hrm.value);
        dateTimes.push(hrm.datetime);
    });
    createHeartRateLineChart(heartRates, dateTimes);
}

function checkHRMStatus(mostRecentHRM) {
    if (mostRecentHRM >= 200) {
        return "Unless you just did some heavy activity, go to the hospital asap.";
    } else if (mostRecentHRM >= 160) {
        return "If you did some activity: good job! Else: check a doctor.";
    } else if (mostRecentHRM >= 120) {
        return "Maybe do some yoga or something that gets you relaxed.";
    } else if (mostRecentHRM >= 90) {
        return "Take some rest.";
    } else if (mostRecentHRM >= 50) {
        return "Your heart rate is good.";
    } else if (mostRecentHRM >= 35) {
        return "If you have a very good condition, fine. Else go to a doctor!";
    } else {
        return "Are you dead?";
    }
}

function showDailyScore($day, dailyScore) {
    $day.innerHTML = `${dailyScore}%`;
    $day.style.width = `${dailyScore}%`;
}

function insertAlerts(json) {
    showAlerts(json["alerts"].filter(alert => alert['type'] === 'EMERGENCY'));
}

function showAlerts(alerts) {
    const AMOUNT_TO_SHOW = 7;
    const displayedAlerts = takeFirstXAmount(alerts, AMOUNT_TO_SHOW);
    const hiddenAlerts = alerts;

    const $alerts = document.querySelector('#alerts div');

    injectDisplayedAlerts(displayedAlerts, $alerts);
    injectHiddenAlerts(hiddenAlerts, $alerts);
    addViewMoreButton(hiddenAlerts, $alerts);
}

function insertSteps(json) {
    const measurements = json["measurements"];
    const calorieMeasurements = measurements.filter(measurement => measurement["type"] === "CalorieMeasurement");
    const lastMeasurement = calorieMeasurements[calorieMeasurements.length - 1];
    showStepsStats(lastMeasurement["footsteps"]);
}

function showStepsStats(steps) {
    const MIN_SPEED = 4;
    const LENGTH_OF_A_STEP = 0.7;
    document.querySelector("#footsteps").insertAdjacentHTML("beforeend", `${steps} steps`);
    const speed = Math.round(getRandomInt(2) + MIN_SPEED);
    document.querySelector("#speed").insertAdjacentHTML("beforeend", `${speed} km/h`);
    const distance = steps * LENGTH_OF_A_STEP * 0.001;
    document.querySelector("#distance").insertAdjacentHTML("beforeend", `${distance} km`);
    const stairs = getRandomInt(12);
    document.querySelector("#stairs").insertAdjacentHTML("beforeend", `${stairs} flights`);
    buildPieChart("#steps-pie-chart", steps);
    showStepsProgress("#steps-chart #progress", steps);
}

function showStepsProgress(selector, steps) {
    const STEPS_GOAL = 100;
    const progressPercentage = steps / STEPS_GOAL * 100;
    document.querySelector(`${selector}`).insertAdjacentHTML("beforeend",
        `<p>You reached</p><p><em>${progressPercentage}%</em></p><p>of your step goal.</p>`);
}

function addStressMeasurement(json) {
    const stressLevel = Math.ceil(json["status"][0]["level"]["stressed"] / 10);
    const $stressScores = document.querySelector("#stress-scores");
    for (const child of $stressScores.children) {
        if (child.innerText === stressLevel.toString()) {
            child.classList.add("current-stress-level");
        }
    }
    const $stressMessage = document.querySelector("#stress-message");
    $stressMessage.insertAdjacentHTML("beforeend", `<p>${determineStressMessage(stressLevel)}</p>`);
}

function determineStressMessage(stressLevel) {
    if (stressLevel >= 10) {
        return "Quit your job! Sleep! Go see a psychiatrist! Try to not kill yourself!";
    } else if (stressLevel >= 8) {
        return "Your stress level is VERY high. Do something that will get you chilled down, eg. go to a wellness.";
    } else if (stressLevel >= 6) {
        return "Maybe do some yoga or something that gets you relaxed.";
    } else if (stressLevel >= 4) {
        return "Make sure you don't get more stressed than you are right now.";
    } else if (stressLevel >= 2) {
        return "You're very relaxed, nice!";
    } else {
        return "You're a chilly Willy.";
    }
}

function insertBloodStats(json) {
    const IDEAL_SCORE = 50;
    const bloodMeasurements = json["measurements"].filter(measurement => measurement["type"] === "BloodMeasurement");
    const dateTimes = [];
    const bloodSugarLevels = [];
    const bloodPressures = [];
    const idealScores = [];
    for (let i = 0; i < bloodMeasurements.length; i++) {
        if (i % 2 === 0) {
            dateTimes.push(bloodMeasurements[i].datetime);
        } else {
            dateTimes.push("");
        }
    }
    bloodMeasurements.forEach(bloodMeasurement => {
        bloodSugarLevels.push(bloodMeasurement.bloodSugarLevel);
        bloodPressures.push(bloodMeasurement.bloodPressure);
        idealScores.push(IDEAL_SCORE);
    });
    createBloodLineChart(bloodSugarLevels, bloodPressures, dateTimes, idealScores);
}

function insertBloodTestData(json) {
    const $bloodTestData = document.querySelector("#blood-test-data #data");
    const bloodMeasurements = json["measurements"].filter(measurement => measurement["type"] === "BloodMeasurement").reverse();
    const displayedBloodTestData = takeFirstXAmount(bloodMeasurements);
    const hiddenBloodTestData = bloodMeasurements;

    displayedBloodTestData.forEach(btd => addDisplayedBloodTestData($bloodTestData, btd, determineBloodStats()));
    hiddenBloodTestData.forEach(btd => addHiddenBloodTestData($bloodTestData, btd, determineBloodStats()));
    addViewMoreButton(hiddenBloodTestData, $bloodTestData);
}

function determineBloodStats() {
    const MAX_BSL = 370;
    const MIN_BSL = 30;
    const MIN_BP = 60;
    const MAX_BP = 100;
    const bloodSugarLevel = getRandomInt(MAX_BSL) + MIN_BSL;
    const bloodPressureFirst = getRandomInt(MAX_BP) + MIN_BP;
    const bloodPressure = `${bloodPressureFirst}/${getRandomInt(MAX_BP) + MIN_BP}`;
    return {bloodSugarLevel, bloodPressureFirst, bloodPressure};
}

function addDisplayedBloodTestData($location, btd, data) {
    buildBloodTestData($location, btd, data);
}

function buildBloodTestData($location, btd, data, classToBeAdded = "none") {
    const $template = $location.querySelector("template").content.firstElementChild.cloneNode(true);

    if (classToBeAdded === "hidden") {
        $template.classList.add("hidden");
    }

    $template.querySelector(".blood-sugar p:first-of-type").innerText = data.bloodSugarLevel;
    $template.querySelector(".blood-sugar p:last-of-type").innerText = determineBSLMessage(data.bloodSugarLevel);
    $template.querySelector(".blood-pressure p:first-of-type").innerText = data.bloodPressure;
    $template.querySelector(".blood-pressure p:last-of-type").innerText = determineBPMessage(data.bloodPressureFirst);

    $location.insertAdjacentHTML("beforeend", $template.outerHTML);
}

function addHiddenBloodTestData($location, btd, data) {
    buildBloodTestData($location, btd, data, "hidden");
}

function addUserDataToGeneralData(json) {
    const $generalData = document.querySelector('#general-info .flex-container');
    const $name = document.querySelector("#general-info #name");
    $generalData.insertAdjacentHTML("afterbegin",
        `<img src="${json["avatar"]}" alt="Your Avatar" title="Your Avatar" style="width: 5rem">`);
    $name.insertAdjacentHTML("beforeend", `${json["firstname"]} ${json["lastname"]}`);
}

function checkBloodType(bloodCode) {
    switch (bloodCode) {
        case "A_POS":
            return "A+";
        case "A_NEG":
            return "A-";
        case "B_POS":
            return "B+";
        case "B_NEG":
            return "B-";
        case "AB_POS":
            return "AB+";
        case "AB_NEG":
            return "AB-";
        case "O_POS":
            return "O+";
        case "O_NEG":
            return "O-";
        default:
            return "Unknown";
    }
}

function insertGeneralData(json) {
    const $birthday = document.querySelector("#general-info #birthday");
    const $gender = document.querySelector("#general-info #gender");
    const $age = document.querySelector("#general-info #age");
    const $height = document.querySelector("#general-info #height");
    const $weight = document.querySelector("#general-info #weight");
    const $bloodType = document.querySelector("#general-info #blood-type");
    const $allergies = document.querySelector("#general-info #allergies");
    const $genetic = document.querySelector("#general-info #genetic-diseases");
    const $chronic = document.querySelector("#general-info #chronic-diseases");
    const bloodType = checkBloodType(json["bloodType"]);

    const birthday = new Date(json["birthdate"]).toDateString();
    $birthday.insertAdjacentHTML("beforeend", birthday);
    $gender.insertAdjacentHTML("beforeend", json["gender"]);
    $age.insertAdjacentHTML("beforeend", json["age"]);
    $height.insertAdjacentHTML("beforeend", `${Math.floor(json["height"])}cm`);
    $weight.insertAdjacentHTML("beforeend", `${Math.floor(json["weight"])}kg`);
    $bloodType.insertAdjacentHTML("beforeend", bloodType);
    $allergies.insertAdjacentHTML("beforeend", json["allergies"]);
    $genetic.insertAdjacentHTML("beforeend", json["geneticDiseases"]);
    $chronic.insertAdjacentHTML("beforeend", json["chronicDiseases"]);
}

function insertCalories() {
    document.querySelectorAll('.block .coloured')
        .forEach(calorieBlock => {
            const value = Math.floor(Math.random() * 100);
            calorieBlock.insertAdjacentHTML("beforeend", `<p>${value}%</p>`);
            calorieBlock.style.backgroundColor = determineColor(value);
            calorieBlock.style.width = value + "%";
        });
}

function determineColor(int) {
    if (int < 20) {
        return "red";
    } else if (int < 40) {
        return "orange";
    } else if (int < 60) {
        return "yellow";
    } else if (int < 80) {
        return "lightgreen";
    } else {
        return "green";
    }
}

function determineBSLMessage(bloodSugarLevel) {
    switch (true) {
        case bloodSugarLevel >= 300:
            return "Your blood sugar level is way too high.";
        case bloodSugarLevel >= 225:
            return "Your blood sugar level is rather high.";
        case bloodSugarLevel >= 150:
            return "Your blood sugar level is good.";
        case bloodSugarLevel >= 75:
            return "Your blood sugar level is rather low.";
        case bloodSugarLevel >= 0:
            return "Your blood sugar level is way too low.";
        default:
            return "Your blood sugar level is not valid.";
    }
}

function determineBPMessage(bloodPressure) {
    switch (true) {
        case bloodPressure >= 150:
            return "Your blood pressure is way too high.";
        case bloodPressure >= 120:
            return "Your blood pressure is rather high.";
        case bloodPressure >= 90:
            return "Your blood pressure is good.";
        case bloodPressure >= 60:
            return "Your blood pressure is rather low.";
        case bloodPressure >= 0:
            return "Your blood pressure is way too low.";
        default:
            return "Your blood pressure is not valid.";
    }
}

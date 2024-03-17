"use strict";

const _daysOfWeek = 7;

function calculateAverageFromObject(scores) {
    if (Object.keys(scores).includes("id")) {
        delete scores.id;
    }

    return calculateAverage(Object.values(scores));
}

function calculatePhysicalScore(healthScores) {
    const average = calculateAverage(healthScores);
    const status = checkStatus(average);
    const scores = healthScores.slice(0, _daysOfWeek);

    buildPieChart(".doughnut-chart-physical .doughnut-chart", average);
    showProgress("physical-progress", average, status);
    showDailyProgress("physical", scores);
}

function calculateAverage(data) {
    return Math.round(data.reduce((a, b) => {
        return a + b;
    }) / data.length);
}

function calculateMentalScore(chartSelector, data) {
    let selectorOfChart;
    let scoreSelector;
    let dailyProgressSelector;

    if (chartSelector === "detailed-friend") {
        selectorOfChart = ".doughnut-chart-mental .doughnut-chart";
        scoreSelector = "mental-progress";
        dailyProgressSelector = "mental";
    } else if (chartSelector === "mental") {
        selectorOfChart = "#piechart";
        scoreSelector = "score";
        dailyProgressSelector = "daily-score";
    }

    const scores = [];
    data.map(state => {
        scores.push(calculateAverageFromObject(state.level));
    });

    const average = calculateAverage(scores);
    const status = checkStatus(average);
    buildPieChart(selectorOfChart, average);
    showProgress(scoreSelector, average, status);
    showDailyProgress(dailyProgressSelector, scores);
}

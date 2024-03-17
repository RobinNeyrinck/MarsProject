"use strict";

const _max = 100;

function buildPieChart(selector, data, max = _max) {
    const ctx = document.querySelector(`${selector}`).getContext("2d");
    const configuration = {
        type: "doughnut",
        data: {
            datasets: [{
                data: [data, max - data],
                backgroundColor: ["#061B39", "#F6F8FC"],
            }],
        },
        options: {
            cutout: "85%"
        }
    };
    new Chart(ctx, configuration); //NOSONAR
}

function createHeartRateLineChart(heartRates, dateTimes) {
    const ctx = document.querySelector('#heart-rate-graph').getContext('2d');
    const configuration = {
        type: 'line',
        data: {
            labels: dateTimes,
            datasets: [
                {
                    data: heartRates,
                    borderColor: "red",
                    label: "Heart rates"
                }
            ]
        },
        options: {
            plugins: {
                legend: {
                    labels: {
                        boxHeight: 1
                    }
                }
            },
            scales:{
                x: {
                    max: heartRates.length,
                    grid: {
                        display: false
                    }
                },
                y:{
                    title: {
                        display: false
                    },
                    max: 240,
                    min: 0,
                    grid: {
                        display:false
                    }
                }
            },
            pointRadius: 0
        }
    };
    new Chart(ctx, configuration); //NOSONAR
}

function createBloodLineChart(bloodSugarLevels, bloodPressures, dateTimes, idealScores) {
    const ctx = document.querySelector('#line-chart').getContext('2d');
    const configuration = {
        type: 'line',
        data: {
            labels: dateTimes,
            datasets: [
                {
                    data: bloodSugarLevels,
                    borderColor: "#4CAF50",
                    label: "Blood sugar levels"
                },
                {
                    data: bloodPressures,
                    borderColor: "blue",
                    label: "Blood pressures"
                },
                {
                    data: idealScores,
                    borderColor: "red",
                    label: "Goal",
                    borderDash: [10, 5]
                }
            ]
        },
        options: {
            plugins: {
                legend: {
                    labels: {
                        boxHeight: 1
                    }
                }
            },
            scales:{
                x: {
                    max: bloodSugarLevels.length,
                    grid: {
                        display: false
                    }
                },
                y:{
                    title: {
                        display: false
                    },
                    max: 100,
                    grid: {
                        display:false
                    },
                    ticks: {
                        display: false,
                    }
                }
            },
            pointRadius: 0
        }
    };
    new Chart(ctx, configuration); //NOSONAR
}

function drawLineGraph(data) {
    const ctx = document.querySelector('.chart #line-chart').getContext("2d");
    const configuration = buildConfig(data);
    new Chart(ctx, configuration); //NOSONAR
}

function buildChartObjects(data) {
    const results = [];
    data.map(item => {
        results.push({
            label: item.userID,
            data: item.healthScores,
            borderColor: getRandomColor(),
        });
    });
    return results;
}

function getRandomColor() {
    return '#' + Math.floor(Math.random() * 16777215).toString(16);
}

function getLongestArray(data) {
    let results = [];
    data.map(item => {
        if (item.healthScores.length > results.length) {
            results = item.healthScores;
        }
    });
    return results;
}

function buildConfig(data) {
    return {
        type: "line",
        data: {
            labels: getLongestArray(data),
            datasets: buildChartObjects(data),
        },
        options: {
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                x: {
                    ticks: {
                        display: false
                    },
                    grid: {
                        display: false
                    },
                    title: {
                        display: false
                    },
                },
                y: {
                    min: 0,
                    max: _max,
                    grid: {
                        display: false
                    },
                }
            },
        },
        pointRadius: 0
    };
}

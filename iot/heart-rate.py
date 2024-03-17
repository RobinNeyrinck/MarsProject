import random

from guizero import *
from datetime import datetime
import requests


def new_heart_rate_measurement():
    now = datetime.now()
    new_rate = random.randint(65, 75)
    url = 'https://project-ii.ti.howest.be/mars-17/api/users/2/measure'
    body = {
        "type": "HeartRateMeasurement",
        "datetime": now.strftime("%m/%d/%Y-%H:%M:%S"),
        "value": new_rate
    }
    requests.post(url, json=body)
    heartrate.value = new_rate


app = App(title="Marsolutions", width=400, height=300)
title_box = Box(app, width="fill", align="top",)

title = Text(title_box, text="MarSolutions", color="red", size=40, font="Roboto")

heartrate = Text(app, text="", color="red", size=30, align="top")

heartrate.value = "..."

measure_button = PushButton(app, command=new_heart_rate_measurement, text="Measure", align="top")

app.display()





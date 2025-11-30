import http from "k6/http";
import { check, sleep } from "k6";
import { randomItem } from "https://jslib.k6.io/k6-utils/1.4.0/index.js";

export const options = {
    vus: 30,
    duration: "45s",
};

const BASE_URL = "http://onlab.msc.nemes.live";
const HOUSEHOLDS = ["692c7368c9a10b913800bd35", "692c7372c9a10b913800bd37", "692c737dc9a10b913800bd39"];
const USER_ID = "692c731ec9a10b913800bd2f";
const TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY5MmM3MzFlYzlhMTBiOTEzODAwYmQyZiIsInVzZXJuYW1lIjoiYXhlbCIsImV4cCI6MTc2NDUyNzk4MX0.hhfRroHaycH4Knt9C6-G5uLK9BQomv2Uk7plpPBRWkw";

export default function () {
    const household = randomItem(HOUSEHOLDS);

    const url = `${BASE_URL}/household/id/${household}/tasks`;

    const payload = JSON.stringify({
        title: `Load test task for household ${household}`,
        description: "Load test task creation",
        due_date: "2024.06.14",
        responsible_id: USER_ID,
        subtasks: [
            { title: "Subtask 1", type: "checkbox" },
            { title: "Subtask 2", type: "checkbox" },
            { title: "Subtask 3", type: "checkbox" },
        ],
    });

    const params = {
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${TOKEN}`,
        },
    };

    const res = http.post(url, payload, params);

    let json;
    try {
        json = res.json();
    } catch (_) {
        json = {};
    }

    check(res, {
        "status 200": (r) => r.status === 200,
        "status field correct": () => json.status === "Added"
    });

    sleep(0.5);

}

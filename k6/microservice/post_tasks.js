import http from "k6/http";
import { check, sleep } from "k6";
import { randomItem } from "https://jslib.k6.io/k6-utils/1.4.0/index.js";

export const options = {
    vus: 30,
    duration: "45s",
};

// d5ef3ab0-435e-41a9-ae5b-99153f0038d5

const BASE_URL = "http://dev.dipterv.msc.nemes.live";
const TEAMS = ["4", "5", "6"];
const USER_ID = "d5ef3ab0-435e-41a9-ae5b-99153f0038d5";
const TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvdVRRcHp5N0JkS2dVZGJIeDNLUm5JdnNpaFdybFlVMkFfYkwza0ZOcVdrIn0.eyJleHAiOjE3NjQ1MjIzNTIsImlhdCI6MTc2NDUyMjA1MiwianRpIjoiOWUyN2UzZWEtYzg3OC00ZmVkLThlNGItMzg2NmRhYmJiY2ZiIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrLm5lbWVzLmxpdmUvcmVhbG1zL21zYy1vbmxhYi10ZXN0IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImQ1ZWYzYWIwLTQzNWUtNDFhOS1hZTViLTk5MTUzZjAwMzhkNSIsInR5cCI6IkJlYXJlciIsImF6cCI6Im1zYy1vbmxhYi1taWNyb3NlcnZpY2UtY2xpZW50LXRlc3QiLCJzaWQiOiI3YmM0NGEyOC1mZjI3LTQ0ZTItODY5YS0zYTdmMDBlNGY4MmEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1tc2Mtb25sYWItdGVzdCIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsibXNjLW9ubGFiLW1pY3Jvc2VydmljZS1jbGllbnQtdGVzdCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJUZXN6dCBFbGVrIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzenRlbGVrazYiLCJnaXZlbl9uYW1lIjoiVGVzenQiLCJmYW1pbHlfbmFtZSI6IkVsZWsiLCJlbWFpbCI6InRlc3p0LmVsZWsuazZAZ21haWwuY29tIn0.IaOLz8_nHtsSnKeeCkQzav3XRJIDQPFEQw6ImZVURoucwi4sZK0JKVVLCB2aC3J_ct-CdQVqGAD6ZhA0hPTzjhWno3QurGdjrx1DcV32PjTZiuy_0lJZr0Ep_zBtaUrw1utrPE2Lz3OHwRAiUfyv7qIcmZHUwprQtVElLsoYFJhv0444Gb1KcoTYYdLHtF76sJoLg8XUL1cjdMvR6U-WcybQHcQaUltFeplPzos6QwXRQ0-GdHUGcFOgAJQp-40cdyH0ns2mXmOlSM2V5L79BLeTKIzUszyrK4gnYa4BjN94FN9EOOOvJb3N6FeBkMKkM40Fdjb28sghEvSi3if-SA";

export default function () {
    const team = randomItem(TEAMS);

    const url = `${BASE_URL}/task/tasks`;

    const payload = JSON.stringify({
        title: `Load test task for team ${team}`,
        description: "Load test task creation",
        due_date: "2024-06-14",
        responsible_id: USER_ID,
        team_id: team,
        subtasks: [
            { title: "Subtask 1", done: false },
            { title: "Subtask 2", done: false },
            { title: "Subtask 3", done: false },
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
        "status 200": (r) => r.status === 201,
        "message correct": () => json.message === "New task successfully created!"
    });

    sleep(0.5);
}

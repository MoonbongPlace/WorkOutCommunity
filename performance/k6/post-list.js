import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        post_list_load: {
            executor: 'ramping-vus',
            startVUs: 1,
            stages: [
                { duration: '30s', target: 20 },
                { duration: '30s', target: 50 },
                { duration: '30s', target: 100 },
                { duration: '1m', target: 100 },
                { duration: '30s', target: 0 },
            ],
            gracefulRampDown: '10s',
        },
    },
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const ACCESS_TOKEN = __ENV.ACCESS_TOKEN || '';

function buildHeaders() {
    const headers = {
        'Content-Type': 'application/json',
    };

    if (ACCESS_TOKEN) {
        headers.Authorization = `Bearer ${ACCESS_TOKEN}`;
    }

    return headers;
}

export default function () {
    const url = `${BASE_URL}/api/v1/posts?page=0&size=20`;

    const res = http.get(url, {
        headers: buildHeaders(),
    });

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 1000ms': (r) => r.timings.duration < 1000,
    });

    // sleep(1);
}
import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'https://community-backend-313731832668.asia-northeast3.run.app';
const ACCESS_TOKEN = __ENV.ACCESS_TOKEN;

export const options = {
    scenarios: {
        post_list_prod_load: {
            executor: 'ramping-vus',
            startVUs: 1,
            stages: [
                { duration: '1m', target: 20 },
                { duration: '1m', target: 50 },
                { duration: '1m', target: 100 },
                { duration: '1m', target: 150 },
                { duration: '1m', target: 200 },
                { duration: '30s', target: 0 },
            ],
            gracefulRampDown: '10s',
        },
    },
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<1000'],
        checks: ['rate>0.99'],
    },
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
};

export default function () {
    const res = http.get(`${BASE_URL}/api/v1/posts?page=0&size=20`, {
        headers: {
            Accept: 'application/json',
            Authorization: `Bearer ${ACCESS_TOKEN}`,
        },
        timeout: '30s',
        tags: {
            endpoint: 'post-list',
        },
    });

    check(res, {
        'status is 200': (r) => r.status === 200,
        'not 401': (r) => r.status !== 401,
        'not 403': (r) => r.status !== 403,
        'not 500': (r) => r.status !== 500,
        'not 503': (r) => r.status !== 503,
    });

    if (res.status !== 200) {
        console.log(`status=${res.status}`);
    }

    sleep(1);
}
import http from 'k6/http';
// import { check } from 'k6';
import { check, sleep } from 'k6';
export const options = {

    scenarios: {
        post_list_load: {
            executor: 'ramping-vus',
            startVUs: 1,
            stages: [
                { duration: '1m', target: 50 },
                { duration: '1m', target: 70 },
                { duration: '1m', target: 100 },
                { duration: '1m', target: 100 }, // 100 VU 유지
                { duration: '30s', target: 70 },
                { duration: '30s', target: 0 },  // 안전하게 종료
            ],
            gracefulRampDown: '10s',
        },
    },
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
    thresholds: {
        http_req_duration: ['p(95)<1000'],
        http_req_failed: ['rate<0.01'],
    },
};
// 'http://localhost:8080'
const BASE_URL = __ENV.BASE_URL || 'https://api.moonbong.kr';

function login() {
    const res = http.post(`${BASE_URL}/api/v1/auth/signin`, JSON.stringify({
        email: 'cocjfals0@naver.com',
        password: 'dkssudgktpdy@0',
    }), {
        headers: { 'Content-Type': 'application/json' },
    });

    check(res, {
        'login status is 200': (r) => r.status === 200,
    });

    const accessToken = res.json('memberSigninResult.accessToken');

    if (!accessToken) {
        throw new Error(`accessToken 추출 실패: ${res.body}`);
    }

    return accessToken;
}

export function setup() {
    return {
        accessToken: login(),
    };
}

function buildHeaders(accessToken) {
    const headers = {
        'Content-Type': 'application/json',
    };

    if (accessToken) {
        headers.Authorization = `Bearer ${accessToken}`;
    }

    return headers;
}

//C2에서 확인 (top)
// CPU (us, sy)
// load average
// 메모리 / swap
// java 프로세스 점유율

export default function (data) {
    const url = `${BASE_URL}/api/v1/posts?page=0&size=20`;

    const res = http.get(url, {
        headers: buildHeaders(data.accessToken),
    });

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 1000ms': (r) => r.timings.duration < 1000,
    });

    sleep(1);
}
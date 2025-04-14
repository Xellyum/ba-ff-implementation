import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    insecureSkipTLSVerify: true,
    noConnectionReuse: false,
    stages: [
        { duration: '3s', target: 4000 },
        { duration: '15s', target: 4000 },
    ],
};

export default function () {
    const url = 'http://localhost:8081/cache';

    const payload1 = JSON.stringify({
        "resource-management-enabled": true,
        "quality-check-enabled": true,
        "priority-queue-enabled": true
    });
    const payload2 = JSON.stringify({
        "resource-management-enabled": false,
        "quality-check-enabled": false,
        "priority-queue-enabled": false
    });

    const payload = (__ITER % 2 === 0) ? payload1 : payload2;

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let res = http.post(url, payload, params);

    check(res, {
        'status is 201': (r) => r.status === 201,
    });

    sleep(1); // think time between iterations
}

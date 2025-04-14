import http from 'k6/http';
import { check,sleep } from 'k6';

export let options = {
    insecureSkipTLSVerify: true,
    noConnectionReuse: false,
    stages: [
        { duration: '3s', target: 4000 },
        { duration: '15s', target: 4000 },
    ],
};

export default () => {
    let res = http.get('http://localhost:8080/flag?env=DEVELOPMENT');
    check(res, {
        'status was 200': (r) => r.status === 200,
    });
    sleep(1);
};

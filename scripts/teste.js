import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 1000 },
        { duration: '30s', target: 5000 },
        { duration: '20s', target: 5000 },
        { duration: '10s', target: 0 },
    ],
};

export default function () {
    http.get('http://api:8080/tickets');

    sleep(0.1);
}
import http from 'k6/http';

export const options = {
    stages: [
        { duration: '10s', target: 3000 },
        { duration: '30s', target: 5000 },
        { duration: '30s', target: 5000 },
        { duration: '20s', target: 0 },
    ]
};

export default function () {
    http.get('http://api:8080/tickets');

}

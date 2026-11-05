import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

const getDuration = new Trend('get_req_duration', true);
const getFailed = new Rate('get_req_failed');
const getReqs = new Counter('get_reqs');

const postDuration = new Trend('post_req_duration', true);
const postFailed = new Rate('post_req_failed');
const postReqs = new Counter('post_reqs');

const baseUrl = "http://api:8080";
const timeSleep = 1;

export const options = {
    stages: [
        // heavy tests
        { duration: '10s', target: 1000 },
        { duration: '30s', target: 2000 },
        { duration: '30s', target: 2000 },
        { duration: '20s', target: 0 },

        // light tests
        // { duration: '5s', target: 10 },
        // { duration: '5s', target: 25 },
        // { duration: '5s', target: 50 },
        // { duration: '5s', target: 0 },
    ],
};

export default function () {
    //GET
    const urlGetFindAll = `${baseUrl}/tickets`;
    const resGet = http.get(urlGetFindAll);

    getDuration.add(resGet.timings.duration);
    getReqs.add(1);

    const getSuccess = check(resGet, {
        'GET status is 200': (r) => r.status === 200,
    });

    getFailed.add(!getSuccess);

    if (resGet.status !== 200) {
        sleep(timeSleep);
        return;
    }

    const body = resGet.json();

    if (body.length === 0) {
        sleep(timeSleep);
        return;
    }
    //

    //POST
    const ticketId = body[0].id;

    const payload = JSON.stringify(
        {
            cpfHolder: "917.927.680-60",
            paymentType: "PIX",
            email: "test@testuser.com",
            token: "8f7665979a4a96f2e3ca6ab211b3da91",
            paymentMethodId: "master",
            installment: 3
        }
    );

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
    const urlPostBuy = `${baseUrl}/tickets/${ticketId}/buy`;
    const resPost = http.post(urlPostBuy, payload, params);

    postDuration.add(resPost.timings.duration);
    postReqs.add(1);

    const postSuccess = check(resPost, {
        'POST status is 2xx': (r) => r.status >= 200 && r.status < 300,
    });

    postFailed.add(!postSuccess);
    //

    sleep(timeSleep);
}

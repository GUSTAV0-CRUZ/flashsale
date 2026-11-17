import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

const getDuration = new Trend('get_req_duration', true);
const getFailed = new Rate('get_req_failed');
const getReqs = new Counter('get_reqs');

const postDuration = new Trend('post_req_duration', true);
const postFailed = new Rate('post_req_failed');
const postReqs = new Counter('post_reqs');

const webhookDuration = new Trend('webhook_req_duration', true);
const webhookFailed = new Rate('webhook_req_failed');
const webhookReqs = new Counter('webhook_reqs');

const baseUrl = "http://localhost:80";
const timeSleep = 1;

export const options = {
    stages: [
        // heavy tests
        // { duration: '10s', target: 500 },
        // { duration: '30s', target: 1000 },
        // { duration: '30s', target: 1000 },
        // { duration: '20s', target: 0 },

        // Test initial of get
        { duration: '10s', target: 1000 },
        { duration: '10s', target: 3000 },
        { duration: '30s', target: 5000 },
        { duration: '20s', target: 5000 },
        { duration: '10s', target: 0 },

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
    //
    // getDuration.add(resGet.timings.duration);
    // getReqs.add(1);
    //
    // const getSuccess = check(resGet, {
    //     'GET status is 200': (r) => r.status === 200,
    // });
    //
    // getFailed.add(!getSuccess);
    //
    // if (resGet.status !== 200) {
    //     sleep(timeSleep);
    //     return;
    // }
    //
    // const body = resGet.json();
    //
    // if (!Array.isArray(body) || body.length === 0) {
    //     sleep(timeSleep);
    //     return;
    // }
    // //
    //
    // //POST
    // const ticketId = body[0].id;
    //
    // const payload = JSON.stringify(
    //     {
    //         cpfHolder: "917.927.680-60",
    //         paymentType: "PIX",
    //         email: "test@testuser.com",
    //         token: "8f7665979a4a96f2e3ca6ab211b3da91",
    //         paymentMethodId: "master",
    //         installment: 3
    //     }
    // );
    //
    // const params = {
    //     headers: {
    //         'Content-Type': 'application/json',
    //     },
    // };
    // const urlPostBuy = `${baseUrl}/tickets/${ticketId}/buy`;
    // const resPost = http.post(urlPostBuy, payload, params);
    //
    // postDuration.add(resPost.timings.duration);
    // postReqs.add(1);
    //
    // const postSuccess = check(resPost, {
    //     'POST status is 2xx': (r) => r.status >= 200 && r.status < 300,
    // });
    //
    // postFailed.add(!postSuccess);
    // //
    //
    // if (resPost.status < 200 || resPost.status >= 300) {
    //     sleep(timeSleep);
    //     return;
    // }
    //
    // const buyResponseBody = resPost.json();
    // const orderId = buyResponseBody.orderId;
    //
    // if (!orderId) {
    //     sleep(timeSleep);
    //     return;
    // }
    //
    // sleep(1);
    //
    // const urlWebhook = `${baseUrl}/order/webHook/payment?data.id=ORD1234`;
    // // const urlWebhook = `http://localhost:8080/order/webHook/payment?data.id=ORD1234`;
    // const action = Math.random() < 0.85 ? "order.processed" : "order.failed";
    // // const action = "order.failed";
    //
    // const payloadWebhook = JSON.stringify({
    //     data: {
    //         external_reference: orderId
    //     },
    //     action
    // });
    //
    // const resWebhook = http.post(urlWebhook, payloadWebhook, params);
    //
    // webhookDuration.add(resWebhook.timings.duration);
    // webhookReqs.add(1);
    //
    // const webhookSuccess = check(resWebhook, {
    //     'Webhook status is 200': (r) => r.status === 200,
    // });
    //
    // webhookFailed.add(!webhookSuccess);
    //
    // sleep(timeSleep);
    sleep(0.1);
}

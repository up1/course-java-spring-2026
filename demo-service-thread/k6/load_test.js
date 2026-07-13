import http from 'k6/http';
import { check } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 100 },   // ramp up to 100 VUs
    { duration: '30s',  target: 500 },  // spike to 500 VUs
    { duration: '10s', target: 0 },     // ramp down
  ],
};

export default function () {
  const targetHost = __ENV.TARGET_HOST || 'localhost';
  const res = http.get(`http://${targetHost}:8080/aggregate`);
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 3000ms': (r) => r.timings.duration < 3000,
  });
}
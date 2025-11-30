import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  stages: [
    {duration:"45s", target:500},
    {duration:"10s", target:500},
    { duration: "20s", target: 0 }, // cooldown
  ],
};

export default function () {
  const res = http.get("http://localhost:8080/api/products");

  check(res, {
    "status 200": (r) => r.status === 200
  });

  sleep(1);
}

import http from "k6/http"
import {check,sleep} from "k6"

export const options = {
    stages: [
    { duration: "30s", target: 50 },   // Warm-up
    { duration: "30s", target: 100 },
    { duration: "30s", target: 200 },
    { duration: "30s", target: 400 },
    { duration: "30s", target: 600 },
    { duration: "30s", target: 800 },  // Có thể tăng lên 1000, 1500, 2000
    { duration: "30s", target: 0 },    // Cool-down
  ],
}

export default function(){
    const url="http://localhost:8080/api/accounts/login";

    const payload= JSON.stringify({
        username:"admin",
        password:"password123"
    });

    const param={
        headers:{
            "Content-Type":"application/json",
        },
    };

    const res=http.post(url,payload,param);

    check(res,{
        "status 200": (r)=> r.status === 200
    });

    sleep(1);
}
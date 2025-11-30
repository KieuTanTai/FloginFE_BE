import http from "k6/http"
import {check,sleep} from "k6"

export const options = {
    stages:[
        {duration:"30s",target:100},
        {duration:"10s",target:100},
        {duration:"10s",target:0},
    ]
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
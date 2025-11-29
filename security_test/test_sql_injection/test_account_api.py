import requests
from utils.log_result import ResultLogger

BASE_URL = "http://localhost:8080/api/accounts"  # Backend server URL

logger = ResultLogger('account_api')

def safe_json(response):
    try:
        return response.json()
    except Exception:
        return response.text

# Test login

def test_login(payload):
    payload = {
        "username": payload,
        "password": payload
    }
    response = requests.post(f"{BASE_URL}/login", json=payload)
    result = safe_json(response)
    print("Login status:", response.status_code)
    def simplify_response(resp):
        if isinstance(resp, str):
            if resp.strip().startswith('<!doctype html>'):
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (400 - Bad Request)"
            if "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException" in resp or "Caused by: java.lang.NumberFormatException" in resp:
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
            if len(resp) > 500 and ("Exception" in resp or "Trace" in resp):
                return "Lỗi server hoặc exception. Xem log server để biết chi tiết."
        return resp
    simple_result = simplify_response(result)
    print("Login response:", simple_result)
    logger.log(payload, {"status": response.status_code, "response": simple_result})

# Test create account

def test_create_account(payload):
    payload = {
        "username": payload,
        "password": payload,
        "email": payload
    }
    response = requests.post(BASE_URL, json=payload)
    result = safe_json(response)
    print("Create status:", response.status_code)
    def simplify_response(resp):
        if isinstance(resp, str):
            if resp.strip().startswith('<!doctype html>'):
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (Bad Request)"
            if "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException" in resp or "Caused by: java.lang.NumberFormatException" in resp:
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
            if len(resp) > 500 and ("Exception" in resp or "Trace" in resp):
                return "Lỗi server hoặc exception. Xem log server để biết chi tiết."
        return resp
    simple_result = simplify_response(result)
    print("Create response:", simple_result)
    logger.log(payload, {"status": response.status_code, "response": simple_result})

# Test get account by id

def test_get_account_by_id(payload):
    payload = {"id": payload}
    response = requests.get(f"{BASE_URL}/{payload['id']}")
    result = safe_json(response)
    def simplify_response(resp):
        if isinstance(resp, str):
            if resp.strip().startswith('<!doctype html>'):
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (400 - Bad Request)"
            if "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException" in resp or "Caused by: java.lang.NumberFormatException" in resp:
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
            if len(resp) > 500 and ("Exception" in resp or "Trace" in resp):
                return "Lỗi server hoặc exception. Xem log server để biết chi tiết."
        if isinstance(resp, dict):
            if resp.get("status") == 400 and resp.get("error") == "Bad Request":
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (400 - Bad Request)"
            if "MethodArgumentTypeMismatchException" in str(resp.get("message", "")):
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
        return resp
    simple_result = simplify_response(result)
    print("Get by ID status:", response.status_code)
    print("Get by ID response:", simple_result)
    logger.log(payload, {"status": response.status_code, "response": simple_result})

# Test get all accounts

def test_get_all_accounts():
    payload = {}
    response = requests.get(BASE_URL)
    result = safe_json(response)
    print("Get all status:", response.status_code)
    print("Get all response:", result)
    logger.log(payload, {"status": response.status_code, "response": result})

# Test update account

def test_update_account(payload):
    payload = {
        "id": payload,
        "username": payload,
        "password": payload,
        "email": payload
    }
    response = requests.put(f"{BASE_URL}/{payload['id']}", json=payload)
    result = safe_json(response)
    def simplify_response(resp):
        if isinstance(resp, str):
            if resp.strip().startswith('<!doctype html>'):
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (400 - Bad Request)"
            if "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException" in resp or "Caused by: java.lang.NumberFormatException" in resp:
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
            if len(resp) > 500 and ("Exception" in resp or "Trace" in resp):
                return "Lỗi server hoặc exception. Xem log server để biết chi tiết."
        if isinstance(resp, dict):
            if resp.get("status") == 400 and resp.get("error") == "Bad Request":
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (400 - Bad Request)"
            if "MethodArgumentTypeMismatchException" in str(resp.get("message", "")):
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
        return resp
    simple_result = simplify_response(result)
    print("Update status:", response.status_code)
    print("Update response:", simple_result)
    logger.log(payload, {"status": response.status_code, "response": simple_result})

# Test delete account

def test_delete_account(payload):
    payload = {"id": payload}
    response = requests.delete(f"{BASE_URL}/{payload['id']}")
    result = safe_json(response)
    def simplify_response(resp):
        if isinstance(resp, str):
            if resp.strip().startswith('<!doctype html>'):
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (400 - Bad Request)"
            if "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException" in resp or "Caused by: java.lang.NumberFormatException" in resp:
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
            if len(resp) > 500 and ("Exception" in resp or "Trace" in resp):
                return "Lỗi server hoặc exception. Xem log server để biết chi tiết."
        if isinstance(resp, dict):
            if resp.get("status") == 400 and resp.get("error") == "Bad Request":
                return "Yêu cầu không hợp lệ hoặc sai định dạng. (400 - Bad Request)"
            if "MethodArgumentTypeMismatchException" in str(resp.get("message", "")):
                return "Sai kiểu dữ liệu hoặc giá trị đầu vào. (Type mismatch/Number format error)"
        return resp
    simple_result = simplify_response(result)
    print("Delete status:", response.status_code)
    print("Delete response:", simple_result)
    logger.log(payload, {"status": response.status_code, "response": simple_result})

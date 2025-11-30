import requests
from utils.log_result import ResultLogger

BASE_URL = "http://localhost:8080/api/categories"
logger = ResultLogger('category_api')

def safe_json(response):
    try:
        return response.json()
    except Exception:
        return response.text

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

def test_get_all_categories(payload):
    response = requests.get(BASE_URL)
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Get all categories status:", response.status_code)
    print("Get all categories response:", simple_result)
    logger.log({"payload": payload}, {"status": response.status_code, "response": simple_result})

def test_get_category_by_id(payload):
    response = requests.get(f"{BASE_URL}/{payload}")
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Get category by ID status:", response.status_code)
    print("Get category by ID response:", simple_result)
    logger.log({"id": payload}, {"status": response.status_code, "response": simple_result})

def test_create_category(payload):
    data = {"name": payload}
    response = requests.post(BASE_URL, json=data)
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Create category status:", response.status_code)
    print("Create category response:", simple_result)
    logger.log(data, {"status": response.status_code, "response": simple_result})

def test_update_category(payload):
    data = {"id": payload, "name": payload}
    response = requests.put(f"{BASE_URL}/{payload}", json=data)
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Update category status:", response.status_code)
    print("Update category response:", simple_result)
    logger.log(data, {"status": response.status_code, "response": simple_result})

def test_delete_category(payload):
    response = requests.delete(f"{BASE_URL}/{payload}")
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Delete category status:", response.status_code)
    print("Delete category response:", simple_result)
    logger.log({"id": payload}, {"status": response.status_code, "response": simple_result})

if __name__ == "__main__":
    test_get_all_categories()
    test_create_category("TestCategory")
    test_get_category_by_id(1)
    test_update_category(1, "UpdatedCategory")
    test_delete_category(1)
    logger.save()

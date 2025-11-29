import requests
from utils.log_result import ResultLogger

BASE_URL = "http://localhost:8080/api/products"
logger = ResultLogger('product_api')

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

def test_get_all_products(payload):
    response = requests.get(BASE_URL)
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Get all products status:", response.status_code)
    print("Get all products response:", simple_result)
    logger.log({"payload": payload}, {"status": response.status_code, "response": simple_result})

def test_get_product_by_id(payload):
    response = requests.get(f"{BASE_URL}/{payload}")
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Get product by ID status:", response.status_code)
    print("Get product by ID response:", simple_result)
    logger.log({"id": payload}, {"status": response.status_code, "response": simple_result})

def test_create_product(payload):
    data = {"name": payload, "price": payload, "categoryId": payload}
    response = requests.post(BASE_URL, json=data)
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Create product status:", response.status_code)
    print("Create product response:", simple_result)
    logger.log(data, {"status": response.status_code, "response": simple_result})

def test_update_product(payload):
    data = {"id": payload, "name": payload, "price": payload, "categoryId": payload}
    response = requests.put(f"{BASE_URL}/{payload}", json=data)
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Update product status:", response.status_code)
    print("Update product response:", simple_result)
    logger.log(data, {"status": response.status_code, "response": simple_result})

def test_delete_product(payload):
    response = requests.delete(f"{BASE_URL}/{payload}")
    result = safe_json(response)
    simple_result = simplify_response(result)
    print("Delete product status:", response.status_code)
    print("Delete product response:", simple_result)
    logger.log({"id": payload}, {"status": response.status_code, "response": simple_result})

if __name__ == "__main__":
    test_get_all_products()
    test_create_product("TestProduct", 100000, 1)
    test_get_product_by_id(1)
    test_update_product(1, "UpdatedProduct", 200000, 1)
    test_delete_product(1)
    logger.save()

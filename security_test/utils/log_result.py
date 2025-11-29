import json
import os
from datetime import datetime

RESULT_DIR = "results_test"

class ResultLogger:
    def __init__(self, test_type: str):
        self.test_type = test_type
        self.dir_path = os.path.join(RESULT_DIR, test_type)
        os.makedirs(self.dir_path, exist_ok=True)
        self.run_id = self._get_next_run_id()
        self.file_path = os.path.join(self.dir_path, f"run{self.run_id}.json")
        self.payload_file_name = None
        self.src = None
        self.results = []

    def _get_next_run_id(self):
        files = [f for f in os.listdir(self.dir_path) if f.startswith("run") and f.endswith(".json")]
        if not files:
            return 1
        ids = [int(f.split("run")[-1].split(".json")[0]) for f in files]
        return max(ids) + 1

    def set_payload_info(self, payload_file):
        src_links = {
            "sql_union_select_payloads.txt": "https://github.com/payloadbox/sql-injection-payload-list?tab=readme-ov-file#generic-union-select-payloads",
            "sql_injection_payloads.txt": "https://github.com/payloadbox/sql-injection-payload-list?tab=readme-ov-file#generic-sql-injection-payloads",
            "sql_base_error_payloads.txt": "https://github.com/payloadbox/sql-injection-payload-list?tab=readme-ov-file#generic-error-based-payloads",
            "sql_injection_time_base_payloads.txt": "https://github.com/payloadbox/sql-injection-payload-list?tab=readme-ov-file#generic-time-based-sql-injection-payloads",
            "sql_injection_auth_payloads.txt": "https://github.com/payloadbox/sql-injection-payload-list?tab=readme-ov-file#sql-injection-auth-bypass-payloads"
        }
        self.payload_file_name = os.path.basename(payload_file) if payload_file else ""
        self.src = src_links.get(self.payload_file_name, "")

    def log(self, input_data, output_data):
        entry = {
            "timestamp": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "input": input_data,
            "output": output_data
        }
        self.results.append(entry)

    def save(self):
        output = {
            "from_payloads": self.payload_file_name,
            "src": self.src,
            "results": self.results
        }
        with open(self.file_path, "w", encoding="utf-8") as f:
            json.dump(output, f, ensure_ascii=False, indent=2)
import pathlib
import os

def find_txt_files_recursive(directory_path):
    txt_files = []
    for path in pathlib.Path(directory_path).rglob('*.txt'):
        txt_files.append(str(path))    
    return txt_files

def return_content_of_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    return content

if __name__ == "__main__":
    directory = os.path.join(os.path.dirname(__file__), '..', 'sql_payloads')
    txt_files = find_txt_files_recursive(directory)
    for file_path in txt_files:
        content = return_content_of_file(file_path)
        print(f"--- Content of {file_path} ---")
        print(content)
        print("\n")

def get_payload_files():
    directory = os.path.join(os.path.dirname(__file__), '..', 'sql_payloads')
    return find_txt_files_recursive(directory)
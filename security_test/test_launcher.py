import sys

MENU = '''\
============================
  SECURITY TEST LAUNCHER
============================
1. Test Account API
2. Test Category API
3. Test Product API
4. Exit
============================
Choose an option: '''

def get_files_payloads():
    import sql_payloads.return_file_payloads as sql_payloads
    return sql_payloads.get_payload_files()

def run_account_api(payload, payload_file, api_to_test_index=0):
    import test_sql_injection.test_account_api as test_account_api
    test_account_api.logger.set_payload_info(payload_file)
    print("\n--- Running Account API tests ---\n")
    if api_to_test_index == 0:
        test_account_api.test_login(payload)
        test_account_api.logger.save()
    elif api_to_test_index == 1:
        test_account_api.test_create_account(payload)
        test_account_api.logger.save()
    elif api_to_test_index == 2:
        test_account_api.test_get_account_by_id(payload)
        test_account_api.logger.save()
    elif api_to_test_index == 3:
        test_account_api.test_get_all_accounts()
        test_account_api.logger.save()
    elif api_to_test_index == 4:
        test_account_api.test_update_account(payload)
        test_account_api.logger.save()
    elif api_to_test_index == 5:
        test_account_api.test_delete_account(payload)
        test_account_api.logger.save()
    print("\n--- Done Account API tests ---\n")

def run_category_api(payload, payload_file, api_to_test_index=0):
    import test_sql_injection.test_category_api as test_category_api
    test_category_api.logger.set_payload_info(payload_file)
    print("\n--- Running Category API tests ---\n")
    if api_to_test_index == 0:
        test_category_api.test_get_all_categories(payload)
        test_category_api.logger.save()
    elif api_to_test_index == 1:
        test_category_api.test_create_category(payload)
        test_category_api.logger.save()
    elif api_to_test_index == 2:
        test_category_api.test_get_category_by_id(payload)
        test_category_api.logger.save()
    elif api_to_test_index == 3:
        test_category_api.test_update_category(payload)
        test_category_api.logger.save()
    elif api_to_test_index == 4:
        test_category_api.test_delete_category(payload)
        test_category_api.logger.save()
    print("\n--- Done Category API tests ---\n")

def run_product_api(payload, payload_file, api_to_test_index=0):
    import test_sql_injection.test_product_api as test_product_api
    test_product_api.logger.set_payload_info(payload_file)
    print("\n--- Running Product API tests ---\n")
    if api_to_test_index == 0:
        test_product_api.test_get_all_products(payload)
        test_product_api.logger.save()
    elif api_to_test_index == 1:
        test_product_api.test_create_product(payload)
        test_product_api.logger.save()
    elif api_to_test_index == 2:
        test_product_api.test_get_product_by_id(payload)
        test_product_api.logger.save()
    elif api_to_test_index == 3:
        test_product_api.test_update_product(payload)
        test_product_api.logger.save()
    elif api_to_test_index == 4:
        test_product_api.test_delete_product(payload)
        test_product_api.logger.save()
    print("\n--- Done Product API tests ---\n")

def select_file_payload():
    import sql_payloads.return_file_payloads as sql_payloads
    payload_files = sql_payloads.get_payload_files()
    print("Available payload files:")
    for idx, file in enumerate(payload_files):
        print(f"{idx + 1}. {file}")
    choice = int(input("Select a file by number: ")) - 1
    if 0 <= choice < len(payload_files):
        selected_file = payload_files[choice]
        content = sql_payloads.return_content_of_file(selected_file)
        return selected_file, content
    else:
        print("Invalid choice.")
        return None, None


def main():
    while True:
        choice = input(MENU)
        if choice == '1':
            selected_file, content = select_file_payload()
            if selected_file and content:
                print(f"Selected file: {selected_file}")
                api_index = int(input('''Select API to test:
                    0. Login
                    1. Create Account
                    2. Get Account by ID
                    3. Get All Accounts
                    4. Update Account
                    5. Delete Account
                    Choose an option: '''))
                for line in content.splitlines():
                    run_account_api(line, selected_file, api_index)
        elif choice == '2':
            selected_file, content = select_file_payload()
            if selected_file and content:
                print(f"Selected file: {selected_file}")
                api_index = int(input('''Select Category API to test:
                    0. Get All Categories
                    1. Create Category
                    2. Get Category by ID
                    3. Update Category
                    4. Delete Category
                    Choose an option: '''))
                for line in content.splitlines():
                    run_category_api(line, selected_file, api_index)
        elif choice == '3':
            selected_file, content = select_file_payload()
            if selected_file and content:
                print(f"Selected file: {selected_file}")
                api_index = int(input('''Select Product API to test:
                    0. Get All Products
                    1. Create Product
                    2. Get Product by ID
                    3. Update Product
                    4. Delete Product
                    Choose an option: '''))
                for line in content.splitlines():
                    run_product_api(line, selected_file, api_index)
        elif choice == '4':
            print("Exiting...")
            sys.exit(0)
        else:
            print("Invalid choice. Please try again.")

if __name__ == "__main__":
    main()

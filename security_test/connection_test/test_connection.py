import security_test.connection_test.db_connect as db_connect

__api__ = 'http://'

def test_connection() -> None:
    print("Testing database connection...")
    conn = None
    try:
        conn = db_connect.get_connection()
        assert conn is not None, "Connection should be established"
        print("Connection established!")
    finally:
        if conn:
            conn.close()

def main() -> None:
    test_connection()
    print("All tests passed.")

if __name__ == "__main__":
    main()
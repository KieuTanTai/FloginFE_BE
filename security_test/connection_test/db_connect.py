import jproperties
import mariadb

# Read properties file
configs = jproperties.Properties()
with open('app-config.properties', 'rb') as prop_file:
    configs.load(prop_file)

url = configs.get('db.url').data
username = configs.get('db.username').data
password = configs.get('db.password').data
# Extract host, port, db from url
import re
match = re.match(r'jdbc:mariadb://([^:/]+):(\d+)/(\w+)', url)
if not match:
    raise ValueError('Invalid DB URL format')
host, port, database = match.groups()

def get_connection():
    conn = mariadb.connect(
        user=username,
        password=password,
        host=host,
        port=int(port),
        database=database
    )
    return conn

if __name__ == "__main__":
    conn = get_connection()
    print("Connection successful!", conn)
    conn.close()

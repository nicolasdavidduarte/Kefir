import psycopg2

# Database connection parameters
conn = psycopg2.connect(
    dbname="Kefir",
    user="postgres",
    password="postgres",
    host="localhost",
    port="5432"
)

# Create a cursor object
cur = conn.cursor()

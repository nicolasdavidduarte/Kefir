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

# Execute the query to fetch all rows from the "loan" table
cur.execute("SELECT * FROM loan;")

# Fetch all rows from the result of the query
rows = cur.fetchall()

# Print the fetched rows
for row in rows:
    print(row)

# Close the cursor and connection
cur.close()
conn.close()
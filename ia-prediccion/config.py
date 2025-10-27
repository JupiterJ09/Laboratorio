import pymysql
from pymysql.cursors import DictCursor

def get_connection():
    return pymysql.connect(
        host="localhost",
        user="root",
        password="puma99912",  # cambia si es diferente
        database="laboratorio",
        cursorclass=DictCursor
    )
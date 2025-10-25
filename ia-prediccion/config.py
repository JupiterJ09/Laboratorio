import pymysql
from pymysql.cursors import DictCursor

def get_connection():
    return pymysql.connect(
        host="localhost",
        user="root",
        password="Grecia1.",  # cambia si es diferente
        database="laboratorio",
        cursorclass=DictCursor
    )
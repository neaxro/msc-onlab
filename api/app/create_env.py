import os

def create_env_variables():
    os.environ["MONGODB_CONNECTION_URL"] = "mongodb://localhost:27017"
    os.environ["TOKEN_SECRET_KEY"] = "boti_kerge_lesz"

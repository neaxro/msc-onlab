import os

# TODO: Refactore env vars to app.config["..."] style 
def create_env_variables():
    os.environ["MONGODB_CONNECTION_URL"] = "mongodb://localhost:27017"
    os.environ["MONGODB_DATABASE_NAME"] = "msc_onlab"
    os.environ["MONGODB_COLLECTION_USERS"] = "users"
    os.environ["MONGODB_COLLECTION_HOUSEHOLDS"] = "households"
    os.environ["TOKEN_SECRET_KEY"] = "boti_kerge_lesz"
    os.environ["APP_FOLDER_PATH"] = os.path.dirname(os.path.abspath(__file__))

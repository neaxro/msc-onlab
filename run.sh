# Start virtual environment
. api/.venv/bin/activate

# Export all needed variables
export FLASK_ENV="development"
export MONGODB_CONNECTION_URL="mongodb://root:pass@localhost:27017"
export MONGODB_DATABASE_NAME="msc_onlab"
export MONGODB_COLLECTION_USERS="users"
export MONGODB_COLLECTION_HOUSEHOLDS="households"
export MONGODB_COLLECTION_INVITATIONS="invitations"
export TOKEN_SECRET_KEY="boti_kerge_lesz"
export APP_FOLDER_PATH="api/app"

# Run application
python3 api/app.py

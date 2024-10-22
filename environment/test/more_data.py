import argparse
import requests
import hashlib
import argcomplete
import uuid
import jwt  # Import the JWT library
import random  # Import random for generating random subtask counts
import getpass  # Import getpass for password input
from faker import Faker  # Import Faker for generating random names

fake = Faker()  # Initialize the Faker instance

def get_uuid():
    return uuid.uuid4().hex[:4]

# Generate random profile pictures
PROFILE_PICTURES = ['man_1', 'man_2', 'man_3', 'man_4', 'woman_1', 'woman_2', 'woman_3']

def login(base_url, username, password) -> str:
    headers = {
        "Content-Type": "application/json"
    }
    
    # Hash the password using SHA-256
    hashed_password = hashlib.sha256(password.encode('utf-8')).hexdigest()
    
    data = {
        "username": username,
        "password": hashed_password
    }
    
    response = requests.post(f"{base_url}/auth/login", json=data, headers=headers)
    
    if response.status_code == 200:
        return response.json()['data']['token']
    else:
        raise Exception(f"Login failed with status code {response.status_code}: {response.text}")

def decode_jwt(token):
    try:
        # Decode the JWT token to get user data
        decoded = jwt.decode(token, options={"verify_signature": False})  # Disable verification for decoding
        return {
            "id": decoded.get("id"),
            "username": decoded.get("username"),
            "profile_picture": decoded.get("profile_picture"),
            "exp": decoded.get("exp")
        }
    except Exception as e:
        print(f"Failed to decode token: {e}")
        return None

def create_users(base_url, count):
    headers = {
        "Content-Type": "application/json"
    }
    
    for i in range(count):
        # Generate random first name, last name, and email
        first_name = fake.first_name()
        last_name = fake.last_name()
        username = f"{first_name.lower()}{last_name.lower()}"
        email = f"{first_name}.{last_name}@gmail.com"
        profile_picture = random.choice(PROFILE_PICTURES)
        password = "Asdasd11"  # Hardcoded password
        
        # Create user registration data
        data = {
            "first_name": first_name,
            "last_name": last_name,
            "username": username,
            "email": email,
            "profile_picture": profile_picture,
            "password": hashlib.sha256(password.encode('utf-8')).hexdigest()
        }

        # Make the POST request to register the user
        response = requests.post(f"{base_url}/auth/register", json=data, headers=headers)
        
        if response.status_code == 200:
            print(f"[{i+1}/{count}] User '{username}' created successfully.")
        else:
            print(f"Failed to create user '{username}'. Status code: {response.status_code}, Error: {response.text}")

def init_parser():
    parser = argparse.ArgumentParser(
        prog="moreData",
        description="Script that creates multiple test data via API"
    )
    
    parser.add_argument("baseurl", nargs="?", default="http://127.0.0.1:5000", help="Base URL of the API")
    parser.add_argument("-u", "--username", required=True, help="API username")
    parser.add_argument("-p", "--password", help="API password (optional, will be prompted if missing)")
    
    # Add the type argument with choices
    parser.add_argument("-t", "--type", choices=['household', 'task', 'users'], required=True, help="Type of data to create (e.g., household, task, users)")
    
    parser.add_argument("-c", "--count", type=int, required=True, help="Number of items to create")

    # Add household_id as a required argument if type is 'task'
    parser.add_argument("-hi", "--household_id", help="Household ID (required for task type)", required=False)
    
    # Add subtask_range for task creation
    parser.add_argument("-sr", "--subtask_range", help="Subtask range (required for task type)", required=False, default=0, type=int)
    
    # Add subtask_range_min for task creation
    parser.add_argument("-srm", "--subtask_range_min", help="Subtask range minimum (required for task type)", required=False, default=0, type=int)
    
    # Enable autocompletion
    argcomplete.autocomplete(parser)
    
    return parser.parse_args()

def main():
    args = init_parser()
    
    # Prompt for password if not provided
    if not args.password:
        args.password = getpass.getpass("Password: ")  # Securely prompt for password if missing
    
    # Log in and get the token
    try:
        token = login(args.baseurl, args.username, args.password)
        user_data = decode_jwt(token)  # Decode the JWT token to fetch user data
        if user_data:
            print(f"User Data: {user_data}")  # Print the user data
    except Exception as e:
        print(f"Error during login: {e}")
        exit(1)

    # Call create_households, create_tasks, or create_users based on the type
    if args.type == 'household':
        create_households(args.baseurl, args.count, token)
    elif args.type == 'task':
        if not args.household_id:
            print("Error: --household_id is required when creating tasks.")
            exit(1)
        if args.subtask_range_min > args.subtask_range:
            print("Error: --subtask_range_min must be less or equal to --subtask_range")
            exit(1)
        
        create_tasks(args.baseurl, args.household_id, args.count, args.subtask_range, args.subtask_range_min, token)
    elif args.type == 'users':
        create_users(args.baseurl, args.count)

if __name__ == "__main__":
    main()

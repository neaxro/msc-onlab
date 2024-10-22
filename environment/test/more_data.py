import argparse
import requests
import hashlib
import argcomplete
import uuid
import jwt  # Import the JWT library
import random  # Import random for generating random subtask counts
import getpass  # Import getpass for password input

def get_uuid():
    return uuid.uuid4().hex[:4]

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

def create_households(base_url, count, token):
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    for i in range(1, count + 1):
        title = f"{get_uuid()} - Household"
        
        data = {
            "title": title
        }
        
        response = requests.post(f"{base_url}/household", json=data, headers=headers)
        
        if response.status_code == 200:
            print(f"[{i}/{count}]  Household '{title}' created successfully.")
        else:
            print(f"Failed to create household '{title}'. Status code: {response.status_code}, Error: {response.text}")

def create_tasks(base_url, household_id, count, subtask_range, subtask_range_min, token):
    
    user_data = decode_jwt(token)
    
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    for i in range(1, count + 1):
        # Generate random number of subtasks between subtask_range_min and subtask_range
        num_subtasks = random.randint(subtask_range_min, int(subtask_range))
        
        # Create a list of subtasks
        subtasks = [
            {
                "title": f"{get_uuid()} - Subtask",
                "type": "checkbox",
                "done": False  # Defaulting to False as it's a new task
            }
            for _ in range(num_subtasks)
        ]

        data = {
            "title": f"{get_uuid()} - Task",
            "description": "Task description",
            "due_date": "2024-10-16",  # Set a sample due date, you can parameterize it if needed
            "responsible_id": user_data['id'],
            "subtasks": subtasks
        }
        
        response = requests.post(f"{base_url}/household/id/{household_id}/tasks", json=data, headers=headers)
        
        if response.status_code == 200:
            print(f"[{i}/{count}]  Task '{data['title']}' created successfully in household '{household_id}' with {num_subtasks} subtasks.")
        else:
            print(f"Failed to create task '{data['title']}'. Status code: {response.status_code}, Error: {response.text}")

def init_parser():
    parser = argparse.ArgumentParser(
        prog="moreData",
        description="Script that creates multiple test data via API"
    )
    
    parser.add_argument("baseurl", nargs="?", default="http://127.0.0.1:5000", help="Base URL of the API")
    parser.add_argument("-u", "--username", required=True, help="API username")
    parser.add_argument("-p", "--password", help="API password (optional, will be prompted if missing)")
    
    # Add the type argument with choices
    parser.add_argument("-t", "--type", choices=['household', 'task'], required=True, help="Type of data to create (e.g., household, task)")
    
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

    # Call create_households or create_tasks based on the type
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

if __name__ == "__main__":
    main()
// Switch to the database
db = db.getSiblingDB("msc_onlab");

// Create a 'users' collection and insert some test user
db.users.insertMany(
    [
        {
            "_id": ObjectId("66f40d24c53e1eb2d8bbcd0d"),
            "first_name": "Axel",
            "last_name": "Nemes",
            "username": "axel",
            "email": "axel.nemes@gmail.com",
            "profile_picture": "default",
            "password": "9098c53fe0e338cf9ef819e1048b6cf42e9d840425a43f96c64c19843ed80b61"
        },
        {
            "_id": ObjectId("66f40ea5c53e1eb2d8bbcd1b"),
            "first_name": "Alice",
            "last_name": "Anderson",
            "username": "alice",
            "email": "alice.anderson@gmail.com",
            "profile_picture": "woman_1",
            "password": "9098c53fe0e338cf9ef819e1048b6cf42e9d840425a43f96c64c19843ed80b61"
        },
        {
            "_id": ObjectId("66f40ebdc53e1eb2d8bbcd1e"),
            "first_name": "Bob",
            "last_name": "Brown",
            "username": "bob",
            "email": "bob.brown@gmail.com",
            "profile_picture": "man_2",
            "password": "9098c53fe0e338cf9ef819e1048b6cf42e9d840425a43f96c64c19843ed80b61"
        }
    ]
)


// Create a 'households' collection and insert some test household
db.households.insertMany([

    // House - Debrecen
    {
        _id: ObjectId("66f40fa950421e8ecd6ccdcf"),
        title: "House - Debrecen",
        creation_date: "2024.09.25. 13:27:05",
        people: [
            ObjectId("66f40d24c53e1eb2d8bbcd0d"),
            ObjectId("66f40ea5c53e1eb2d8bbcd1b")
        ],
        tasks: [
            {
                _id: ObjectId("66f42ba150421e8ecd6ccdff"),
                title: "Wash the Dishes",
                description: "Clean up the dishes after dinner",
                creation_date: "2024.09.25. 15:26:25",
                due_date: "2024.06.12",
                done: false,
                responsible_id: ObjectId("66f40ea5c53e1eb2d8bbcd1b"),
                subtasks: []
            },
            {
                _id: ObjectId("66f42bb650421e8ecd6cce08"),
                title: "Do the Laundry",
                description: "Wash and dry the family clothes",
                creation_date: "2024.09.25. 15:26:46",
                due_date: "2024.06.13",
                done: false,
                responsible_id: ObjectId("66f40ea5c53e1eb2d8bbcd1b"),
                subtasks: [
                    {
                        title: "Sort Clothes",
                        type: "checkbox",
                        _id: ObjectId("66f42bb650421e8ecd6cce04"),
                        done: false
                    },
                    {
                        title: "Wash Clothes",
                        type: "checkbox",
                        _id: ObjectId("66f42bb650421e8ecd6cce05"),
                        done: false
                    },
                    {
                        title: "Dry Clothes",
                        type: "checkbox",
                        _id: ObjectId("66f42bb650421e8ecd6cce06"),
                        done: false
                    },
                    {
                        title: "Fold and Put Away",
                        type: "checkbox",
                        _id: ObjectId("66f42bb650421e8ecd6cce07"),
                        done: false
                    }
                ]
            }
        ]
    },
    
    // Flat - Budapest
    {
        "_id": ObjectId("66f40fb050421e8ecd6ccdd2"),
        "title": "Flat - Budapest",
        "creation_date": "2024.09.25. 13:27:12",
        "people": [
            ObjectId("66f40d24c53e1eb2d8bbcd0d"),
            ObjectId("66f40ebdc53e1eb2d8bbcd1e")
        ],
        "tasks": [
            {
                "_id": ObjectId("66f412e150421e8ecd6ccde4"),
                "title": "Shopping",
                "description": "Buy groceries for the week",
                "creation_date": "2024.09.25. 13:40:49",
                "due_date": "2024.06.11",
                "done": false,
                "responsible_id": ObjectId("66f40d24c53e1eb2d8bbcd0d"),
                "subtasks": [
                    {
                        "_id": ObjectId("66f412e150421e8ecd6ccde0"),
                        "title": "Bread",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f412e150421e8ecd6ccde1"),
                        "title": "Milk",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f412e150421e8ecd6ccde2"),
                        "title": "Eggs",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f412e150421e8ecd6ccde3"),
                        "title": "Butter",
                        "type": "checkbox",
                        "done": false
                    }
                ]
            },
            {
                "_id": ObjectId("66f4290950421e8ecd6ccde9"),
                "title": "Mowing the lawn",
                "description": "Mow the lawn in the backyard",
                "creation_date": "2024.09.25. 15:15:21",
                "due_date": "2024.06.11",
                "done": false,
                "responsible_id": ObjectId("66f40ea5c53e1eb2d8bbcd1b"),
                "subtasks": []
            },
            {
                "_id": ObjectId("66f4294c50421e8ecd6ccdf1"),
                "title": "Take animals for a walk",
                "description": "Walk the pets around the neighborhood",
                "creation_date": "2024.09.25. 15:16:28",
                "due_date": "2024.06.11",
                "done": false,
                "responsible_id": ObjectId("66f40ebdc53e1eb2d8bbcd1e"),
                "subtasks": [
                    {
                        "_id": ObjectId("66f4294c50421e8ecd6ccdee"),
                        "title": "Walk Charlie (Dog)",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f4294c50421e8ecd6ccdef"),
                        "title": "Walk Bella (Dog)",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f4294c50421e8ecd6ccdf0"),
                        "title": "Walk Luna (Cat)",
                        "type": "checkbox",
                        "done": false
                    }
                ]
            },
            {
                "_id": ObjectId("66f4295c50421e8ecd6ccdfa"),
                "title": "Tidy the room",
                "description": "Clean up the rooms in the house",
                "creation_date": "2024.09.25. 15:16:44",
                "due_date": "2024.06.11",
                "done": false,
                "responsible_id": ObjectId("66f40d24c53e1eb2d8bbcd0d"),
                "subtasks": [
                    {
                        "_id": ObjectId("66f4295c50421e8ecd6ccdf6"),
                        "title": "Living Room",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f4295c50421e8ecd6ccdf7"),
                        "title": "Kitchen",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f4295c50421e8ecd6ccdf8"),
                        "title": "Bedroom",
                        "type": "checkbox",
                        "done": false
                    },
                    {
                        "_id": ObjectId("66f4295c50421e8ecd6ccdf9"),
                        "title": "Bathroom",
                        "type": "checkbox",
                        "done": false
                    }
                ]
            }
        ]
    },

    // Flat - Szeged
    {
        _id: ObjectId("66f40fb450421e8ecd6ccdd5"),
        title: "Flat - Szeged",
        creation_date: "2024.09.25. 13:27:16",
        people: [
            ObjectId("66f40d24c53e1eb2d8bbcd0d")
        ],
        tasks: [
            {
                _id: ObjectId("66f42bd450421e8ecd6cce17"),
                title: "Clean the Windows",
                description: "Wash and wipe down all windows in the house",
                creation_date: "2024.09.25. 15:27:16",
                due_date: "2024.06.14",
                done: false,
                responsible_id: ObjectId("66f40d24c53e1eb2d8bbcd0d"),
                subtasks: [
                    {
                        title: "Living Room Windows",
                        type: "checkbox",
                        _id: ObjectId("66f42bd450421e8ecd6cce13"),
                        done: false
                    },
                    {
                        title: "Kitchen Windows",
                        type: "checkbox",
                        _id: ObjectId("66f42bd450421e8ecd6cce14"),
                        done: false
                    },
                    {
                        title: "Bedroom Windows",
                        type: "checkbox",
                        _id: ObjectId("66f42bd450421e8ecd6cce15"),
                        done: false
                    },
                    {
                        title: "Bathroom Windows",
                        type: "checkbox",
                        _id: ObjectId("66f42bd450421e8ecd6cce16"),
                        done: false
                    }
                ]
            }
        ]
    }
]);

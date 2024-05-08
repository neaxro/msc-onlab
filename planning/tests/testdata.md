# Test data used during the tests

## Users

- Alice: `65dcf20484273f26b75734f0` (3 tasks)
- Bob: `65dcf20484273f26b75734f1`   (4 tasks)
- Emma: `65dcf20484273f26b75734f2`  (3 tasks)
- William: `65dcf20484273f26b75734f3`   (0 tasks)

## Households

| _id 	| title 	| creation_date 	| people 	| tasks |
|-----	|-------	|---------------	|-------------	|--------- |
|65dcf20484273f26b75734b0|Test Household 1|2024.01.01.|65dcf20484273f26b75734f0, 65dcf20484273f26b75734f1|65dcf20484273f26b75734a0, 65dcf20484273f26b75734a3, 65dcf20484273f26b75734a5, 65dcf20484273f26b75734a1, 65dcf20484273f26b75734a4, 65dcf20484273f26b75734a6, 65dcf20484273f26b75734a8|
|65dcf20484273f26b75734b1|Test Household 2|2024.02.03.|65dcf20484273f26b75734f2, 65dcf20484273f26b75734f3|65dcf20484273f26b75734a2, 65dcf20484273f26b75734a7, 65dcf20484273f26b75734a9|

## Tasks

| _id 	| title 	| creation_data 	| description 	| done 	| due_date 	| responsible_id 	| subtasks 	|
|-----	|-------	|---------------	|-------------	|------	|----------	|----------------	|----------	|
|65dcf20484273f26b75734a0|Wash the dishes|2024.05.08.|Test Description|False|2024.05.20.|65dcf20484273f26b75734f0|65dcf20484273f26b75734b0|
|65dcf20484273f26b75734a1|Feed the dog|2024.05.07.|Test Description|False|2024.05.21.|65dcf20484273f26b75734f1|                	|
|65dcf20484273f26b75734a2|Clean the window|2024.05.06.|Test Description|False|2024.05.10.|65dcf20484273f26b75734f2|                	|
|65dcf20484273f26b75734a3|Tidy up the room|2024.04.01.|Test Description|False|2024.05.12.|65dcf20484273f26b75734f0|                	|
|65dcf20484273f26b75734a4|Vacuum the floor|2024.05.07.|Test Description|False|2024.05.14.|65dcf20484273f26b75734f1|                	|
|65dcf20484273f26b75734a5|Mop the floor|2024.05.06.|Test Description|True|2024.05.20.|65dcf20484273f26b75734f0|                	|
|65dcf20484273f26b75734a6|Do the cooking|2024.04.01.|Test Description|True|2024.04.12.|65dcf20484273f26b75734f1|                	|
|65dcf20484273f26b75734a7|Sweep the floor|2024.05.07.|Test Description|True|2024.05.08.|65dcf20484273f26b75734f2|                	|
|65dcf20484273f26b75734a8|Clean the kitchen|2024.05.06.|Test Description|True|2024.05.30.|65dcf20484273f26b75734f1|                	|
|65dcf20484273f26b75734a9|Do the shopping|2024.04.01.|Test Description|True|2024.04.02.|65dcf20484273f26b75734f2|65dcf20484273f26b75734b1, 65dcf20484273f26b75734b2, 65dcf20484273f26b75734b3|

## Subtasks

| _id 	| title 	| done 	| type 	|
|-----	|-------	|---------------	|-------------	|
|65dcf20484273f26b75734b0|Plates|False|checkbox|
|65dcf20484273f26b75734b1|Oranges|False|checkbox|
|65dcf20484273f26b75734b2|Apple|False|checkbox|
|65dcf20484273f26b75734b3|Banana|True|checkbox|

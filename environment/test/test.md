# Test environment

## Init Database

| Household ID                     | Household Title      | Users                                                                                  | Tasks                                                                                                              |
|----------------------------------|----------------------|----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| 66f40fa950421e8ecd6ccdcf         | House - Debrecen     | Axel (66f40d24c53e1eb2d8bbcd0d), Alice (66f40ea5c53e1eb2d8bbcd1b)                   | Wash the Dishes, Do the Laundry                                                                                   |
| 66f40fb050421e8ecd6ccdd2         | Flat - Budapest      | Axel (66f40d24c53e1eb2d8bbcd0d), Bob (66f40ebdc53e1eb2d8bbcd1e)                     | Shopping, Mowing the lawn, Take animals for a walk, Tidy the room                                                  |
| 66f40fb450421e8ecd6ccdd5         | Flat - Szeged       | Axel (66f40d24c53e1eb2d8bbcd0d)                                                       | Clean the Windows                                                                                                  |

## More data with script

With the `more_data.py` you can create more test data quickly. Here are some useful commands:

### Creates 1 household for axel user
```console
python3 more_data.py  --username axel --password Asdasd11 --type household --count 1
```

### Creates 20 tasks for household with 66f40fb450421e8ecd6ccdd5 that contains 0-10 (random) subtasks
```console
python3 more_data.py  --username axel --password Asdasd11 --type task --count 20 --household_id 66f40fb450421e8ecd6ccdd5 --subtask_range 10
```

### Creates 1 task for household with 66f40fb450421e8ecd6ccdd5 that contains 10 subtasks
```console
python3 more_data.py  --username axel --password Asdasd11 --type task --count 1 --household_id 66f40fb450421e8ecd6ccdd5 --subtask_range_min 10 --subtask_range 10
```

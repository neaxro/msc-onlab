#!/bin/bash

set -e

# auth
PYTHONPATH=. pytest -v --maxfail=1 --disable-warnings --cov=auth/repository --cov=auth/service --cov-report=term-missing auth/tests

# invitation
PYTHONPATH=. pytest -v --maxfail=1 --disable-warnings --cov=invitation/repository --cov=invitation/service --cov-report=term-missing invitation/tests

# team
PYTHONPATH=. pytest -v --maxfail=1 --disable-warnings --cov=team/repository --cov=team/service --cov-report=term-missing team/tests

# task
PYTHONPATH=. pytest -v --maxfail=1 --disable-warnings --cov=task/repository --cov=task/service --cov-report=term-missing task/tests

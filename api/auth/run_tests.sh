#!/bin/bash

set -e

# Run tests
pytest -v --maxfail=1 --disable-warnings auth/tests

# Run coverage
pytest --cov=service --cov=repository --cov-report=term-missing auth/tests

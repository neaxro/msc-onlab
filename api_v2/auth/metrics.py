from prometheus_client import Summary, Counter
from functools import wraps
from flask import request
from config import Config
import time

class Metrics:
    def __new__(cls):
        if not hasattr(cls, 'instance'):
            cls.instance = super(Metrics, cls).__new__(cls)
        return cls.instance

    def __init__(self):
        config = Config()
        
        self.REQUEST_COUNT = Counter(f'{config.METRICS_PREFIX}_request_count', 'Number of requests', ['method', 'endpoint'])
        self.REQUEST_TIME = Summary(f'{config.METRICS_PREFIX}_request_time', 'Time spent processing request', ['method', 'endpoint'])
        self.REQUEST_LATENCY = Summary(f'{config.METRICS_PREFIX}_request_latency', 'Request latency', ['method', 'endpoint'])

metrics = Metrics()

def count_requests(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        metrics.REQUEST_COUNT.labels(method=request.method, endpoint=request.endpoint).inc()  # Increment request count
        return func(*args, **kwargs)
    return wrapper

def time_request(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        with metrics.REQUEST_TIME.labels(method=request.method, endpoint=request.endpoint).time():  # Measure time taken by request
            return func(*args, **kwargs)
    return wrapper

def latency_request(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        start_time = time.time()
        response = func(*args, **kwargs)
        latency = time.time() - start_time
        metrics.REQUEST_LATENCY.labels(method=request.method, endpoint=request.endpoint).observe(latency)  # Record latency
        return response
    return wrapper

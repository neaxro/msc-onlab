-- Create the 'root' role if it does not exist
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'root') THEN
      CREATE ROLE root WITH SUPERUSER LOGIN PASSWORD 'pass';
   END IF;
END
$$;

-- Create 'keycloak' database if it does not exist
CREATE DATABASE keycloak OWNER root;

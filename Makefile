.PHONY: list
list:
	@LC_ALL=C $(MAKE) -pRrq -f $(firstword $(MAKEFILE_LIST)) : 2>/dev/null | awk -v RS= -F: '/(^|\n)# Files(\n|$$)/,/(^|\n)# Finished Make data base/ {if ($$1 !~ "^[#.]") {print $$1}}' | sort | grep -E -v -e '^[^[:alnum:]]' -e '^$@$$'

.PHONY: secret-mysql
secret-mysql:
	kubectl create secret generic mysql-config --from-env-file=secrets/secret-mysql.env --dry-run=client -o yaml > flux/secret-mysql.yaml

.PHONY: secret-keycloak
secret-keycloak:
	kubectl create secret generic keycloak-config --from-env-file=secrets/secret-keycloak.env --dry-run=client -o yaml > flux/secret-keycloak.yaml

.PHONY: secret-all
secret-all: secret-keycloak secret-mysql

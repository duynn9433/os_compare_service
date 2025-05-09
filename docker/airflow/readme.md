https://airflow.apache.org/docs/apache-airflow/stable/howto/docker-compose/index.html

```script
mkdir -p ./dags ./logs ./plugins ./config ./data
echo -e "AIRFLOW_UID=$(id -u)" > .env
```
```aiignore
docker compose run airflow-cli airflow config list
```

```aiignore
docker compose up airflow-init
```
```aiignore
docker compose up
```
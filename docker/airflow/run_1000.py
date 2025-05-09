import requests
import concurrent.futures
import time
from datetime import datetime, timedelta
import argparse

# === CONFIG ===
airflow_url = "http://localhost:8080"
dag_id = "data_processing_pipeline"
username = "airflow"
password = "airflow"
total_runs = 1
max_workers = 1
poll_interval = 1  # seconds

# === Global timestamp for uniqueness ===
timestamp = datetime.utcnow().strftime("%H%M%S%d%m%y")

# === Get access token ===
def get_token():
    url = f"{airflow_url}/auth/token"
    payload = {"username": username, "password": password}
    response = requests.post(url, json=payload)
    if response.status_code in [200, 201]:
        token = response.json().get("access_token")
        print("✅ Authenticated.")
        return token
    raise Exception(f"❌ Login failed: {response.status_code} - {response.text}")

# === Trigger DAG run ===
def trigger_dag(job_id, token):
    url = f"{airflow_url}/api/v2/dags/{dag_id}/dagRuns"
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    dag_run_id = f"job_{job_id}_{timestamp}"
    logical_date = (datetime.utcnow() + timedelta(seconds=job_id)).isoformat() + "Z"
    payload = {
        "dag_run_id": dag_run_id,
        "conf": {"job_id": job_id},
        "logical_date": logical_date
    }
    resp = requests.post(url, headers=headers, json=payload)
    if resp.status_code in [200, 201]:
        print(f"[✓] Triggered {dag_run_id}")
        return dag_run_id
    else:
        print(f"[✗] {dag_run_id}: {resp.status_code} - {resp.text}")
        return None

# === Poll DAG status ===
def poll_dag_status(dag_run_ids, token):
    url_template = f"{airflow_url}/api/v2/dags/{dag_id}/dagRuns/{{}}"
    headers = {"Authorization": f"Bearer {token}"}

    for dag_run_id in dag_run_ids:
        print(f"🔄 Checking status for DAG Run ID: {dag_run_id}")
        while True:
            resp = requests.get(url_template.format(dag_run_id), headers=headers)
            if resp.status_code == 200:
                state = resp.json().get("state")
                if state in ["success", "failed"]:
                    print(f"[✔] {dag_run_id} finished with state: {state}")
                    break
                else:
                    print(f"⏳ {dag_run_id} still running, waiting {poll_interval}s...")
            else:
                print(f"[!] Failed to get status of {dag_run_id}: {resp.status_code}")
            time.sleep(poll_interval)

def parse_arguments():
    parser = argparse.ArgumentParser(description='Airflow Session Manager')
    parser.add_argument('--url', default='http://localhost:8080', help='Airflow base URL')
    parser.add_argument('--username', default='admin', help='Airflow username')
    parser.add_argument('--password', default='admin', help='Airflow password')
    parser.add_argument('--dag', default='data_processing_pipeline', help='DAG ID to trigger')
    parser.add_argument('--count', type=int, default=10, help='Number of DAG runs to trigger')
    parser.add_argument('--workers', type=int, default=10, help='Number of concurrent workers')
    parser.add_argument('--poll', type=float, default=1.0, help='Polling interval in seconds')

    return parser.parse_args()

# === Main ===
if __name__ == "__main__":
    args = parse_arguments()
    total_runs = args.count
    max_workers = args.workers
    poll_interval = args.poll

    start_time = time.time()
    token = get_token()

    # Trigger DAGs in parallel
    with concurrent.futures.ThreadPoolExecutor(max_workers=max_workers) as executor:
        futures = [
            executor.submit(trigger_dag, i, token)
            for i in range(total_runs)
        ]
        dag_run_ids = [f.result() for f in concurrent.futures.as_completed(futures) if f.result()]

    # Poll until all DAG runs complete
    poll_dag_status(dag_run_ids, token)

    total_duration = time.time() - start_time
    print(f"\n⏱️ All {len(dag_run_ids)} DAG runs completed in {total_duration:.2f} seconds.")
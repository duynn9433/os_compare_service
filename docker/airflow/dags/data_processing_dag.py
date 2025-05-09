from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime
import pandas as pd
import random
import os
import time
import uuid

BASE_DIR = "/opt/airflow/data"
os.makedirs(BASE_DIR, exist_ok=True)

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 1, 1),
    'retries': 1,
}

def create_job_folder(**context):
    """Create a unique job folder for this execution"""
    # Lấy dag_id và dag_run_id từ context
    dag_id = context['dag'].dag_id
    dag_run_id = context['dag_run'].run_id

    # Kết hợp dag_id và dag_run_id để tạo tên thư mục
    job_folder = f"{BASE_DIR}/{dag_id}_{dag_run_id}"
    os.makedirs(job_folder, exist_ok=True)

    # Tạo thư mục logs
    logs_folder = f"{job_folder}/logs"
    os.makedirs(logs_folder, exist_ok=True)

    # Ghi log khi job bắt đầu
    with open(f"{logs_folder}/job.log", 'w') as f:
        f.write(f"Job started at: {datetime.now()}\n")
        f.write(f"DAG ID: {dag_id}\n")
        f.write(f"DAG Run ID: {dag_run_id}\n")

    # Đẩy thông tin vào XCom để sử dụng ở các task khác
    context['ti'].xcom_push(key='job_id', value=f"{dag_id}_{dag_run_id}")
    context['ti'].xcom_push(key='job_folder', value=job_folder)
    context['ti'].xcom_push(key='logs_folder', value=logs_folder)

    return f"{dag_id}_{dag_run_id}"

def log_task(task_name, logs_folder, start_time, **kwargs):
    """Log task execution details"""
    end_time = time.time()
    duration = end_time - start_time

    with open(f"{logs_folder}/{task_name}.log", 'w') as f:
        f.write(f"Task: {task_name}\n")
        f.write(f"Start time: {datetime.fromtimestamp(start_time)}\n")
        f.write(f"End time: {datetime.fromtimestamp(end_time)}\n")
        f.write(f"Duration: {duration:.2f} seconds\n")

        # Log additional details if provided
        for key, value in kwargs.items():
            f.write(f"{key}: {value}\n")

def task1_create_file(**context):
    start = time.time()

    # Get job paths from XCom
    job_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='job_folder')
    logs_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='logs_folder')

    # 1000 số đầu tiên: từ 1 đến 1000, theo thứ tự
    positive_values = list(range(1, 1001))

    # 9000 số sau: ngẫu nhiên từ -1000 đến -1
    negative_values = [random.randint(-1000, -1) for _ in range(9000)]

    # Ghép lại, không shuffle
    all_values = positive_values + negative_values

    df = pd.DataFrame({'value': all_values})
    file_path = f"{job_folder}/raw_data.csv"
    df.to_csv(file_path, index=False)

    # Log task execution
    log_task(
        'task1_create_file',
        logs_folder,
        start,
        file_created=file_path,
        total_rows=len(all_values)
    )

    context['ti'].xcom_push(key='start_time', value=start)

def task2_filter_natural_numbers(**context):
    start = time.time()

    # Get job paths from XCom
    job_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='job_folder')
    logs_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='logs_folder')

    df = pd.read_csv(f"{job_folder}/raw_data.csv")
    df_filtered = df[df['value'] >= 0]
    filtered_path = f"{job_folder}/filtered_data.csv"
    df_filtered.to_csv(filtered_path, index=False)

    # Log task execution
    log_task(
        'task2_filter_natural_numbers',
        logs_folder,
        start,
        input_rows=len(df),
        output_rows=len(df_filtered),
        file_created=filtered_path
    )

def task3_calculate_stats(**context):
    start = time.time()

    # Get job paths from XCom
    job_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='job_folder')
    logs_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='logs_folder')

    df = pd.read_csv(f"{job_folder}/filtered_data.csv")
    stats = {
        'mean': df['value'].mean(),
        'min': df['value'].min(),
        'max': df['value'].max()
    }
    context['ti'].xcom_push(key='stats', value=stats)

    # Log task execution
    log_task(
        'task3_calculate_stats',
        logs_folder,
        start,
        stats=stats
    )

def task4_write_result(**context):
    start = time.time()

    # Get job paths from XCom
    job_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='job_folder')
    logs_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='logs_folder')

    stats = context['ti'].xcom_pull(task_ids='calculate_stats', key='stats')
    result_path = f"{job_folder}/result.csv"

    if stats:
        df_stats = pd.DataFrame([stats])
        df_stats.to_csv(result_path, index=False)

    # Log task execution
    log_task(
        'task4_write_result',
        logs_folder,
        start,
        stats_received=bool(stats),
        file_created=result_path if stats else "None"
    )

def task5_log_metrics(**context):
    start = time.time()

    # Get job paths from XCom
    job_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='job_folder')
    logs_folder = context['ti'].xcom_pull(task_ids='create_job_folder', key='logs_folder')
    job_id = context['ti'].xcom_pull(task_ids='create_job_folder', key='job_id')

    # Get the initial start time
    start_time = context['ti'].xcom_pull(task_ids='create_file', key='start_time')

    if start_time is None:
        raise ValueError("start_time is None. Did 'create_file' run successfully?")

    end_time = time.time()
    duration = end_time - start_time

    metrics_path = f"{job_folder}/metrics.txt"
    with open(metrics_path, 'w') as f:
        f.write(f"Job ID: {job_id}\n")
        f.write(f"Total execution time: {duration:.2f} seconds\n")
        f.write(f"System: {os.name} / {os.uname().sysname}\n")

    # Create job summary with all task logs combined
    summary_path = f"{job_folder}/job_summary.log"
    with open(summary_path, 'w') as summary_file:
        summary_file.write(f"=== JOB SUMMARY FOR {job_id} ===\n\n")

        # Read and append all log files
        log_files = [f for f in os.listdir(logs_folder) if f.endswith('.log')]
        for log_file in sorted(log_files):
            summary_file.write(f"--- {log_file} ---\n")
            with open(f"{logs_folder}/{log_file}", 'r') as task_log:
                summary_file.write(task_log.read())
            summary_file.write("\n\n")

        summary_file.write(f"Job completed at: {datetime.now()}\n")

    # Log task execution
    log_task(
        'task5_log_metrics',
        logs_folder,
        start,
        total_job_duration=f"{duration:.2f} seconds",
        metrics_file_created=metrics_path,
        summary_file_created=summary_path
    )


with DAG(
    dag_id='data_processing_pipeline',
    default_args=default_args,
    schedule=None,
    catchup=False
) as dag:

    t0 = PythonOperator(task_id='create_job_folder', python_callable=create_job_folder)
    t1 = PythonOperator(task_id='create_file', python_callable=task1_create_file)
    t2 = PythonOperator(task_id='filter_data', python_callable=task2_filter_natural_numbers)
    t3 = PythonOperator(task_id='calculate_stats', python_callable=task3_calculate_stats)
    t4 = PythonOperator(task_id='write_result', python_callable=task4_write_result)
    t5 = PythonOperator(task_id='log_metrics', python_callable=task5_log_metrics)

    t0 >> t1 >> t2 >> t3 >> t4 >> t5
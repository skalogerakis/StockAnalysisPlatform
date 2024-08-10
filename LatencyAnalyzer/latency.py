import sys
import re
import numpy as np
import os
from datetime import datetime

TS_FORMAT = "%Y-%m-%dT%H:%M:%S.%f"
startTime_dict = {}
reportTime_dict = {}
registry_latency = []

def logReader(name):
    with open(name, 'r') as file:
        for line in file:
            if "START" in line:
                batchID = batchExtractor(line)
                ts = timestampExtractor(line)
                startTime_dict[batchID] = ts
                # print(f"STARTING -> Batch ID: {batchID}, Timestamp: {ts}")
            elif "REPORT" in line:
                batchID = batchExtractor(line)
                ts = timestampExtractor(line)
                reportTime_dict[batchID] = ts
                # print(f"REPORTING -> Batch ID: {batchID}, Timestamp: {ts}")

def logReaderFolder(name):
    for x in os.listdir(name):
        with open(name+x, 'r') as file:
            for line in file:
                if "START" in line:
                    batchID = batchExtractor(line)
                    ts = timestampExtractor(line)
                    startTime_dict[batchID] = ts
                    # print(f"STARTING -> Batch ID: {batchID}, Timestamp: {ts}")
                elif "REPORT" in line:
                    batchID = batchExtractor(line)
                    ts = timestampExtractor(line)
                    reportTime_dict[batchID] = ts
                    # print(f"REPORTING -> Batch ID: {batchID}, Timestamp: {ts}")

def batchExtractor(line):
    match = re.search(r'Batch ID: (\d+),', line)
    if match:
        batch_id = match.group(1)
        return batch_id


def timestampExtractor(line):
    match = re.search(r'Timestamp: ([\d\-T:.]+),', line)
    if match:
        ts = match.group(1)
        return ts


def LatencyAnalyzer(out_path):
    f = open(out_path, "w")

    print(f"BatchID, StartingTime, ReportingTime, Latency")
    f.write(f"BatchID, StartingTime, ReportingTime, Latency\n")

    result_counter = 0
    total_latency = 0


    for item in sorted(startTime_dict.items(), key=lambda x: int(x[0])):  # Sort dictionary based on batch ID
        if reportTime_dict.__contains__(item[0]):
            result_counter += 1

            rep_time = reportTime_dict[item[0]]
            latency = datetime.strptime(rep_time, TS_FORMAT) - datetime.strptime(item[1], TS_FORMAT)
            latency_sec = latency.total_seconds()  # Latency expressed in seconds
            latency_ms = latency_sec * 1000  # Latency expressed in ms

            total_latency += latency_ms
            registry_latency.append(latency_ms) # Append all in the existing registry and then calculate statistics

            print(f"{item[0]},{item[1].split('T')[1]},{rep_time.split('T')[1]},{latency_ms}")
            f.write(f"{item[0]},{item[1].split('T')[1]},{rep_time.split('T')[1]},{latency_ms}\n")


    min_ts = datetime.strptime(min(startTime_dict.values()), TS_FORMAT)
    max_ts = datetime.strptime(max(reportTime_dict.values()), TS_FORMAT)

    print(f"\n\nAverage: {str((total_latency/result_counter)).replace('.',',')}\nMedian: {str(np.percentile(registry_latency, 50)).replace('.',',')}"
          f"\n90th Percentile: {str(np.percentile(registry_latency, 90)).replace('.',',')}\n"
          f"Min: {np.min(registry_latency)}\nMax: {np.max(registry_latency)}\nMean: {np.mean(registry_latency)}")
    f.write(
        f"\n\nAverage: {str((total_latency / result_counter)).replace('.', ',')}\nMedian: {str(np.percentile(registry_latency, 50)).replace('.', ',')}"
        f"\n90th Percentile: {str(np.percentile(registry_latency, 90)).replace('.', ',')}\n"
        f"Min: {np.min(registry_latency)}\nMax: {np.max(registry_latency)}\nMean: {np.mean(registry_latency)}")

    print(f"\n\nElapsed Time(s): {str((max_ts - min_ts).total_seconds()).replace('.',',')}, Starting Ts: {min_ts}, End Ts:{max_ts}")
    f.write(f"\n\nElapsed Time(s): {str((max_ts - min_ts).total_seconds()).replace('.',',')}, Starting Ts: {min_ts}, End Ts:{max_ts}")

    f.close()


if __name__ == '__main__':
    # file_path = sys.argv[1]
    ex_time="tim750"
    par="timer"
    day="day1"
    file_name = ex_time+".txt"
    # file_path = "/home/skalogerakis/Documents/Workspace/debs2022/LatencyAnalyzer/MyDocs/day5/" + file_name
    file_path = f"/home/skalogerakis/Documents/Workspace/debs2022/LatencyAnalyzer/CompleteComparison/{day}/{par}/{ex_time}/"

    output_path=f"/home/skalogerakis/Documents/Workspace/debs2022/LatencyAnalyzer/CompleteComparison/{day}/{par}/" + file_name

    # logReader(file_path)
    logReaderFolder(file_path)
    LatencyAnalyzer(output_path)

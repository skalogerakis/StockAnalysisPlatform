import sys
import re
import numpy as np
from datetime import datetime

startTime_dict = {}


def logReader(name):
    with open(name, 'r') as file:
        for line in file:
            if "START" in line:
                batchID = batchExtractor(line)
                ts = timestampExtractor(line)
                startTime_dict[batchID] = ts
                # print(f"STARTING -> Batch ID: {batchID}, Timestamp: {ts}")


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



def OutOfOrderAnalyzerOrdered(out_path):
    f = open(out_path, "w")

    prev = -1
    counter = 0
    total_diff = 0

    # Sort everything based on timestamp(value)
    for key, value in sorted(startTime_dict.items(), key=lambda item: item[1]):

        if prev != -1:
            diff = prev - int(key)

            # When batches are out-of-order the diff will be positive
            if diff > 0:
                print(f"Diff: {diff}, Previous: {prev}, Current: {key}")
                f.write(f"Diff: {diff}, Previous: {prev}, Current: {key}\n")
                counter += 1
                total_diff += diff

        prev = int(key)

    if counter != 0:
        print(f"AVG_Diff {total_diff / counter}, Diff Counter: {counter}")
        f.write(f"AVG_Diff {total_diff / counter}, Diff Counter: {counter}\n")
    else:
        print(f"AVG_Diff 0, Diff Counter: 0")
        f.write(f"AVG_Diff 0, Diff Counter: 0\n")

    f.close()


if __name__ == '__main__':
    # file_path = sys.argv[1]
    file_name = "par4_day1_tm4.txt"
    file_path = "/home/skalogerakis/Documents/Workspace/debs2022/LatencyAnalyzer/MyDocs/par4_day1/tm_analysis/" + file_name
    output_path = "/home/skalogerakis/Documents/Workspace/debs2022/LatencyAnalyzer/OOF/par4_day1/" + file_name

    logReader(file_path)
    OutOfOrderAnalyzerOrdered(output_path)
